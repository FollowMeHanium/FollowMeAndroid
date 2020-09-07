package com.ghdev.followme.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.ghdev.followme.R
import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener{


    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.prefs
    }

    private var callback : SessionCallback = SessionCallback()

    override fun onClick(v: View?) {
        when(v) {

            //둘러보기 버튼
            btn_look_login_act -> {
                //ApplicationData.loginState = false
                startActivity<MainActivity>()
                //finish()

            }

            //회원가입하기
            btn_signup_login_act -> {
                startActivity<SignUpActivity>()

            }

            //키보드 다운
            rl_login_act -> {
                downKeyboard(rl_login_act)
            }

            //로그인하기
            btn_login_act -> {
                Log.d("login_fun", "clicked")
                getLoginResponse()
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        //viewmodel 불러오기
        val activityLoginBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        val activityLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        activityLoginBinding.loginviewmodel = activityLoginViewModel
        //ViewModelProviders는 deprecated되었다. -> ViewModelProvider하고 ViewModelStoreOwner을 호출한다.
         */

        init()
        //getHashKey(this) //해시키값 구하기
        Session.getCurrentSession().addCallback(callback) //콜백 추가 정의
    }

    private fun init() {
        btn_look_login_act.setOnClickListener(this)
        btn_signup_login_act.setOnClickListener(this)
        rl_login_act.setOnClickListener(this)
        btn_login_act.setOnClickListener(this)
    }


    private fun downKeyboard(view : View) {
        val imm: InputMethodManager = applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /*override fun onSuccess(loginResponse: LiveData<String>) {
        loginResponse.observe(this, Observer {
            toast(it)
        })
    }

    override fun onFailure(message: String) {
        toast(message)
    }*/

    /*
    //해시키 구하기
    private fun getHashKey(context: Context) {
        try {
            val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = packageInfo.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                md.update(signature.toByteArray())
                //val str = String( Base64.encode(md.digest(), NO_WRAP))
                //Log.d("HashKey", str)
                Log.d("HashKey", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        }
        catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }
*/



    //콜백 추가 정의
    //세션 콜백 삭제
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    //카카오톡 간편로그인 실행 결과를 받아서 SDK로 전달
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private class SessionCallback : ISessionCallback {
        //로그인 실패한 상태
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("Session Call back", exception?.message)
        }

        //로그인 성공한 상태
        override fun onSessionOpened() {
            UserManagement.getInstance().me(object: MeV2ResponseCallback(){
                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("Session Call back", errorResult?.errorMessage)
                }//사용자 정보 요청 실패

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.e("Session Call back", errorResult?.errorMessage)
                }//세션 오픈 실패

                override fun onSuccess(result: MeV2Response?) {
                    checkNotNull(result) {"session response null"}
                    Log.d("TAG", "유저 가입 성공")

                    val input_kakao_id: Long = result.id

                    Log.e("Success Profile: ",input_kakao_id.toString())
                }//사용자 정보 요청 성공
            })
        }
    }




    //로그인 통신
    private fun getLoginResponse(){
        val input_email: String = et_id_login_act.text.toString()
        val input_pw: String = et_pw_login_act.text.toString()

        var jsonObject = JSONObject()
        jsonObject.put("id", input_email)
        jsonObject.put("password", input_pw)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.d("login_fun", "gson")
        val postLoginResponse: Call<PostLoginResponse> =
            networkService.postLoginResponse("application/json", gsonObject)
        postLoginResponse.enqueue(object : Callback<PostLoginResponse> {


            //통신 실패 시 수행되는 메소드
            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                Log.e("login_fail", t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse(
                call: Call<PostLoginResponse>,
                response: Response<PostLoginResponse>
            ) {
                if (response.isSuccessful) {
                   // response.body()!!.token
                    toast(response.body()!!.message)
                    finish()
                }
            }
        })

    }

    private fun saveSharedPrefs() {

    }



}

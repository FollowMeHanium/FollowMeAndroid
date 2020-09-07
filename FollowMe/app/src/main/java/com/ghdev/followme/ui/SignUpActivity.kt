package com.ghdev.followme.ui

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.ghdev.followme.R
import com.ghdev.followme.data.PostSignUpResponse
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    var year_arr = ArrayList<String>()
    var month_arr = ArrayList<String>()
    var date_arr = ArrayList<String>()
    var checkpw : Boolean = false
    private lateinit var calendar: Calendar

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onClick(v: View?) {

        when (v) {
           /* //중복확인 버튼
            btn_id_check_sign_id_set_act -> {
                //##

            }*/

            //키보드 내리기
            rl_signup_act -> {
                downKeyboard(rl_signup_act)
            }

            //가입하기
            btn_agree_sign_id_set_act -> {
                if(!checkpw) {
                    toast("비밀번호를 확인해주세요")
                } else{
                    getSignUpResponse()
                }
            }
            btn_signup_birth -> {
                BirthDiarlogCreate(v!!)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()

        //비밀번호 중복여부 확인
        et_pw_check_sign_up_act.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(et_pw_sign_up_act.text.toString().equals(et_pw_check_sign_up_act.text.toString())){
                    tv_sign_up_pw_check.setText(R.string.password_not_overlap)
                    tv_sign_up_pw_check.setTextColor(Color.parseColor("#008000"))
                    checkpw = true
                } else{
                    tv_sign_up_pw_check.setText(R.string.password_overlap)
                    tv_sign_up_pw_check.setTextColor(Color.parseColor("#ff0000"))
                    checkpw = false
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun init() {

        //btn_id_check_sign_id_set_act.setOnClickListener(this)
        rl_signup_act.setOnClickListener(this)
        btn_agree_sign_id_set_act.setOnClickListener(this)
        btn_signup_birth.setOnClickListener(this)

    }


    //생일 다이어로그 생성
    private fun BirthDiarlogCreate(view: View){
        val pd: BirthDialogFragment<View> = BirthDialogFragment(view, "tag")
        pd.show(supportFragmentManager, "BirthDialog")
    }
    private fun downKeyboard(view: View) {
        val imm: InputMethodManager =
            applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    //성별 선택시 숫자로 반환
    private fun getSignUpGender() : Int{
        var gendertype = 0
        rg_sign_up_gender_act.setOnCheckedChangeListener{radioGroup, i ->
            when(i){
                R.id.rb_man_sign_up_act -> gendertype = 0
                R.id.rb_woman_sign_up_act -> gendertype = 1
            }
        }
        return gendertype
    }



    //network
    private fun getSignUpResponse() {

        val input_email: String = et_id_sign_up_act.text.toString()
        val input_pw: String = et_pw_sign_up_act.text.toString()
        val input_nickname: String = et_nickname_sign_up_act.text.toString()
        val input_phone: String = et_phonenumber_sign_up_act.text.toString()
        val input_gender: Int = getSignUpGender()


        var jsonObject = JSONObject()
        jsonObject.put("email", input_email)
        jsonObject.put("password", input_pw)
        jsonObject.put("nickname", input_nickname)
        jsonObject.put("phone_num", input_phone)
        jsonObject.put("gender", input_gender)


        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postSignUpResponse: Call<PostSignUpResponse> =
            networkService.postSignUpResponse("application/json", gsonObject)
        postSignUpResponse.enqueue(object : Callback<PostSignUpResponse> {
            override fun onFailure(call: Call<PostSignUpResponse>, t: Throwable) {
                Log.e("sign up fail", t.toString())
                toast(t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse(
                call: Call<PostSignUpResponse>,
                response: Response<PostSignUpResponse>
            ) {
                if (response.isSuccessful) {
                    toast(response.body()!!.message)
                    finish()
                }
            }
        })
    }

    }



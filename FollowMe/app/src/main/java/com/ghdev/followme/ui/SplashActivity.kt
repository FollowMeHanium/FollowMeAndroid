package com.ghdev.followme.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.LoginNetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {

    private val loginService: LoginNetworkService by lazy {
        ApplicationController.instance.loginService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isAutoLogingOrNot()
    }

    private fun isAutoLogingOrNot() {
        val id = sharedPrefs.getID(PreferenceHelper.USERID, "0")
        val pw = sharedPrefs.getPW(PreferenceHelper.USERPW, "0")

        if(id == "0" || pw == "0"){
            startActivity<LoginActivity>()
            finish()
        }else {
            getLoginResponse(id,pw)
        }
    }

     fun getLoginResponse(input_email: String, input_pw: String) {
        var isLoginRight = false
        val jsonObject = JSONObject()
        jsonObject.put("email", input_email)
        jsonObject.put("password", input_pw)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postLoginResponse: Call<PostLoginResponse> =
            loginService.postLoginResponse("application/json", gsonObject)
        postLoginResponse.enqueue(object : Callback<PostLoginResponse> {

            //통신 실패 시 수행되는 메소드
            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                Log.e("login_fun: ", t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse(
                call: Call<PostLoginResponse>,
                response: Response<PostLoginResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    sharedPrefs.setString(PreferenceHelper.PREFS_KEY_ACCESS, response.body()!!.token)
                    sharedPrefs.setString(PreferenceHelper.PREFS_KEY_REF, response.body()!!.refreshtoken)
                    sharedPrefs.setID(PreferenceHelper.USERID, input_email)
                    sharedPrefs.setPW(PreferenceHelper.USERPW, input_pw)

                    isLoginRight = true
                }

                if (isLoginRight) {
                    startActivity<MainActivity>()
                    finish()
                }
                else {
                    startActivity<LoginActivity>()
                    finish()
                }
            }
        })
    }
}

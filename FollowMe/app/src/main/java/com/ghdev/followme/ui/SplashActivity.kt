package com.ghdev.followme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ghdev.followme.R
import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //테스트용
        try{
            Thread.sleep(1500)
        }catch (e : InterruptedException){

        }
        startActivity<LoginActivity>()
        finish()
        //

        if(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0") !== "0"){
            getLoginResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_EMAIL, "0"),
                sharedPrefs.getString(PreferenceHelper.PREFS_KEY_PASSWORD, "0"))
        }
        else{
            try{
                Thread.sleep(1500)
            }catch (e : InterruptedException){

            }
            startActivity<LoginActivity>()
            finish()
        }
    }

    private fun getLoginResponse(input_email : String, input_pw : String){

        var jsonObject = JSONObject()
        jsonObject.put("email", input_email)
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
                        //token값 저장
                        sharedPrefs.setString(PreferenceHelper.PREFS_KEY_ACCESS, response.body()!!.token)
                        sharedPrefs.setString(PreferenceHelper.PREFS_KEY_REF, response.body()!!.refreshToken)

                        Log.d("SHARED_INFO_ALREADY", "access token" + sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"))

                        startActivity<MainActivity>()
                        finish()

                }
            }
        })

    }

}

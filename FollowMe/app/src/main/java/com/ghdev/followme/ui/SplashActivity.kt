package com.ghdev.followme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ghdev.followme.R
import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.LoginNetworkService
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

    private val loginService: LoginNetworkService by lazy {
        ApplicationController.instance.loginService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //디폴트 0값이면 로그인창으로 이동
        if(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_REF, "0").equals("0")) {
            Log.d("sharedPrefs_thread3: ",sharedPrefs.getString(PreferenceHelper.PREFS_KEY_REF, "0") )
            try{
                Thread.sleep(1500)
            }catch (e : InterruptedException){
                Log.d("sharedPrefs_thread2: ", e.message)
            }
            startActivity<LoginActivity>()
            finish()
        }
        //아니면 자동로그인
        else {
            Log.d("sharedPrefs_thread0: ",sharedPrefs.getString(PreferenceHelper.PREFS_KEY_REF, "0") )
            try {
                Thread.sleep(1500)
            } catch (e: InterruptedException) {
                Log.d("sharedPrefs_thread1: ", e.message)
            }
            startActivity<MainActivity>()
            finish()
        }
    }


}

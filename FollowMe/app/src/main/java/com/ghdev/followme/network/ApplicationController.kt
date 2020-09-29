package com.ghdev.followme.network

import android.app.Application
import com.ghdev.followme.db.PreferenceHelper
import com.kakao.auth.KakaoSDK
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationController : Application() {

    private val loginURL = "http://3.15.22.4:8008"
    private val baseURL = "http://3.15.22.4:8888"

    lateinit var networkService: NetworkService
    lateinit var loginService : LoginNetworkService
    lateinit var prefs : PreferenceHelper

    companion object {
        lateinit var instance: ApplicationController
    }

    override fun onCreate() {
        //preferenceHelper 초기화
        prefs = PreferenceHelper(applicationContext)
        super.onCreate()
        instance = this
        buildNetWork()
        LoginbuildNetWork()

        KakaoSDK.init(KakaoSDKAdapter())
    }

    //인증 방식 더 간단하게 사용하기 위해서 retrofit이랑 okhttp3 같이 사용
    private fun buildNetWork() {
        //val httpLoggingInterceptor = HttpLoggingInterceptor()
        //httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkService = retrofit.create(NetworkService::class.java)
    }

    private fun LoginbuildNetWork() {
        //val httpLoggingInterceptor = HttpLoggingInterceptor()
        //httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(loginURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        loginService = retrofit.create(LoginNetworkService::class.java)
    }

    //카카오로그인
    fun getGlobalApplicationContext() : ApplicationController{
        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
        return instance!!
    }
}
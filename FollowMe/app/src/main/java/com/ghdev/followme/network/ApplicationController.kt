package com.ghdev.followme.network

import android.app.Application
import com.ghdev.followme.db.PreferenceHelper
import com.kakao.auth.KakaoSDK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationController : Application() {

    private val baseURL = "http://3.15.22.4:8008"
    lateinit var networkService: NetworkService

    companion object {
        lateinit var instance: ApplicationController
        lateinit var prefs : PreferenceHelper
    }

    override fun onCreate() {
        //onCreate보다 먼저 초기화 해줘야 한다고 함!
        prefs = PreferenceHelper(applicationContext)


        super.onCreate()
        instance = this
        buildNetWork()


        KakaoSDK.init(KakaoSDKAdapter())
    }

    //인증 방식 더 간단하게 사용하기 위해서 retrofit이랑 okhttp3 같이 사용
    private fun buildNetWork() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkService = retrofit.create(NetworkService::class.java)
    }

    //카카오로그인
    fun getGlobalApplicationContext() : ApplicationController{
        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
        return instance!!
    }
}
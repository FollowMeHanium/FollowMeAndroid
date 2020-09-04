package com.ghdev.followme.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ghdev.followme.data.PostLoginResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Repository 패턴
//viewmodel이 요청하는 데이터를 room이나 retrofit으로부터 가져와 전달. -> viewmodel은 비즈니스 로직만 신경 씀 (어쨋든 이것도 분업인듯!)
class LoginUserRepo {

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    var jsonObject = JSONObject()

    fun postLoginResponse(email: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()

        jsonObject.put("email", email)
        jsonObject.put("password", password)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postLoginResponse: Call<PostLoginResponse> =
            networkService.postLoginResponse("application/json", gsonObject)

        postLoginResponse.enqueue(object : Callback<PostLoginResponse> {

            //통신 실패 시 수행되는 메소드
            override fun onFailure(call: Call<PostLoginResponse>, t: Throwable) {
                loginResponse.value = t.message
                Log.e("login fail", t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse( call: Call<PostLoginResponse>, response: Response<PostLoginResponse>) {
                if (response.isSuccessful) {
                    loginResponse.value = response.body()?.toString()
                }else{
                    loginResponse.value = response.errorBody()?.toString()
                }

            }
        })

        return loginResponse
    }
}
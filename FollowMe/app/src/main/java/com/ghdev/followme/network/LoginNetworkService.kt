package com.ghdev.followme.network

import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.data.PostNewTokenResponse
import com.ghdev.followme.data.PostSignUpResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface LoginNetworkService {


    //회원가입
    @POST("/auth/join")
    fun postSignUpResponse(
        @Header("Content-Type") content_type : String,
        @Body() body : JsonObject
    ) : Call<PostSignUpResponse>

    //회원가입 -> 닉네임 중복여부
    @POST("/auth/checkNickname")
    fun postSignUpCheckNameResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ) : Call<PostSignUpResponse>

    //회원가입 -> 아이디 중복여부
    @POST("/auth/checkEmail")
    fun postSignUpCheckIdResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ) : Call<PostSignUpResponse>

    //로그인
    @POST("/auth/login")
    fun postLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body : JsonObject
    ) : Call<PostLoginResponse>

    //토큰 검사 -> 토큰 사용시 매번 검사해야함
    @POST("/auth/getNewToken")
    fun postNewToken(
        @HeaderMap header: Map<String, String>
    ) : Call<PostNewTokenResponse>

}
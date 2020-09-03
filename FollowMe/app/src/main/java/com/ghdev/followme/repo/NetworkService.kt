package com.ghdev.followme.repo

import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.data.PostSignUpResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NetworkService {

    //회원가입
    @POST("/auth/join")
    fun postSignUpResponse(
        @Header("Content-Type") content_type : String,
        @Body() body : JsonObject
    ) : Call<PostSignUpResponse>

    //로그인
    @POST("/auth/login")
    fun postLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body : JsonObject
    ) :Call<PostLoginResponse>


    //shop


    //course
  /*  @GET("/course/list")
    fun getCommunitySmallNewPosts(

    ): Call<>*/
}
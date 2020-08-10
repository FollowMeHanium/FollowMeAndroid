package com.ghdev.followme.repo

import com.ghdev.followme.data.GetShopInfoResponse
import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.data.PostShopResponse
import com.ghdev.followme.data.PostSignUpResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

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
    
    //Shop 정보 1개
    @GET("/shop/one/:id")
    fun getShopInfoResponse(
        @Query("token") query : String
    ) : Call<GetShopInfoResponse>

    //Shop 찜하기
    @POST("/shop/dip")
    fun postShopDipResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ) : Call<PostShopResponse>

    //Shop 찜 취소
    @POST("/shop/undip")
    fun postShopUnDipResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ) : Call<PostShopResponse>



}
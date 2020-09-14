package com.ghdev.followme.network

import com.ghdev.followme.data.*
import com.ghdev.followme.network.get.GetAllCourseResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
    
    //Shop 목록
    @GET("/shop/list/:category&:tag")
    fun getShopListInfoResponse(
        @Header("authorization") authorization : String
    ) : Call<GetShopListInfoResponse>


    //Shop 정보 1개
    @GET("/shop/one/:id")
    fun getShopInfoResponse(
        @Header("authorization") authorization : String
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




    //GEt 모두의 코스
    @GET("/course/list")
    fun getAllOurCourse(
        @Header("authorization") authorization : String
    ): Call<GetAllCourseResponse>

    //GET 나의 코스
}
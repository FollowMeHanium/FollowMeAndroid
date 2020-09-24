package com.ghdev.followme.network


import com.ghdev.followme.data.*
import com.ghdev.followme.data.test.GetRecommendListInfo
import com.ghdev.followme.data.GetShopInfoResponse
import com.ghdev.followme.data.PostLoginResponse
import com.ghdev.followme.data.PostShopResponse
import com.ghdev.followme.data.PostSignUpResponse
import com.ghdev.followme.network.get.CourseDetailResponse
import com.ghdev.followme.network.get.GetAllCourseResponse
import com.ghdev.followme.network.get.ResponseMessageNonData
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




    //Shop과 Course 추천 목록
    @GET("/")
    fun getAllRecomendListInfoResponse(
        @Header("authorization") authorization: String
    ) : Call<GetRecommendListInfo>


    //Shop 검색 목록 (태그와 카테고리에 따라)
    @GET("/shop/list/:category&:tag")
    fun getShopListInfoResponse(
        @Header("authorization") authorization : String,
        @QueryMap filter : HashMap <String, String>
    ) : Call<GetShopListInfoResponse>


    //Shop 정보 1개
    @GET("/shop/one/:id")
    fun getShopInfoResponse(
        @Header("authorization") authorization : String,
        @Query("id") id : Int
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


    //Shop 좋아요 리스트
    @GET("/shop/like:id")
    fun getShopLikeListResponse(
        @Header("authorization") authorization: String,
        @Query("id") id: Int
    ) : Call<GetShopLikeListResponse>

    //GEt 모두의 코스
    @GET("/course/list")
    fun getAllOurCourse(
        @Header("authorization") authorization : String
    ): Call<GetAllCourseResponse>

    //GET 나의 코스
    @GET("/course/my")
    fun getMyCourse(
        @Header("authorization") authorization : String
    ): Call<GetAllCourseResponse>

    //코스 Detail
    @GET("/course/one")
    fun getCourseDetail(
        @Header("authorization") authorization : String,
        @Query("id") id : Int
    ): Call<CourseDetailResponse>

    //코스 추가하기
    @POST("/course")
    fun postCourseAdd(
        @Header("authorization") authorization : String,
        @Body() body : JsonObject
    ): Call<ResponseMessageNonData>


}
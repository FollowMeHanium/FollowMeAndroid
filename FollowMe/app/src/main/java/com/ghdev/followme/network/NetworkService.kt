package com.ghdev.followme.network


import com.ghdev.followme.data.*
import com.ghdev.followme.network.get.GetRecommendListInfo
import com.ghdev.followme.network.get.*

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.*

interface NetworkService {

  //Shop과 Course 추천 목록
    @GET("/recommend")
    fun getAllRecomendListInfoResponse(
        @Header("authorization") authorization: String
    ) : Call<GetRecommendListInfo>


    //Shop 검색 목록 (태그와 카테고리에 따라)
    @GET("/shop/list")
    fun getShopListInfoResponse(
        @Header("authorization") authorization : String,
        @QueryMap filter : HashMap <String, String>
    ) : Call<GetShopListInfoResponse>
    
    //Shop 정보 1개
    @GET("/shop/one")
    fun getShopInfoResponse(
        @Header("authorization") authorization : String,
        @Query("id") id : Int
    ) : Call<GetShopInfoResponse>

    //Shop 좋아요
    @POST("/shop/like")
    fun postShopLikeResponse(
        @Header("authorization") authorization: String,
        @Body() body: JsonObject
    ) : Call<PostCodeAndMessageResponse>

    //Shop 좋아요 취소
    @POST("/shop/dislike")
    fun postShopUnLikeResponse(
        @Header("authorization") authorization: String,
        @Body() body: JsonObject
    ) : Call<PostCodeAndMessageResponse>

    //Shop 좋아요 리스트
    @GET("/shop/like")
    fun getShopLikeListResponse(
        @Header("authorization") authorization: String
    ) : Call<GetShopLikeListResponse>

    //Shop Review 리스트
    @GET("/shop/review")
    fun getShopReviewListResponse(
        @Header("authorization") authorization: String,
        @Query("id") id : Int
    ) : Call<GetShopReviewListResponse>

    //Shop Review 남기기
    @POST("/shop/review")
    fun postShopReviewWriteResponse(
        @Header("authorization") authorization: String,
        @Body() body: JsonObject
    ) : Call<PostCodeAndMessageResponse>

    //GET 모두의 코스
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

    //코스 삭제하기
    //@DELETE("/course")
    @HTTP(method = "DELETE", path = "/course", hasBody = true)
    fun deleteCourse(
        @Header("authorization") authorization : String,
        @Body() body : JsonObject
    ): Call<ResponseMessageNonData>


    //검색
    @POST("/search")
    fun postSeach(
        @Header("authorization") authorization : String,
        @Body() body : JsonObject
    ): Call<SearchResultResponse>


}
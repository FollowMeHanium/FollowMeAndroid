package com.ghdev.followme.data

data class GetShopInfoResponse (
    val id : Int,
    val category : Int,
    val shopname : String,
    val address : String,
    val menu : String,
    val operating_time : String,
    val introduce : String,
    val grade_avg : Float,
    val latitude : Int,
    val longitude : Int,
    val reviewnum : Int,
    val likenum : Int
)
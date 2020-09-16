package com.ghdev.followme.data

data class GetShopInfoResponse (
    val id : Int,
    val category : String,
    val shopname : String,
    val address : String,
    val menu : String,
    val operating_time : String,
    val grade_avg : Int,
    val latitude : Int,
    val longitude : Int,
    val like : Int,
    val tag1 : Int,
    val tag2 : Int,
    val tag3 : Int
)
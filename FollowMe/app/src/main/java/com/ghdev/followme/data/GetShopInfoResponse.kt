package com.ghdev.followme.data

import java.sql.Time

data class GetShopInfoResponse (
    val id : Int,
    val category : Int,
    val shopname : String,
    val address : String,
    val menu : String,
    val operating_time : String,
    val grade_avg : Float,
    val latitude : Float,
    val longitude : Float,
    val like : Int,
    val tag1 : Int,
    val tag2 : Int,
    val tag3 : Int,
    val main_photo : String,
    val photo : List<String>
)
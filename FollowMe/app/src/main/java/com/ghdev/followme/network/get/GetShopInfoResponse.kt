package com.ghdev.followme.network.get

import java.sql.Time

//Shop정보 하나
data class GetShopInfoResponse (
    val id : Int,
    val category : Int,
    val shopname : String,
    val address : String,
    val menu : String,
    val operating_time : String,
    val grade_avg : Float,
    val latitude : Double,
    val longitude : Double,
    val like : Int,
    val thema1: Int,
    val thema2 : Int,
    val thema3 : Int,
    val main_photo : Int,
    val photos : List<String>
)
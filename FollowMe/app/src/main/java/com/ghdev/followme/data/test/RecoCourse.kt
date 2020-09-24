package com.ghdev.followme.data.test

import java.util.*

data class RecoCourse (
    val id : Int,
    val user_nickname : String,
    val title : String,
    val dday : Date,
    val grade_avg : Int,
    val main_photo : String,
    val created_at : String,
    val shops : List<RecoCourseShop>
)
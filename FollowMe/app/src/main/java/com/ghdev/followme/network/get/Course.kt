package com.ghdev.followme.network.get

data class Course(
    val grade_avg: Int,
    val id: Int,
    val user_nickname : String,
    val dday : String,
    val main_photo : String,
    val created_at : String,
    val share : Int,
    val shops: List<Shop>,
    val title: String
)
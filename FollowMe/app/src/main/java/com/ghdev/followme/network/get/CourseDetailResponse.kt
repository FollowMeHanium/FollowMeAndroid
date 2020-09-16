package com.ghdev.followme.network.get

data class CourseDetailResponse(
    val contents: String,
    val created_at: String,
    val dday: String,
    val grade_avg: Int,
    val id: Int,
    val like: Int,
    val shops: ArrayList<Shop>,
    val title: String,
    val user_nickname: String
)
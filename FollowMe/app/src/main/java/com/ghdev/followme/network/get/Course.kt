package com.ghdev.followme.network.get

data class Course(
    val grade_avg: Int,
    val id: Int,
    val shops: List<Shop>,
    val title: String
)
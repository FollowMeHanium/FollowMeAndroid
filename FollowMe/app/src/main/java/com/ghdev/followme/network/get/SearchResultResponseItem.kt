package com.ghdev.followme.network.get

data class SearchResultResponseItem(
    val address: String,
    val category: Int,
    val grade_avg: Double,
    val likenum: Int,
    val menu: String,
    val photo: String,
    val score: Double,
    val shopname: String
)
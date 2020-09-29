package com.ghdev.followme.data

data class PostLoginResponse (
    val token: String,
    val refreshtoken: String,
    val message: String
)
package com.ghdev.followme.data

data class PostLoginResponse (
    val token: String,
    val refreshToken: String,
    val message: String
)
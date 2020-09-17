package com.ghdev.followme.data

import com.ghdev.followme.data.test.SimpleShopInfo

data class GetShopLikeListResponse(
    val shopnum : Int,
    val shops : ArrayList<SimpleShopInfo>
)
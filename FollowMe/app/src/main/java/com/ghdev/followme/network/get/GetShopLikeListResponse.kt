package com.ghdev.followme.network.get

import com.ghdev.followme.network.get.Shop

data class GetShopLikeListResponse(
    var shopnum : Int,
    val shops : ArrayList<Shop>
)
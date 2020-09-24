package com.ghdev.followme.network.get

import com.ghdev.followme.network.get.Shop

data class GetShopListInfoResponse(
    val shopnum : Int,
    val shops : ArrayList<Shop>
)

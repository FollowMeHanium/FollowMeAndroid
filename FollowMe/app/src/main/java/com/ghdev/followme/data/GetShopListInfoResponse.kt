package com.ghdev.followme.data

import com.ghdev.followme.data.test.SimpleShopInfo

data class GetShopListInfoResponse(
    val shopnum : Int,
    val shops : ArrayList<SimpleShopInfo>
)

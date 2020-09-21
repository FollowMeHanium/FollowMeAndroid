package com.ghdev.followme.data.test

import com.ghdev.followme.network.get.Shop


data class GetRecommendListInfo (
    val shops : ArrayList<Shop>,
    val courses : ArrayList<RecoCourse>
)
package com.ghdev.followme.network.get

import com.ghdev.followme.network.get.Course
import com.ghdev.followme.network.get.Shop

data class GetRecommendListInfo (
    val hot : ArrayList<Shop>,
    val food : ArrayList<Shop>,
    val courses : ArrayList<Course>
)
package com.ghdev.followme.data.test

data class CourseData(
    var date: String,
    var star: Int,
    var place: ArrayList<Place>,
    var title: String,
    var background: Int
)

data class Place(
    var name : String
)
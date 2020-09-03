package com.ghdev.followme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.home.HomeFragment.Companion.PLACE_INFO
import kotlinx.android.synthetic.main.activity_place_detail.*

class PlaceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        val place_info = intent.getParcelableExtra<PlaceInfo>(PLACE_INFO)

        tv_place_detail_title.text = place_info.name
        tv_place_detail_name.text = place_info.address
        //Glide.with(this).load(place_info.img).into(iv_place_detail_main)
    }
}

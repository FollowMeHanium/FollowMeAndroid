package com.ghdev.followme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.test.ReviewInfo
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.mypage.MypageMypickActivity.Companion.PLACE_INFO
import kotlinx.android.synthetic.main.activity_place_detail.*

class PlaceDetailActivity : AppCompatActivity() {

    lateinit var placeReivewRecyclerViewAdapter: PlaceReivewRecyclerViewAdapter
    var reviewList : ArrayList<ReviewInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        //Recyclerview에서 받은 정보 표시
        val place_info = intent.putExtra("place_idx", -1)
        /*
        tv_place_detail_title.text = place_info.shopname
        tv_place_detail_name.text = place_info.address
        Glide.with(this).load(place_info.main_photo).into(iv_place_detail_main)*/

        PlaceReviewRcycler()
    }

    fun PlaceReviewRcycler(){
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))

        placeReivewRecyclerViewAdapter = PlaceReivewRecyclerViewAdapter(reviewList)
        rv_place_detail_review.adapter = placeReivewRecyclerViewAdapter
        rv_place_detail_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

}

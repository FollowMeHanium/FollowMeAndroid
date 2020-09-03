package com.ghdev.followme.ui.mycourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.test.CourseData
import com.ghdev.followme.data.test.Place
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.HotPlaceRecyclerViewAdapter
import com.ghdev.followme.ui.PlaceDetailActivity
import com.ghdev.followme.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_mycourse_detail.*
import kotlinx.android.synthetic.main.fragment_home.*

class MycourseDetailActivity : AppCompatActivity() {

    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_detail)

        setRecyclerView()
    }


    private fun setRecyclerView() {

        //핫플
        //데이터는 서버에서 받을 것
        //모듈화를 시키기(rv_id와 datalist가 들어가는 것 말고는 다른 것은 동일)
        var dataList: ArrayList<PlaceInfo> = ArrayList()

        dataList.add(PlaceInfo("", "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo("", "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo("", "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo("", "하", "서울시 노원구 공릉동 131313"))

        hotPlaceRecyclerViewAdapter =
            HotPlaceRecyclerViewAdapter(dataList) { PlaceInfo ->
                val intent = Intent(
                    this,
                    PlaceDetailActivity::class.java
                )
                intent.putExtra(
                    HomeFragment.PLACE_INFO,
                    PlaceInfo
                )
                startActivity(intent)
            }

        rv_store_list.adapter = hotPlaceRecyclerViewAdapter
        rv_store_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }
}

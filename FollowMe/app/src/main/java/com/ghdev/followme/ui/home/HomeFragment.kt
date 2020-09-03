package com.ghdev.followme.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.ghdev.followme.R
import com.ghdev.followme.data.test.CourseData
import com.ghdev.followme.data.test.Place
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.HotPlaceRecyclerViewAdapter
import com.ghdev.followme.ui.PlaceDetailActivity
import com.ghdev.followme.ui.mycourse.CourseRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home.*


/*create gahui*/

class HomeFragment : Fragment() {

    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter
    lateinit var courseRecyclerViewAdapter: CourseRecyclerViewAdapter

    companion object{
        val PLACE_INFO = "place_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
                    context,
                    PlaceDetailActivity::class.java
                )
                intent.putExtra(
                    PLACE_INFO,
                    PlaceInfo
                )
                startActivity(intent)
            }
        rv_hot_place_home.adapter = hotPlaceRecyclerViewAdapter
        rv_hot_place_home.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        //맛집
        var restaurantList : ArrayList<PlaceInfo> = ArrayList()

        restaurantList.add(PlaceInfo("dk", "맛집", "서울시 노원구 공릉동 131313"))
        restaurantList.add(PlaceInfo("dk", "맛집", "서울시 노원구 공릉동 131313"))
        restaurantList.add(PlaceInfo("dk", "맛집", "서울시 노원구 공릉동 131313"))
        restaurantList.add(PlaceInfo("dk", "맛집", "서울시 노원구 공릉동 131313"))

        hotPlaceRecyclerViewAdapter =
            HotPlaceRecyclerViewAdapter(restaurantList) { PlaceInfo ->
                val intent = Intent(
                    context,
                    PlaceDetailActivity::class.java
                )
                intent.putExtra(
                    PLACE_INFO,
                    PlaceInfo
                )
                startActivity(intent)
            }
        rv_restaurant_today.adapter = hotPlaceRecyclerViewAdapter
        rv_restaurant_today.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        //코스로 따라와
        var courseDataList : ArrayList<CourseData> = ArrayList()

        var place : ArrayList<Place>  = ArrayList()
        place.add(Place("갬성"))
        place.add(Place("소울커피"))
        place.add(Place("공차"))

        courseDataList.add(CourseData("2020.01.04", 5, place,"나만의 힙한 장소"))
        courseDataList.add(CourseData("2020.01.04", 3, place,"나만의 힙한 장소"))
        courseDataList.add(CourseData("2020.01.04", 2, place, "나만의 힙한 장소"))
        courseDataList.add(CourseData("2020.01.04", 1, place, "나만의 힙한 장소"))

        courseRecyclerViewAdapter =
            CourseRecyclerViewAdapter(requireActivity(), courseDataList)
        rv_follow_course.adapter = courseRecyclerViewAdapter
        rv_follow_course.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }

}

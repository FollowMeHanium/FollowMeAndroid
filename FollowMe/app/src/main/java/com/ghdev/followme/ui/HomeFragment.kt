package com.ghdev.followme.ui

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

        dataList.add(PlaceInfo(R.drawable.img5, "비트포비아", "서울특별시 강남구 역삼1동 824-30"))
        dataList.add(PlaceInfo(R.drawable.img6, "카페 프레도", "서울특별시 강남구 역삼1동"))
        dataList.add(PlaceInfo(R.drawable.img7, "꽃을피우고", "서울특별시 강남구 역삼동"))
        dataList.add(PlaceInfo(R.drawable.img8, "자세", "서울특별시 마포구 서교동"))

        hotPlaceRecyclerViewAdapter = HotPlaceRecyclerViewAdapter(dataList){PlaceInfo ->
            val intent = Intent(context, PlaceDetailActivity::class.java)
            intent.putExtra(PLACE_INFO, PlaceInfo)
            startActivity(intent)
        }
        rv_hot_place_home.adapter = hotPlaceRecyclerViewAdapter
        rv_hot_place_home.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        //맛집
        var restaurantList : ArrayList<PlaceInfo> = ArrayList()

        restaurantList.add(PlaceInfo(R.drawable.img1, "오우 연남점", "서울특별시 마포구 서교동"))
        restaurantList.add(PlaceInfo(R.drawable.img2, "돈부리", "서울특별시 마포구 서교동"))
        restaurantList.add(PlaceInfo(R.drawable.img3, "랍스타파티", "서울특별시 마포구 서교동 독막로7길"))
        restaurantList.add(PlaceInfo(R.drawable.img4, "라공방", "서울특별시 강남구 역삼동 825-20"))

        hotPlaceRecyclerViewAdapter = HotPlaceRecyclerViewAdapter(restaurantList){PlaceInfo->
            val intent = Intent(context, PlaceDetailActivity::class.java)
            intent.putExtra(PLACE_INFO, PlaceInfo)
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

        courseRecyclerViewAdapter = CourseRecyclerViewAdapter(courseDataList)
        rv_follow_course.adapter = courseRecyclerViewAdapter
        rv_follow_course.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }

}

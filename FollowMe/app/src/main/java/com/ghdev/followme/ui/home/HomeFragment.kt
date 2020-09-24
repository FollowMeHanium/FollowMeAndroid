package com.ghdev.followme.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.test.CourseData
import com.ghdev.followme.data.test.GetRecommendListInfo
import com.ghdev.followme.data.test.Place
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.PlaceDetailActivity
import com.ghdev.followme.ui.mycourse.CourseRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*create gahui*/

class HomeFragment : Fragment() {

    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter
    lateinit var courseRecyclerViewAdapter: CourseRecyclerViewAdapter

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
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
        getRecomendInfo()
    }

    private fun setRecyclerView() {

        //핫플
        //데이터는 서버에서 받을 것
        //모듈화를 시키기(rv_id와 datalist가 들어가는 것 말고는 다른 것은 동일)
        var dataList: ArrayList<Shop> = ArrayList()
/*
        dataList.add(PlaceInfo(R.drawable.img5, "비트포비아", "서울특별시 강남구 역삼1동 824-30"))
        dataList.add(PlaceInfo(R.drawable.img6, "카페 프레도", "서울특별시 강남구 역삼1동"))
        dataList.add(PlaceInfo(R.drawable.img7, "꽃을피우고", "서울특별시 강남구 역삼동"))
        dataList.add(PlaceInfo(R.drawable.img8, "자세", "서울특별시 마포구 서교동"))*/

        hotPlaceRecyclerViewAdapter = HotPlaceRecyclerViewAdapter( requireActivity(), dataList)
        rv_hot_place_home.adapter = hotPlaceRecyclerViewAdapter
        rv_hot_place_home.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        //맛집
        var restaurantList : ArrayList<Shop> = ArrayList()
/*

        restaurantList.add(PlaceInfo(R.drawable.img1, "오우 연남점", "서울특별시 마포구 서교동"))
        restaurantList.add(PlaceInfo(R.drawable.img2, "돈부리", "서울특별시 마포구 서교동"))
        restaurantList.add(PlaceInfo(R.drawable.img3, "랍스타파티", "서울특별시 마포구 서교동 독막로7길"))
        restaurantList.add(PlaceInfo(R.drawable.img4, "라공방", "서울특별시 강남구 역삼동 825-20"))
*/

        hotPlaceRecyclerViewAdapter =
            HotPlaceRecyclerViewAdapter(requireActivity(), restaurantList)
        rv_restaurant_today.adapter = hotPlaceRecyclerViewAdapter
        rv_restaurant_today.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        //코스로 따라와
/*        var courseDataList : ArrayList<CourseData> = ArrayList()

        var place : ArrayList<Place>  = ArrayList()
        place.add(Place("갬성"))
        place.add(Place("소울커피"))
        place.add(Place("공차"))

        courseDataList.add(CourseData("2020.01.04", 5, place,"나만의 힙한 장소", R.drawable.img1))
        courseDataList.add(CourseData("2020.04.03", 3, place,"나만의 데이트 장소", R.drawable.img3))
        courseDataList.add(CourseData("2020.04.26", 2, place, "힐링하기 좋은날", R.drawable.img2))
        courseDataList.add(CourseData("2020.03.02", 1, place, "친구와 함께한 날", R.drawable.img8))

        courseRecyclerViewAdapter =
            CourseRecyclerViewAdapter(requireActivity(), courseDataList)
        rv_follow_course.adapter = courseRecyclerViewAdapter
        rv_follow_course.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)*/

    }

    private fun getRecomendInfo(){
        val getReco : Call<GetRecommendListInfo> = networkService.getAllRecomendListInfoResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"))
        //val getReco : Call<GetRecommendListInfo> = networkService.getAllRecomendListInfoResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIsIm5pY2tuYW1lIjoiYWFhYSIsImdlbmRlciI6MSwiYWdlIjoyNSwic3RhdHVzIjoxLCJpYXQiOjE2MDA3NjM5NTUsImV4cCI6MTYwMDc2NzU1NSwiaXNzIjoiY29tZU9uIn0.qmQWJPjyKqTNeGHj6oBP5L3MF1s4CF9Ue7wLDXU_4CE")

        getReco.enqueue(object: Callback<GetRecommendListInfo> {
            override fun onFailure(call: Call<GetRecommendListInfo>, t: Throwable) {
                Log.d("getReco", "실패 " + t.message)

            }

            override fun onResponse(
                call: Call<GetRecommendListInfo>,
                response: Response<GetRecommendListInfo>
            ) {
                if(response.isSuccessful){
                    Log.d("getReco", "성공")
                    val temp = response.body()!!.shops

                    if(temp.size >0){

                        val position = hotPlaceRecyclerViewAdapter.itemCount
                        hotPlaceRecyclerViewAdapter.dataList.addAll(temp)
                        hotPlaceRecyclerViewAdapter.notifyItemInserted(position)
                    }
                }
            }

        })
    }

}

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
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.Course
import com.ghdev.followme.network.get.GetRecommendListInfo
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.mycourse.CourseRecyclerViewAdapter
import com.ghdev.followme.ui.search.SearchActivity
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

    // 검색 버튼 구현
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_course_frag.setOnClickListener {
            //FirebaseAuth.getInstance().signOut()
            val intent = Intent (getActivity(), SearchActivity::class.java)
            getActivity()?.startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getRecomendInfo()
    }

    private fun hotPlaceRV() {
        //핫플
        val dataList: ArrayList<Shop> = ArrayList()
        hotPlaceRecyclerViewAdapter = HotPlaceRecyclerViewAdapter( requireActivity(), dataList)
        rv_hot_place_home.adapter = hotPlaceRecyclerViewAdapter
        rv_hot_place_home.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun foodPlaceRV() {

        //맛집
        val restaurantList : ArrayList<Shop> = ArrayList()
        hotPlaceRecyclerViewAdapter = HotPlaceRecyclerViewAdapter(requireActivity(), restaurantList)
        rv_restaurant_today.adapter = hotPlaceRecyclerViewAdapter
        rv_restaurant_today.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun courseRV() {

        //코스로 따라와
       var courseDataList : ArrayList<Course> = ArrayList()
        courseRecyclerViewAdapter = CourseRecyclerViewAdapter(requireActivity(), courseDataList)
        rv_follow_course.adapter = courseRecyclerViewAdapter
        rv_follow_course.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun getRecomendInfo(){
        val getReco : Call<GetRecommendListInfo> = networkService.getAllRecomendListInfoResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"))

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
                    val temp = response.body()!!.hot

                    if(temp.isNullOrEmpty()) {
                        Log.d("getReco", "null값")
                    }
                    else{
                        val hot = response.body()!!.hot
                        val courses = response.body()!!.courses
                        val food = response.body()!!.food

                        if(hot.size > 0){
                            hotPlaceRV()
                            val position = hotPlaceRecyclerViewAdapter.itemCount
                            hotPlaceRecyclerViewAdapter.dataList.addAll(hot)
                            hotPlaceRecyclerViewAdapter.notifyItemChanged(position)

                        }
                        if(courses.size > 0) {
                            courseRV()
                            val position = courseRecyclerViewAdapter.itemCount
                            courseRecyclerViewAdapter.dataList.addAll(courses)
                            courseRecyclerViewAdapter.notifyItemChanged(position)
                        }
                        if(food.size > 0) {
                            foodPlaceRV()
                            val position = hotPlaceRecyclerViewAdapter.itemCount
                            hotPlaceRecyclerViewAdapter.dataList.addAll(food)
                            hotPlaceRecyclerViewAdapter.notifyItemChanged(position)
                        }
                    }
            }
        }
    })
}
}

package com.ghdev.followme.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter

    companion object{
        val PLACE_INFO = "place_info"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView() {

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
        rv_search_1.adapter = hotPlaceRecyclerViewAdapter
        rv_search_1.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


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
        rv_search_2.adapter = hotPlaceRecyclerViewAdapter
        rv_search_2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

    }



}
package com.ghdev.followme.ui.coursereco

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.test.CourseData
import com.ghdev.followme.data.test.Place
import com.ghdev.followme.ui.mycourse.CourseRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_course_recommend.*
import kotlin.collections.ArrayList

class CourseRecommendFragment : Fragment() {

    lateinit var courseRecyclerViewAdapter: CourseRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_recommend, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setRecyclerView()
    }


    private fun setRecyclerView() {
        //코스
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
        rv_course_reco.adapter = courseRecyclerViewAdapter
        rv_course_reco.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }


}

package com.ghdev.followme.ui.coursereco

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.Course
import com.ghdev.followme.network.get.GetAllCourseResponse
import com.ghdev.followme.ui.mycourse.CourseRecyclerViewAdapter
import com.ghdev.followme.util.CourseDialogFragment
import kotlinx.android.synthetic.main.fragment_course_recommend.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlin.collections.ArrayList

class CourseRecommendFragment : Fragment() {

    lateinit var courseRecyclerViewAdapter: CourseRecyclerViewAdapter

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_course_recommend, container, false)

        val btn_select_categroy = view.findViewById(R.id.cl_category_filter) as ConstraintLayout

        btn_select_categroy.setOnClickListener{
            val dialog: CourseDialogFragment = CourseDialogFragment()
                .getInstance()
            //val fm = supportFragmentManager.beginTransaction()
            val fm = getFragmentManager()
            dialog.show(fm!!, "TAG_DIALOG_EVENT")

        }
        // Inflate the layout for this fragment
        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setRecyclerView()
        getMyCourseResponse()
    }

    private fun setRecyclerView() {
            //코스
//        var place : ArrayList<Place>  = ArrayList()
//        place.add(Place("갬성"))
//        place.add(Place("소울커피"))
//        place.add(Place("공차"))
//
//
//        courseDataList.add(CourseData("2020.01.04", 5, place,"나만의 힙한 장소", R.drawable.img1))
//        courseDataList.add(CourseData("2020.04.03", 3, place,"나만의 데이트 장소", R.drawable.img3))
//        courseDataList.add(CourseData("2020.04.26", 2, place, "힐링하기 좋은날", R.drawable.img2))
//        courseDataList.add(CourseData("2020.03.02", 1, place, "친구와 함께한 날", R.drawable.img8))

        var courseDataList : ArrayList<Course> = ArrayList()

        courseRecyclerViewAdapter = CourseRecyclerViewAdapter(requireActivity(), courseDataList)
        rv_course_reco.adapter = courseRecyclerViewAdapter
        rv_course_reco.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }

    private fun getMyCourseResponse() {

        val getOurCorse: Call<GetAllCourseResponse> = networkService.getAllOurCourse(PreferenceHelper.PREFS_KEY_ACCESS)

        Log.d("TAGG", "안들어가니?" )
        getOurCorse.enqueue(object : Callback<GetAllCourseResponse> {

            override fun onFailure(call: Call<GetAllCourseResponse>, t: Throwable) {
                Log.d("모두의 Course fail", t.toString())
            }

            override fun onResponse(
                call: Call<GetAllCourseResponse>,
                response: Response<GetAllCourseResponse>
            ) {
                Log.d("TAGG 22", response.isSuccessful.toString() )
                if (response.isSuccessful) {

                    val temp: ArrayList<Course> = response.body()!!.courses

                    Log.d("TAGG 33", temp.toString() )

                    if (temp.size > 0) {
                        val position = courseRecyclerViewAdapter.itemCount
                        courseRecyclerViewAdapter.dataList.addAll(temp)
                        courseRecyclerViewAdapter.notifyItemInserted(position)
                    }else {

                    }
                }
            }
        })
    }
}

package com.ghdev.followme.ui.coursereco

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.Course
import com.ghdev.followme.network.get.GetAllCourseResponse
import com.ghdev.followme.ui.mycourse.CourseRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_course_recommend.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseRecommendFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_course_recommend, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getRecommendCourseResponse()
    }

    private fun setRecyclerView() {
        var courseDataList : ArrayList<Course> = ArrayList()
        courseRecyclerViewAdapter = CourseRecyclerViewAdapter(requireActivity(), courseDataList)
        rv_course_reco.adapter = courseRecyclerViewAdapter
        rv_course_reco.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun getRecommendCourseResponse() {
        val getOurCorse: Call<GetAllCourseResponse> = networkService.getAllOurCourse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, ""))

        getOurCorse.enqueue(object : Callback<GetAllCourseResponse> {
            override fun onFailure(call: Call<GetAllCourseResponse>, t: Throwable) {
                Log.d("모두의 Course fail", t.toString())
            }

            override fun onResponse(
                call: Call<GetAllCourseResponse>,
                response: Response<GetAllCourseResponse>
            ) {
                if (response.isSuccessful) {

                    val temp: ArrayList<Course> = response.body()!!.courses

                    if (temp.size > 0) {
                        setRecyclerView()
                        val position = courseRecyclerViewAdapter.itemCount
                        courseRecyclerViewAdapter.dataList.addAll(temp)
                        courseRecyclerViewAdapter.notifyItemChanged(position)
                    }
                    else {

                    }
                }
            }
        })
    }
}

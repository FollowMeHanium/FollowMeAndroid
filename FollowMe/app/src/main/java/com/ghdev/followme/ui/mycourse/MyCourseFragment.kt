package com.ghdev.followme.ui.mycourse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.Course
import com.ghdev.followme.network.get.GetAllCourseResponse
import org.jetbrains.anko.startActivity
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendarView
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_my_course.*
import kotlinx.android.synthetic.main.fragment_my_course.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MyCourseFragment : Fragment() {

    lateinit var courseRecyclerViewAdapter: CourseRecyclerViewAdapter
    lateinit var rootView : View

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_my_course, container, false)

        rootView.btn_add_course_frag.setOnClickListener {
            var intent = Intent(activity, MycourseAddActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        getMyCourseResponse()
        setRecyclerView()
        setCalendar()

    }

    private fun setCalendar() {

        //custom Calendar
        //https://github.com/Mulham-Raee/Horizontal-Calendar

        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        tv_ymd_course_frag.text = Calendar.YEAR.toString()+ "년 " + Calendar.MONTH.toString() + "월"

        var  horizontalCalendar = HorizontalCalendar.Builder(rootView, R.id.cv_calendar_my_course_frag)
            .range(startDate, endDate)
            .datesNumberOnScreen(7)
            /*.configure()
                .showTopText(false)
            .end()*/
            .build()



        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {

            }

            override fun onCalendarScroll(calendarView: HorizontalCalendarView, dx: Int, dy: Int) {

            }

            override fun onDateLongClicked(date: Calendar?, position: Int): Boolean {
                return true
            }
        }

    }

    private fun setRecyclerView() {
        var courseDataList : ArrayList<Course> = ArrayList()
        courseRecyclerViewAdapter = CourseRecyclerViewAdapter(requireActivity(), courseDataList)
        rv_my_love_course.adapter = courseRecyclerViewAdapter
        rv_my_love_course.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun getMyCourseResponse() {
        //## token 자리에 SharedPreference 에 있는 token 값 가져와야함.
        val getOurCorse: Call<GetAllCourseResponse> = networkService.getMyCourse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoyLCJuaWNrbmFtZSI6InVzZXIxIiwiZ2VuZGVyIjoxLCJhZ2UiOjIwMjAsInN0YXR1cyI6MSwiaWF0IjoxNjAwOTE4NzU1LCJleHAiOjE2MDEwMDUxNTUsImlzcyI6ImNvbWVPbiJ9.f-m4QiX0OXm1nvJDxXvajr0AL0y480Y4EFVGcvttRAY")

       // Log.d("TAGG", "안들어가니?" )
        getOurCorse.enqueue(object : Callback<GetAllCourseResponse> {
            override fun onFailure(call: Call<GetAllCourseResponse>, t: Throwable) {
                Log.d("my course GET fail", t.toString())
            }

            override fun onResponse(
                call: Call<GetAllCourseResponse>,
                response: Response<GetAllCourseResponse>
            ) {
                //Log.d("TAGG 22", response.isSuccessful.toString() )
                if (response.isSuccessful) {

                    val temp: ArrayList<Course> = response.body()!!.courses
                    if (temp.size > 0) {

                        val position = courseRecyclerViewAdapter.itemCount
                        courseRecyclerViewAdapter.dataList.addAll(temp)
                        courseRecyclerViewAdapter.notifyItemInserted(position)
                    }
                }
            }
        })
    }
}
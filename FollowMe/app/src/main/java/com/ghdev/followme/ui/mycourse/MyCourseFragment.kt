package com.ghdev.followme.ui.mycourse

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
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendarView
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_my_course.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MyCourseFragment : Fragment() {

    lateinit var courseRecyclerViewAdapter: CourseRecyclerViewAdapter
    private val viewModel : MyCourseViewModel by viewModels()
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

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        setRecyclerView()
        getMyCourseResponse()
        setCalendar()

    }

    private fun setCalendar() {

        //custom Calendar
        //https://github.com/Mulham-Raee/Horizontal-Calendar

        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

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
//        //코스
//
//
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

        courseRecyclerViewAdapter =
        CourseRecyclerViewAdapter(requireActivity(), courseDataList)
        rv_my_love_course.adapter = courseRecyclerViewAdapter
        rv_my_love_course.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }


    private fun getMyCourseResponse() {


        //## token 자리에 SharedPreference 에 있는 token 값 가져와야함.
        val getOurCorse: Call<GetAllCourseResponse> = networkService.getAllOurCourse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.88j2Z3_pB_z-xU4AGuYsptIiV9zFdH7bsweI8hR3NS8")

        Log.d("TAGG", "안들어가니?" )
        getOurCorse.enqueue(object : Callback<GetAllCourseResponse> {

            override fun onFailure(call: Call<GetAllCourseResponse>, t: Throwable) {
                Log.d("course 가져오기 fail", t.toString())
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
                    }
                }
            }
        })
    }


}
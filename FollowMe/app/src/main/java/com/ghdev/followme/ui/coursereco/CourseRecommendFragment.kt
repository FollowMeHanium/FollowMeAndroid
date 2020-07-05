package com.ghdev.followme.ui.coursereco

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.test.CourseData
import com.ghdev.followme.data.test.Place
import com.ghdev.followme.ui.CourseDialogFragment
import com.ghdev.followme.ui.CourseRecyclerViewAdapter
import kotlinx.android.synthetic.main.dialog_course_category.*
import kotlinx.android.synthetic.main.dialog_course_category.view.*
import kotlinx.android.synthetic.main.fragment_course_recommend.*
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.windowManager
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

        val view : View = inflater.inflate(R.layout.fragment_course_recommend, container, false)

        val btn_select_categroy = view.findViewById(R.id.cl_category_filter) as ConstraintLayout

        btn_select_categroy.setOnClickListener{
            val dialog: CourseDialogFragment = CourseDialogFragment().getInstance()
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


    }


    private fun setRecyclerView() {
        //코스
        var courseDataList : ArrayList<CourseData> = ArrayList()

        var place : ArrayList<Place>  = ArrayList()
        place.add(Place("갬성"))
        place.add(Place("소울커피"))
        place.add(Place("공차"))

        courseDataList.add(CourseData("2020.01.04", 5, place,"나만의 힙한 장소", R.drawable.img1))
        courseDataList.add(CourseData("2020.04.03", 3, place,"나만의 데이트 장소", R.drawable.img3))
        courseDataList.add(CourseData("2020.04.26", 2, place, "힐링하기 좋은날", R.drawable.img2))
        courseDataList.add(CourseData("2020.03.02", 1, place, "친구와 함께한 날", R.drawable.img8))

        courseRecyclerViewAdapter = CourseRecyclerViewAdapter(courseDataList)
        rv_course_reco.adapter = courseRecyclerViewAdapter
        rv_course_reco.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    }


}

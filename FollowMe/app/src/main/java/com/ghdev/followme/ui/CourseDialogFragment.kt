package com.ghdev.followme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ghdev.followme.R
import kotlinx.android.synthetic.main.dialog_course_category.*
import kotlinx.android.synthetic.main.dialog_course_category.view.*

class CourseDialogFragment : DialogFragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.dialog_course_category, container, false)

        //코스 카테고리 항목
        var contents_lovers = view.btn_course_category_lovers
        var contents_friends = view.btn_course_category_friends
        var contents_family = view.btn_course_category_family
        var contents_childs = view.btn_course_category_child
        var contents_act = view.btn_course_category_act
        var contents_alone = view.btn_course_category_alone
        var contents_pet = view.btn_course_category_pet
        var contents_tv = view.btn_course_category_tv
        var contents_anni = view.btn_course_category_anni
        var contents_ex = view.btn_course_category_ex
        var contents_rain = view.btn_course_category_rain
        var contents_out = view.btn_course_category_out

        //닫기 버튼
        var btn_close = view.btn_course_dialog_close

        btn_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                dismiss()
            }
        })

        return view

    }

    fun getInstance():CourseDialogFragment {
        return CourseDialogFragment()
    }
}
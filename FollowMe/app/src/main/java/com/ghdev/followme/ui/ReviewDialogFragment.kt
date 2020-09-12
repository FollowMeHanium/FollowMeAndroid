package com.ghdev.followme.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.ghdev.followme.R
import kotlinx.android.synthetic.main.dialog_course_category.view.*
import kotlinx.android.synthetic.main.dialog_review_insert.view.*

class ReviewDialogFragment : DialogFragment(){

    override fun onResume() {
        var width = resources.getDimensionPixelSize(R.dimen.review_dialog_width)
        var height = resources.getDimensionPixelSize(R.dimen.review_dialog_height)
        dialog?.window?.setLayout(width, height)
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.dialog_review_insert, container, false)

        //코스 카테고리 항목
        var et_review = view.et_dialog_review
        var rb_review = view.rb_dialog_review


        //닫기 버튼
        var btn_close = view.btn_dialog_review_cancle

        btn_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                dismiss()
            }
        })

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view

    }

    fun getInstance():ReviewDialogFragment {
        return ReviewDialogFragment()
    }

}
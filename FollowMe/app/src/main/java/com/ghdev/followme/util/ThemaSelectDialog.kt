package com.ghdev.followme.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.ghdev.followme.R
import kotlinx.android.synthetic.main.dialog_course_category.*


class ThemaSelectDialog (context: Context,
                         private val mLoversClickListener : View.OnClickListener?,
                         private val mFriendClickListener : View.OnClickListener?,
                         private val mTVClickListener : View.OnClickListener?,
                         private val mActivityClickListener : View.OnClickListener?,
                         private val mPetClickListener : View.OnClickListener?,
                         private val mFamilyClickListener : View.OnClickListener?,
                         private val mCloseClickListener : View.OnClickListener?) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {

    private val clickedState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.55f
        window!!.attributes = lpWindow

        setContentView(R.layout.dialog_course_category)

        //연인
        if(mLoversClickListener != null) {
            btn_course_category_lovers.setOnClickListener(mLoversClickListener)
        }

        //친구
        if(mFriendClickListener != null) {
            btn_course_category_friends.setOnClickListener(mFriendClickListener)
        }

        //가족
        if(mFamilyClickListener != null) {
            btn_course_category_family.setOnClickListener(mFamilyClickListener)
        }

        //활동
        if(mActivityClickListener != null) {
            btn_course_category_act.setOnClickListener(mActivityClickListener)
        }

        //강아지
        if(mPetClickListener != null) {
            btn_course_category_pet.setOnClickListener(mPetClickListener)
        }

        //TV
        if(mTVClickListener != null) {
            btn_course_category_tv.setOnClickListener(mTVClickListener)
        }

        //닫기버튼
        if(mCloseClickListener != null) {
            btn_course_dialog_close.setOnClickListener(mCloseClickListener)
        }
    }
}


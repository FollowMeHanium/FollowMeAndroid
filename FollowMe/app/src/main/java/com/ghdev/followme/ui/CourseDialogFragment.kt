package com.ghdev.followme.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.ghdev.followme.R
import kotlinx.android.synthetic.main.dialog_course_category.*
import kotlinx.android.synthetic.main.dialog_course_category.view.*

class CourseDialogFragment : DialogFragment(), View.OnClickListener{

    private lateinit var contents_lovers : Button
    private lateinit var contents_friends : Button
    private lateinit var contents_family : Button
    private lateinit var contents_act : Button
    private lateinit var contents_pet : Button
    private lateinit var contents_tv : Button
    private lateinit var btn_close : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        var width = resources.getDimensionPixelSize(R.dimen.course_dialog_width)
        var height = resources.getDimensionPixelSize(R.dimen.course_dialog_height)
        dialog?.window?.setLayout(width, height)
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.dialog_course_category, container, false)

        init(view);

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }

    fun getInstance():CourseDialogFragment {
        return CourseDialogFragment()
    }

    private fun init(view : View) {
        //코스 카테고리 항목
        contents_lovers = view.btn_course_category_lovers
        contents_friends = view.btn_course_category_friends
        contents_family = view.btn_course_category_family
        contents_act = view.btn_course_category_act
        contents_pet = view.btn_course_category_pet
        contents_tv = view.btn_course_category_tv
        btn_close = view.btn_course_dialog_close
    }

    override fun onClick(v: View?) {
        when (v!!) {
            //0
            contents_lovers -> {

            }
            //1
            contents_friends -> {

            }
            //2
            contents_tv -> {

            }
            //3
            contents_act-> {

            }
            //4
            contents_pet -> {

            }
            //5
            contents_family -> {

            }
            btn_close ->  {
                dismiss()
            }
        }
    }
}
package com.ghdev.followme.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.ghdev.followme.R
import kotlinx.android.synthetic.main.dialog_course_category.view.*

class CourseDialogFragment : DialogFragment(){

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

        //코스 카테고리 항목
        var contents_lovers = view.btn_course_category_lovers
        var contents_friends = view.btn_course_category_friends
        var contents_family = view.btn_course_category_family
        var contents_act = view.btn_course_category_act
        var contents_pet = view.btn_course_category_pet
        var contents_tv = view.btn_course_category_tv

        //닫기 버튼
        var btn_close = view.btn_course_dialog_close

        btn_close.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?){
                dismiss()
            }
        })

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view

    }

    fun getInstance():CourseDialogFragment {
        return CourseDialogFragment()
    }
}






/*
package com.ghdev.followme.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.OnShowListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.ghdev.followme.R
import kotlinx.android.synthetic.main.dialog_course_category.view.*

class CourseDialogFragment : DialogFragment(){

    lateinit var lists : MutableList<Button>

    private lateinit var contents_lovers : Button
    private lateinit var contents_friends : Button
    private lateinit var contents_family : Button
    private lateinit var contents_act : Button
    private lateinit var contents_pet : Button
    private lateinit var contents_tv : Button
    private lateinit var btn_close : Button

    private var mListenerItemClick: OnItemClickListener? = null
    private var mAlertDialog: AlertDialog? = null

    interface OnItemClickListener {
        fun onDialogItemClick(tag: String?, dialog: Dialog?, title: String?, which: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments

        val builder = AlertDialog.Builder(activity)

        // listener check
        if (targetFragment != null) {
            setListener(targetFragment)
        } else if (activity != null) {
            setListener(activity)
        }

        // dialog string list
        if (lists.size > 0) {
            builder.setItems(lists.toTypedArray()) { dialog, which ->
                if (mListenerItemClick != null) {
                    mListenerItemClick.onDialogItemClick(tag, mAlertDialog, lists[which], which)
                }
            }
        }

        // make dialog
        mAlertDialog = builder.create()

        // show listener
        if (mListenerShow != null) {
            mAlertDialog.setOnShowListener(OnShowListener {
                mListenerShow.onDialogShow(
                    tag,
                    mAlertDialog
                )
            })
        }

        return mAlertDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.dialog_course_category, container, false)
        init(view);

        val builder = AlertDialog.Builder(activity)

        if (lists.size > 0) {
            builder.setItems(lists.toTypedArray()) { dialog, which ->
                if (mListenerItemClick != null) {
                    mListenerItemClick.onDialogItemClick(tag, mAlertDialog, lists[which].text.toString(), which)
                }
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return view
    }

    fun getInstance(): CourseDialogFragment {
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

        lists.add(contents_lovers)
        lists.add(contents_friends)
        lists.add(contents_family)
        lists.add(contents_act)
        lists.add(contents_pet)
        lists.add(contents_tv)
        lists.add(btn_close)
    }

    private fun setListener(target: OnItemClickListener) {
        mListenerItemClick =  target
    }

}*/

package com.ghdev.followme.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.ghdev.followme.R
import java.util.*

class BirthDialogFragment<Button : View?>(v : View, tag:String): DialogFragment() {
    private var listener: DatePickerDialog.OnDateSetListener? = null
    private final val MAX_YEAR = 2099
    private final val MIN_YEAR = 1980
    val v = v
    val tagname:String = tag
    var cal = Calendar.getInstance()

    fun setListener(listener: DatePickerDialog.OnDateSetListener?){
        this.listener = listener
    }

    var btn_done : Button? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val dialog:View = inflater.inflate(R.layout.dialog_sign_up_birth, null).also{
            btn_done = it.findViewById<Button>(R.id.btn_dialog_sign_up_done)
        }

        val yearPicker = dialog.findViewById<View>(R.id.dialog_sign_up_pick_year) as NumberPicker
        val monthPicker = dialog.findViewById<View>(R.id.dialog_sign_up_pick_month) as NumberPicker
        val dayPicker = dialog.findViewById<View>(R.id.dialog_sign_up_pick_day) as NumberPicker

        btn_done?.setOnClickListener(View.OnClickListener {
            //tv바꾸기
            listener?.onDateSet(null, yearPicker.value, monthPicker.value, dayPicker.value)
            dismiss()
        })

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = year

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = month

        dayPicker.minValue = 1
        dayPicker.maxValue = 31
        dayPicker.value = day

        builder.setView(dialog)


        return builder.create()
    }

    override fun onResume() {
        var width = resources.getDimensionPixelSize(R.dimen.sign_up_dialog_width)
        var height = resources.getDimensionPixelSize(R.dimen.sign_up_dialog_height)
        dialog?.window?.setLayout(width, height)
        super.onResume()
    }


}
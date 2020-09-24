package com.ghdev.followme.ui.mycourse

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ghdev.followme.R
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.ResponseMessageNonData
import com.ghdev.followme.ui.CourseDialogFragment
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_mycourse_add.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Boolean

class MycourseAddActivity : AppCompatActivity(), View.OnClickListener {

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_add)

        init()
    }

    private fun init() {

    }

    override fun onClick(v: View?) {

        when (v!!) {
            btn_close_course_add -> {
                finish()
            }

            //테마
            img_icon_select_btn -> {
                val dialog: CourseDialogFragment = CourseDialogFragment().getInstance()
                val fm = supportFragmentManager.beginTransaction()
                dialog.show(fm!!, "TAG_DIALOG_EVENT")
                dialog.id
                Log.v("LOGGG", dialog.id.toString())

            }

            //작성완료 체크버튼 눌렀을 때
            img_btn_add_my_course -> {

                //edtiText에 있는 것 모두 체크해줘야함 ( ㅎㅎ )

               /* if (et_title_commu_write_act.text.length == 0)
                    titleInputDialog.show()
                else if (et_context_commu_write_act.text.length == 0)
                    contextInputDialog.show()
                else
                {
                    postCourseAdd()
                }*/
            }

            //키보드 다운 함수
            cl_mycourse_add_layout -> {
                downKeyboard(cl_mycourse_add_layout)
            }

            //et로 focus가도록
            /*btn_et_focus_commu_write_act -> {

                if (et_context_commu_write_act.requestFocus() == Boolean.TRUE)
                    downKeyboard(btn_et_focus_commu_write_act)
                else {
                    et_context_commu_write_act.requestFocus()
                    //키보드 업
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
                }
            }*/
        }
    }

    //키보드 다운
    private fun downKeyboard(view: View) {
        val imm: InputMethodManager =
            applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun postCourseAdd(){
        val title: String = et_add_title.text.toString()
        val thema: Int = -1
        val dday : String = et_add_date.text.toString()


        var jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("thema", thema)
        jsonObject.put("dday", dday)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postCourseAddResponse: Call<ResponseMessageNonData> =
            networkService.postCourseAdd("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoyLCJuaWNrbmFtZSI6InVzZXIxIiwiZ2VuZGVyIjoxLCJhZ2UiOjIwMjAsInN0YXR1cyI6MSwiaWF0IjoxNjAwOTE4NzU1LCJleHAiOjE2MDEwMDUxNTUsImlzcyI6ImNvbWVPbiJ9.f-m4QiX0OXm1nvJDxXvajr0AL0y480Y4EFVGcvttRAY", gsonObject)
        postCourseAddResponse.enqueue(object : Callback<ResponseMessageNonData> {

            //통신 실패 시 수행되는 메소드
            override fun onFailure(call: Call<ResponseMessageNonData>, t: Throwable) {
                Log.e("코스추가 통신 fail", t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse(
                call: Call<ResponseMessageNonData>,
                response: Response<ResponseMessageNonData>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(getApplicationContext(), "코스가 추가되었습니다.", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(getApplicationContext(), "코스추가가 실패되었습니다.", Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}

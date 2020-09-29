package com.ghdev.followme.ui.mycourse

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.ResponseMessageNonData
import com.ghdev.followme.network.get.ShopDAO
import com.ghdev.followme.util.CourseDialogFragment
import com.ghdev.followme.util.SearchAlarmDialog
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_mycourse_add.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MycourseAddActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var titleInputDialog : SearchAlarmDialog
    private var thema_id = -1
    private lateinit var placeLists : MutableList<ShopDAO>
    val YYYYMMDD = "(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])"

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }

    private fun makeDialog(message : String) : SearchAlarmDialog {
        titleInputDialog  = SearchAlarmDialog(this@MycourseAddActivity, message, TitleConfirmListener)
        return titleInputDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_add)

        clickInit()
        init()
    }

    private fun init() {

    }

    private fun clickInit() {
        btn_close_course_add.setOnClickListener(this)
        img_icon_select_btn.setOnClickListener(this)
        img_btn_add_my_course.setOnClickListener(this)
        cl_mycourse_add_layout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!) {
            btn_close_course_add -> {
                Log.v("TAGG", "터치가 왜 안되니?")
                finish()
            }

            //테마
            img_icon_select_btn -> {
                val dialog: CourseDialogFragment = CourseDialogFragment().getInstance()
                val fm = supportFragmentManager.beginTransaction()
                dialog.show(fm!!, "TAG_DIALOG_EVENT")
            }

            //작성완료 체크버튼 눌렀을 때
            img_btn_add_my_course -> {

                //edtiText에 있는 것 모두 체크해줘야함 ( ㅎㅎ )
                /*if (et_add_title.text.length == 0)
                    makeDialog("제목을 입력해주세요").show()
                else if(et_add_date.length() == 0)
                    makeDialog("날짜를 입력해주세요").show()
                else if(thema_id == -1)
                    makeDialog("테마를 선택해주세요").show()
                else if(placeLists.size == 0)
                    makeDialog("장소는 최소 1개 이상 선택해주세요.").show()
                else*/
                    postCourseAdd()
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

    private val TitleConfirmListener = View.OnClickListener {
        titleInputDialog!!.dismiss()
    }

    //키보드 다운
    private fun downKeyboard(view: View) {
        val imm: InputMethodManager =
            applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun postCourseAdd(){
        val title: String = et_add_title.text.toString()
        val dday : String = et_add_date.text.toString()
        placeLists = ArrayList<ShopDAO>()

        var jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("thema", thema_id)
        jsonObject.put("dday", dday)
        jsonObject.put("shops", placeLists)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postCourseAddResponse: Call<ResponseMessageNonData> =
            networkService.postCourseAdd(sharedPrefs.getString(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"), ""), gsonObject)
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
                    Log.v("코스추가 통신 성공", response.body().toString())
                    if(response.body()!!.code == 200) {
                        Toast.makeText(getApplicationContext(), "코스가 추가되었습니다.", Toast.LENGTH_LONG).show()
                        finish()
                    }else {
                        Toast.makeText(getApplicationContext(), "코스추가가 실패되었습니다.", Toast.LENGTH_LONG).show()
                    }

                }
            }
        })
    }
}

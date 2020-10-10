package com.ghdev.followme.ui.mycourse

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.*
import com.ghdev.followme.util.SearchAlarmDialog
import com.ghdev.followme.util.ThemaSelectDialog
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.naver.maps.geometry.LatLng
import kotlinx.android.synthetic.main.activity_mycourse_add.*
import kotlinx.android.synthetic.main.fragment_my_course.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MycourseAddActivity : AppCompatActivity(), View.OnClickListener, SearchRecyclerViewAdapter.OnItemClick {

    private lateinit var titleInputDialog : SearchAlarmDialog
    private lateinit var thema : ThemaSelectDialog
    lateinit var searchRecyclerViewAdapter: SearchRecyclerViewAdapter
    var categoryThemaText = ""
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

    private fun themaSelectDialog() : ThemaSelectDialog {
        thema = ThemaSelectDialog(this@MycourseAddActivity,LoverListener, FriendsListener, TVListener, ActivityListener, PetListener, FamilyListener, CloseListener)
        return thema
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_add)

        placeLists = ArrayList<ShopDAO>()
        clickInit()
        setEditTextSearch()
        setPlace()
    }

    private fun setRecyclerView() {
        var searchDataList : ArrayList<SearchResultResponseItem> = ArrayList()

        searchRecyclerViewAdapter = SearchRecyclerViewAdapter(this, searchDataList)
        rv_search_place.adapter = searchRecyclerViewAdapter
        rv_search_place.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun setPlace() {
        for(i in 0..placeLists.size - 1) {
            if(i == 0)
                tv_first_store_name_detail.text = placeLists[i].shopname
            else if(i == 1)
                tv_second_store_name_detail.text = placeLists[i].shopname
            else if( i == 2)
                tv_third_store_name_detail.text = placeLists[i].shopname
        }
    }

    private fun setEditTextSearch() {

        et_add_course.setOnClickListener {
            et_add_course.text.clear()
        }

        et_add_course.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_add_course.text != null || et_add_course.toString() != "")
                    postSearch()
                else
                    rv_search_place.visibility = View.INVISIBLE
            }
        })
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
                finish()
            }

            //테마
            img_icon_select_btn -> {
                themaSelectDialog().show()
            }

            //작성완료 체크버튼 눌렀을 때
            img_btn_add_my_course -> {

                //edtiText에 있는 것 모두 체크해줘야함 ( ㅎㅎ )
                if (et_add_title.text.toString() == "") {
                    makeDialog("제목을 입력해주세요").show()
                }
                else if ( et_add_date.text.toString() == "") {
                    makeDialog("날짜를 입력해주세요").show()
                }
                else if (thema_id == -1) {
                    makeDialog("테마를 선택해주세요").show()
                }
                else if(placeLists.size == 0)
                    makeDialog("장소는 최소 1개 이상 선택해주세요.").show()
                else {
                    postCourseAdd()
                }
            }

            //키보드 다운 함수
            cl_mycourse_add_layout -> {
                downKeyboard(cl_mycourse_add_layout)
                rv_search_place.visibility = View.INVISIBLE
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

    private val LoverListener = View.OnClickListener {
        categoryThemaText = "연인과 함께"
        thema_id = 0;
        select_thema.text = categoryThemaText
        thema!!.dismiss()
    }
    private val FriendsListener = View.OnClickListener {
        categoryThemaText = "친구와 함께"
        select_thema.text = categoryThemaText
        thema_id = 1;
        thema!!.dismiss()
    }
    private val TVListener = View.OnClickListener {
        categoryThemaText = "TV 출현"
        select_thema.text = categoryThemaText
        thema_id = 2;
        thema!!.dismiss()
    }
    private val ActivityListener = View.OnClickListener {
        categoryThemaText = "활동적인"
        select_thema.text = categoryThemaText
        thema_id = 3;
        thema!!.dismiss()
    }
    private val PetListener = View.OnClickListener {
        categoryThemaText = "애완동물과 함께"
        select_thema.text = categoryThemaText
        thema_id = 4;
        thema!!.dismiss()
    }
    private val FamilyListener = View.OnClickListener {
        categoryThemaText = "단체석"
        select_thema.text = categoryThemaText
        thema_id = 5;
        thema!!.dismiss()
    }
    private val CloseListener = View.OnClickListener {
        thema!!.dismiss()
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

    //코스 추가통신
    private fun postCourseAdd(){
        val title: String = et_add_title.text.toString()
        val dday : String = et_add_date.text.toString()

        val jsonObjectPlaceList = JSONObject()
        val jsonArray = JSONArray()
        for(i in 0..placeLists.size-1) {
            jsonObjectPlaceList.put("id", placeLists[i].id)
            jsonObjectPlaceList.put("shopname", placeLists[i].shopname)
            jsonArray.put(jsonObjectPlaceList)
        }

        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("thema", thema_id)
        jsonObject.put("dday", dday)
        jsonObject.put("shops", jsonArray)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postCourseAddResponse: Call<ResponseMessageNonData> =
            networkService.postCourseAdd(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"), gsonObject)
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

    //검색통신
    private fun postSearch() {
        val query: String = et_add_course.text.toString()

        val jsonObject = JSONObject()
        jsonObject.put("from", 0)
        jsonObject.put("query", query)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postSearchResponse: Call<SearchResultResponse> =
            networkService.postSeach(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"), gsonObject)
        postSearchResponse.enqueue(object : Callback<SearchResultResponse> {

            //통신 실패 시 수행되는 메소드
            override fun onFailure(call: Call<SearchResultResponse>, t: Throwable) {
                Log.e("검색 통신 fail", t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse(
                call: Call<SearchResultResponse>,
                response: Response<SearchResultResponse>
            ) {
                if (response.isSuccessful) {
                    val temp: ArrayList<SearchResultResponseItem> = response.body()!!
                    if (temp.size > 0) {
                        rv_search_place.visibility = View.VISIBLE
                        setRecyclerView()
                        val position = searchRecyclerViewAdapter.itemCount
                        searchRecyclerViewAdapter.dataList.addAll(temp)
                        searchRecyclerViewAdapter.notifyItemChanged(position)
                        searchRecyclerViewAdapter.setOnItemClickListener(this@MycourseAddActivity)
                    }
                }
            }
        })
    }

    override fun onClick(id: Int, shopname: String) {
        val shopAdd = ShopDAO(id, shopname)

        if(placeLists.size < 3 && !placeLists.contains(shopAdd))
            placeLists.add(shopAdd)
        else
            Toast.makeText(getApplicationContext(), "코스를 추가할 수 없습니다..", Toast.LENGTH_LONG).show()

        rv_search_place.visibility = View.INVISIBLE
        setPlace()
    }
}

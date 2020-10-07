package com.ghdev.followme.ui.mycourse

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.CourseDetailResponse
import com.ghdev.followme.network.get.ResponseMessageNonData
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.network.get.ShopDAO
import com.ghdev.followme.ui.home.HotPlaceRecyclerViewAdapter
import com.ghdev.followme.util.SearchAlarmDialog
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.android.synthetic.main.activity_mycourse_add.*
import kotlinx.android.synthetic.main.activity_mycourse_detail.*
import kotlinx.android.synthetic.main.activity_mypage_mysetting.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MycourseDetailActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var titleInputDialog : SearchAlarmDialog
    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter
    private var naverMap: NaverMap? = null
    lateinit var path: PathOverlay
    val mapCoords = mutableListOf<LatLng>()
    var courseIdx = 0
    var userNickname = ""

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_detail)

        init()
        getCourseDetailResponse()
        setRecyclerView()
    }

    override fun onClick(v: View?) {
        when(v){
            btn_edit_mycourse -> {
                if(userNickname != "")
                    showPopup(btn_edit_mycourse)
            }
        }
    }

    fun init() {
        btn_edit_mycourse.setOnClickListener(this)

        userNickname = intent.getStringExtra("user_nickname")
        if(userNickname == "")
            btn_edit_mycourse.visibility = View.GONE
    }

    //팝업창
    private fun showPopup(view: View) {
        var popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_main)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.menu_modified -> {
                    makeDialog("준비중인 기능입니다").show()
                }
                R.id.menu_delete -> {
                    postCourseDelete()
                }
            }
            true
        })

        popup.show()
    }

    private fun makeDialog(message : String) : SearchAlarmDialog {
        titleInputDialog  = SearchAlarmDialog(this@MycourseDetailActivity, message, TitleConfirmListener)
        return titleInputDialog
    }

    private val TitleConfirmListener = View.OnClickListener {
        titleInputDialog!!.dismiss()
    }

    fun settingMap() {
        //NaverMap 객체 얻어오기
        val fm = supportFragmentManager
        val mapvRecDetail = fm.findFragmentById(R.id.map_naver_mycourse_detail) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_naver_mycourse_detail, it).commit()
            }

        mapvRecDetail.getMapAsync(this)
    }

    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0

        //지도에 경로 뿌리기
        if (mapCoords.size >= 2) {
            //경로객체 만들기
            path = PathOverlay()
            path.coords = mapCoords

            naverMap?.let {
                path.map = it

                //마커 세팅
                val marker = Marker()
                marker.position = path.coords[path.coords.size-1]
                marker.captionText = "Hello"
                //marker.icon = OverlayImage.fromResource(R.drawable.icon_location)
                //marker.map = naverMap

                //경로선 커스텀
                path.width = 20
                path.outlineWidth = 0
                path.color = Color.parseColor("#3868ff")

                var la : Double = 0.0
                var lo : Double = 0.0
                //카메라 포커스 이동
                for(coord in mapCoords) {
                    la += coord.latitude
                    lo += coord.longitude
                }

                val map = mutableListOf<LatLng>()
                map.add(LatLng(la/3, lo/3))

                val coord = map[0]
                it.moveCamera(CameraUpdate.scrollTo(coord))
            }
        }
    }

    private fun setRecyclerView() {

        /* dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo( 0,"하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313")) */

        //모듈화를 시키기(rv_id와 datalist가 들어가는 것 말고는 다른 것은 동일)
        var dataList: ArrayList<Shop> = ArrayList()

        hotPlaceRecyclerViewAdapter = HotPlaceRecyclerViewAdapter(this, dataList)
        rv_store_list.adapter = hotPlaceRecyclerViewAdapter
        rv_store_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    //코스 detail 통신
    private fun getCourseDetailResponse() {
        courseIdx = intent.getIntExtra("course_idx", -1)
        //Log.d("Detail후후", courseIdx.toString())

        val getOurCorse: Call<CourseDetailResponse> =
            networkService.getCourseDetail(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"), courseIdx)

        getOurCorse.enqueue(object : Callback<CourseDetailResponse> {
            override fun onFailure(call: Call<CourseDetailResponse>, t: Throwable) {
                Log.d("GET course detail fail", t.toString())
            }

            override fun onResponse(
                call: Call<CourseDetailResponse>,
                response: Response<CourseDetailResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("TAGG 22 in detail", response.body().toString() )

                    //null처리
                    if(response.body()?.title == null)
                        tv_course_title_mycourse_detail.text = "null"
                    if(response.body()?.dday == null)
                        tv_date.text = "null"
                    if(response.body()?.title == null)
                        rb_star_mycourse_detail.rating = 3F

                    tv_course_title_mycourse_detail.text = response.body()!!.title
                    tv_date.text = response.body()!!.dday
                    rb_star_mycourse_detail.rating = (response.body()!!.grade_avg/2).toFloat()

                    val temp: ArrayList<Shop> = response.body()!!.shops

                    if (temp.size > 0) {
                        val position = hotPlaceRecyclerViewAdapter.itemCount
                        hotPlaceRecyclerViewAdapter.dataList.addAll(temp)
                        hotPlaceRecyclerViewAdapter.notifyItemChanged(position)
                        mapCoords.clear()

                        for(i in 0..temp.size-1) {
                            mapCoords.add(LatLng(temp[i].latitude, temp[i].longitude))
                        }

                        Log.v("TAGG map " , mapCoords.toString())
                        settingMap()
                    }
                    else {

                    }
                }
            }
        })
    }

    //코스 삭제 통신
    private fun postCourseDelete(){
        val jsonObject = JSONObject()
        jsonObject.put("id", courseIdx)
        Log.v("TAGG id", courseIdx.toString())

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val deleteCourseAddResponse: Call<ResponseMessageNonData> =
            networkService.deleteCourse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"), gsonObject)
        deleteCourseAddResponse.enqueue(object : Callback<ResponseMessageNonData> {

            //통신 실패 시 수행되는 메소드
            override fun onFailure(call: Call<ResponseMessageNonData>, t: Throwable) {
                Log.e("코스삭제 통신 fail", t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse(
                call: Call<ResponseMessageNonData>,
                response: Response<ResponseMessageNonData>
            ) {

                Log.v("코스삭제 통신 성공 body", response.body().toString())
                Log.v("코스삭제 통신 성공 success", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    Log.v("코스삭제 통신 성공", response.body().toString())
                    if(response.body()!!.code == 200) {
                        Toast.makeText(getApplicationContext(), "코스가 삭제되었습니다.", Toast.LENGTH_LONG).show()
                        finish()
                    }else if(response.body()!!.code == 403){
                        Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_LONG).show()
                    }else {
                        Toast.makeText(getApplicationContext(), "코스 삭제가 실패했습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}

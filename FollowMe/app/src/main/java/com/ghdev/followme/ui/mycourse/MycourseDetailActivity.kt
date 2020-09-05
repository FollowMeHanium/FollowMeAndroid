package com.ghdev.followme.ui.mycourse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.PlaceDetailActivity
import com.ghdev.followme.ui.home.HomeFragment
import com.ghdev.followme.ui.home.HotPlaceRecyclerViewAdapter
import com.naver.maps.map.MapFragment
import kotlinx.android.synthetic.main.activity_mycourse_detail.*

class MycourseDetailActivity : AppCompatActivity() {

    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter
    var courseIdx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_detail)


        init()
        setRecyclerView()

    }

    private fun init() {
        courseIdx = intent.getIntExtra("course_idx", -1)
    }

    fun settingMap() {
        //NaverMap 객체 얻어오기
        val fm = supportFragmentManager
        val mapvRecDetail = fm.findFragmentById(R.id.map_naver_mycourse_detail) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_naver_mycourse_detail, it).commit()
            }

        //mapvRecDetail.getMapAsync(this)
    }

/*    override fun onMapReady(p0: NaverMap) {
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
                marker.icon = OverlayImage.fromResource(R.drawable.icon_location)
                marker.map = naverMap

                //경로선 커스텀
                path.width = 20
                path.outlineWidth = 0
                path.color = Color.parseColor("#3868ff")

                //카메라 포커스 이동
                val coord = mapCoords[mapCoords.size - 1]
                it.moveCamera(CameraUpdate.scrollTo(coord))
            }
        }
    }*/

/*
    fun loadCoordinateDatas() {
        var token = prefs.getString("token", null)
        requestToServer.service.requestRecordDetail(token, runIdx).customEnqueue(
            onFailure = { call, t ->
                Log.d(
                    "RecDetailActivity",
                    "requestRecDetail onFailure msg = ${t.message}"
                )
            },
            onResponse = { call, r ->
                if (r.isSuccessful) {
                    val body = r.body()
                    if (body?.status == 200) {
                        if (body?.success) {
                            //DateData Setting
                            tvRecDetailDate.text =
                                body.result.month.toString() + "월 " + body.result.day.toString() + "일의 러닝"

                            //TimeData Setting
                            val sTimeArr = body.result.start_time!!.split(":")
                            val eTimeArr = body.result.end_time!!.split(":")

                            var sTimeTitle = ""
                            var eTimeTitle = ""

                            if (sTimeArr[0].toInt() < 12)
                                sTimeTitle = "오전"
                            else
                                sTimeTitle = "오후"

                            if (eTimeArr[0].toInt() < 12)
                                eTimeTitle = "오전"
                            else
                                eTimeTitle = "오후"

                            var sTimeHour = (sTimeArr[0].toInt() % 12)
                            var eTimeHour = (eTimeArr[0].toInt() % 12)

                            if (sTimeHour == 0)
                                sTimeHour = 12

                            if (eTimeHour == 0)
                                eTimeHour = 12

                            tvRecDetailTime.text =
                                sTimeTitle + sTimeHour.toString() + ":" + sTimeArr[1] + "-" + eTimeTitle + eTimeHour.toString() + ":" + eTimeArr[1]

                            //coordinate를 mapCoords: List<LatLng>에 Setting
                            val coordsDoubleArr = body.result.coordinate
                            for (coord in coordsDoubleArr) {
                                mapCoords.add(LatLng(coord.latitude, coord.longitude))
                            }

                            settingMap()
                        }
                    }
                } else {
                    Log.d(
                        "TAG",
                        "requestRecordDetail onSuccess but response code is not 200 ~ 300 " +
                                "(status code:${r.code()}) " +
                                "(message: ${r.message()})" +
                                "(errorBody: ${r.errorBody()})"
                    )
                }
            }

        )
    }*/

    private fun setRecyclerView() {

        //핫플
        //데이터는 서버에서 받을 것
        //모듈화를 시키기(rv_id와 datalist가 들어가는 것 말고는 다른 것은 동일)
        var dataList: ArrayList<PlaceInfo> = ArrayList()

        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo( 0,"하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))

        hotPlaceRecyclerViewAdapter =
            HotPlaceRecyclerViewAdapter(dataList) { PlaceInfo ->
                val intent = Intent(
                    this,
                    PlaceDetailActivity::class.java
                )
                intent.putExtra(
                    HomeFragment.PLACE_INFO,
                    PlaceInfo
                )
                startActivity(intent)
            }

        rv_store_list.adapter = hotPlaceRecyclerViewAdapter
        rv_store_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }
}

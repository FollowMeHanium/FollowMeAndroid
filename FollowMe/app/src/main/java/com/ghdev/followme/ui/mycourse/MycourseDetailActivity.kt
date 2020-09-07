package com.ghdev.followme.ui.mycourse

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.PlaceDetailActivity
import com.ghdev.followme.ui.home.HomeFragment
import com.ghdev.followme.ui.home.HotPlaceRecyclerViewAdapter
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.android.synthetic.main.activity_mycourse_detail.*

class MycourseDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter
    private var naverMap: NaverMap? = null
    lateinit var path: PathOverlay
    val mapCoords = mutableListOf<LatLng>()
    var courseIdx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_detail)

        init()
        loadCoordinateDatas()
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

    fun loadCoordinateDatas() {
       /* val coordsDoubleArr =
        for (coord in coordsDoubleArr) {
            mapCoords.add(LatLng(coord.latitude, coord.longitude))
        }*/

        //##서버에서 좌표 받아오면 됨

        mapCoords.add(LatLng(37.57152, 126.97714))
        mapCoords.add(LatLng(37.56607, 126.98268))
        mapCoords.add(LatLng(37.56445, 126.97707))

        settingMap()
    }

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

package com.ghdev.followme.ui.mycourse

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.CourseDetailResponse
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.home.HotPlaceRecyclerViewAdapter
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.android.synthetic.main.activity_mycourse_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MycourseDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var hotPlaceRecyclerViewAdapter: HotPlaceRecyclerViewAdapter
    private var naverMap: NaverMap? = null
    lateinit var path: PathOverlay
    val mapCoords = mutableListOf<LatLng>()
    var courseIdx = 0

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mycourse_detail)

        getCourseDetailResponse()
        loadCoordinateDatas()
        setRecyclerView()

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

        /*
        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo( 0,"하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))
        dataList.add(PlaceInfo(0, "하", "서울시 노원구 공릉동 131313"))*/

        //모듈화를 시키기(rv_id와 datalist가 들어가는 것 말고는 다른 것은 동일)
        var dataList: ArrayList<Shop> = ArrayList()

        hotPlaceRecyclerViewAdapter = HotPlaceRecyclerViewAdapter(this, dataList)
        rv_store_list.adapter = hotPlaceRecyclerViewAdapter
        rv_store_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun getCourseDetailResponse() {
        courseIdx = intent.getIntExtra("course_idx", -1)
        Log.d("Detail후후", courseIdx.toString())

        //## token 자리에 SharedPreference 에 있는 token 값 가져와야함.
        val getOurCorse: Call<CourseDetailResponse> =
            networkService.getCourseDetail(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxfQ.ldsBBxz_tUoqEMKD39ugh1rW32kR6tNLfQ-j7nLKi5Y",
            courseIdx)

        getOurCorse.enqueue(object : Callback<CourseDetailResponse> {
            override fun onFailure(call: Call<CourseDetailResponse>, t: Throwable) {
                Log.d("GET course detail fail", t.toString())
            }

            override fun onResponse(
                call: Call<CourseDetailResponse>,
                response: Response<CourseDetailResponse>
            ) {
                Log.d("TAGG 22 in detail", response.isSuccessful.toString() )
                Log.d("TAGG 22 in detail", response.message().toString() )
                if (response.isSuccessful) {
                    //null처리
                    if(response.body()?.title == null)
                        tv_course_title_mycourse_detail.text = "null"
                    if(response.body()?.dday == null)
                        tv_date.text = "null"
                    if(response.body()?.title == null)
                        rb_star_mycourse_detail.rating = 3F

                    tv_course_title_mycourse_detail.text = response.body()!!.title
                    tv_date.text = response.body()!!.dday
                    rb_star_mycourse_detail.rating = response.body()!!.like.toFloat()

                    val temp: ArrayList<Shop> = response.body()!!.shops

                    if (temp.size > 0) {

                        val position = hotPlaceRecyclerViewAdapter.itemCount
                        hotPlaceRecyclerViewAdapter.dataList.addAll(temp)
                        hotPlaceRecyclerViewAdapter.notifyItemInserted(position)
                    }
                }
            }
        })
    }
}

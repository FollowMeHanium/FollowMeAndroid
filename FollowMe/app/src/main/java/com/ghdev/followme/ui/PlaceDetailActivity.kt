package com.ghdev.followme.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghdev.followme.R
import com.ghdev.followme.network.get.GetShopInfoResponse
import com.ghdev.followme.data.test.GetRecommendListInfo
import com.ghdev.followme.data.test.ReviewInfo
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import kotlinx.android.synthetic.main.activity_place_detail.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceDetailActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    lateinit var placeReivewRecyclerViewAdapter: PlaceReivewRecyclerViewAdapter
    var reviewList : ArrayList<ReviewInfo> = ArrayList()
    val mapCoords = mutableListOf<LatLng>()
    lateinit var path: PathOverlay
    private var naverMap: NaverMap? = null

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }

    //찜하기 여부
    var mypick : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        //Recyclerview에서 받은 정보 표시 -> shop id값 intent받기
        /*val place_info = intent.getParcelableExtra<PlaceInfo>(PLACE_INFO)
        tv_place_detail_title.text = place_info.name
        tv_place_detail_name.text = place_info.address
        Glide.with(this).load(place_info.img).into(iv_place_detail_main)*/

        //Recyclerview에서 받은 정보 표시
        //val place_info = intent.getIntExtra("place_idx", -1)
        //Log.d("place_info: ", place_info.toString())

        init()
        //getShopInfoResponse(place_info)
        getShopInfoResponse(2)
        loadCoordinateDatas()
        PlaceReviewRecycler()
    }



        /*
        tv_place_detail_title.text = place_info.shopname
        tv_place_detail_name.text = place_info.address
        Glide.with(this).load(place_info.main_photo).into(iv_place_detail_main)*/

    private fun init(){
        btn_place_detail_add_review.setOnClickListener(this)
        btn_place_detail_add_mypick.setOnClickListener(this)
        btn_place_detail_add_mycourse.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_place_detail_add_review ->{
                //리뷰 다이어로그 띄우기
                ReviewDialogCreate()
            }

            btn_place_detail_add_mypick ->{
                //좋아요버튼 이벤트
                ClickMypick()
            }

            btn_place_detail_add_mycourse ->{
                //내코스 추가하기 이벤트
                MyCourseDialogCreate()
            }
        }
    }

    /**안됌ㅠㅠ**/
    /************************Shop정보 읽어오기********************/
    private fun getShopInfoResponse(id : Int){
        //val getshop : Call<GetShopInfoResponse> = networkService.getShopInfoResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"),id)
        val getshop : Call<GetShopInfoResponse> = networkService.getShopInfoResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxfQ.ldsBBxz_tUoqEMKD39ugh1rW32kR6tNLfQ-j7nLKi5Y",2)

        Log.d("getshop", "동작")
        toast("getshop동작")
        getshop.enqueue(object: Callback<GetShopInfoResponse>{
            override fun onFailure(call: Call<GetShopInfoResponse>, t: Throwable) {
                Log.d("getshop", "실패/ " + t.message)
                toast("getshop실패/" + t.message)
            }

            override fun onResponse(
                call: Call<GetShopInfoResponse>,
                response: Response<GetShopInfoResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("getshop", "성공")
                    toast("getshop 성공" + response.body()!!.id)
                    //tv_place_detail_title.text = response.body()!!.shopname
                    //tv_place_detail_watch.text = response.body()!!.operating_time
                    //tv_place_detail_menu.text = response.body()!!.menu
                    //tv_place_detail_address.text = response.body()!!.address
                }
            }

        })
        Log.d("getshop", "완료")
    }

    fun PlaceReviewRecycler(){
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))
        reviewList.add(ReviewInfo("aaaaaa", "동해물과 백두산이 마르고 닳도록~", 5))

        placeReivewRecyclerViewAdapter = PlaceReivewRecyclerViewAdapter(reviewList)
        rv_place_detail_review.adapter = placeReivewRecyclerViewAdapter
        rv_place_detail_review.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }



    /*************리뷰추가********************/

    fun ReviewDialogCreate(){
        val builder = AlertDialog.Builder(this)
        val dv = layoutInflater.inflate(R.layout.dialog_review_insert, null)
        builder.setView(dv)
        builder.setPositiveButton("확인"){dialog, i ->
            toast("확인 클릭!!")
        }
            .setNegativeButton("취소"){dialog, i ->
                toast("취소 클릭!!")
            }
        builder.show()
        rv_place_detail_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }



    /****************좋아요 버튼***********************/
    fun ClickMypick(){

        val anim = AnimationUtils.loadAnimation(this, R.anim.anim_mypick_alpha)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                iv_place_detail_anim_mypick.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                iv_place_detail_anim_mypick.visibility = View.VISIBLE
            }

        })

        //mypick가 선택되어져 있을때
        if(mypick){
            btn_place_detail_add_mypick.setImageResource(R.drawable.btn_add_mypick)
            Toast.makeText(this, "찜 리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            mypick = false
        }
        //mypick가 선택되어있지 않을때
        else{
            btn_place_detail_add_mypick.setImageResource(R.drawable.btn_selected_mypick)
            iv_place_detail_anim_mypick.startAnimation(anim)
            Toast.makeText(this, "찜 리스트 추가되었습니다.", Toast.LENGTH_SHORT).show()
            mypick = true
        }

    }



    /**********************내코스 추가하기 버튼***************************/
    fun MyCourseDialogCreate() {
        val builder = AlertDialog.Builder(this)
        val mycourse = arrayOf("내가 가고싶은 코스1", "내가 가고싶은 코스2")
        val checkedItem = 1 // cow
        builder.setTitle("내 코스에 추가하기")
        .setSingleChoiceItems(mycourse, checkedItem) { dialog, which ->
            // user checked an item
        }
        .setPositiveButton("확인"){dialog, which ->
            //user clicked OK
        }
        .setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }

    /****************************지도 API*********************************/
    override fun onMapReady(p0: NaverMap) {
        this.naverMap = p0

        if(mapCoords.size >=2){
            path = PathOverlay()
            path.coords = mapCoords

            naverMap?.let{
                path.map = it
                val marker = Marker()
                marker.position = path.coords[path.coords.size-1]
                marker.captionText = "Hello"

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
        settingMap()
    }

    fun settingMap() {
        //NaverMap 객체 얻어오기
        val fm = supportFragmentManager
        val mapvRecDetail = fm.findFragmentById(R.id.map_naver_shop_detail) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_naver_shop_detail, it).commit()
            }

        mapvRecDetail.getMapAsync(this)
    }


}

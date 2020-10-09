package com.ghdev.followme.ui

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RatingBar
import androidx.annotation.UiThread
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.JWTDecode
import com.ghdev.followme.data.PostCodeAndMessageResponse
import com.ghdev.followme.data.test.PostShopLikeResponse
import com.ghdev.followme.data.test.PostShopUnLikeResponse
import com.ghdev.followme.network.get.GetShopInfoResponse
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.GetShopReviewListResponse
import com.ghdev.followme.network.get.Review
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.dialog_review_insert.*
import kotlinx.android.synthetic.main.dialog_review_insert.view.*
import kotlinx.android.synthetic.main.item_place_review.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceDetailActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    //recyclerview
    lateinit var placeReivewRecyclerViewAdapter: PlaceReivewRecyclerViewAdapter
    lateinit var placePictureRecyclerViewAdapter: PlacePictureRecyclerViewAdapter
    lateinit var placeMenuGridViewAdapter: PlaceMenuGridViewAdapter
    var reviewList : ArrayList<Review> = ArrayList()
    var picturesList : ArrayList<String> = ArrayList()
    var menu_nameList : ArrayList<String> = ArrayList()
    var menu_priceList : ArrayList<String> = ArrayList()
    var menuList : ArrayList<String> = ArrayList()

    //naver map
    //key값 넣어야 함! (안넣음)
    val mapCoords = mutableListOf<LatLng>()
    private var naverMap: NaverMap? = null

    //recyclerview로부터 받은 shop id
    var place_info : Int = 0

    //이미지로딩 -> 왜 안되?
    private  val url = "http://3.15.22.4:3005"

    //애니메이션
    lateinit var anim : Animation

    //찜하기 여부, 디폴트값은 false
    var mypick : Boolean = false


    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        //Glide.with(this).load(url+"/images/shop/4.jpg").into(iv_place_detail_main)

        //Recyclerview에서 받은 정보 표시
        place_info = intent.getIntExtra("place_idx", -1)
        Log.d("place_info: ", place_info.toString())

        init()
        getShopInfoResponse(place_info)
        getShopReviewListResponse(place_info)
        //loadCoordinateDatas()
        PlaceReviewRecycler()
    }

    private fun init(){
        btn_place_detail_add_review.setOnClickListener(this)
        btn_place_detail_add_mypick.setOnClickListener(this)
        btn_place_detail_add_mycourse.setOnClickListener(this)

        /*************애니메이션 정의****************/
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_mypick_alpha)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                iv_place_detail_anim_mypick.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                iv_place_detail_anim_mypick.visibility = View.VISIBLE
            }
        })

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


    /************************Shop정보 읽어오기********************/
    private fun getShopInfoResponse(id : Int){
                val getshop : Call<GetShopInfoResponse> = networkService.getShopInfoResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"),id)


                Log.d("getshop", "동작")
                getshop.enqueue(object: Callback<GetShopInfoResponse>{
                    override fun onFailure(call: Call<GetShopInfoResponse>, t: Throwable) {
                        Log.d("getshop", "실패/ " + t.message)
                    }

                    override fun onResponse(
                        call: Call<GetShopInfoResponse>,
                        response: Response<GetShopInfoResponse>
                    ) {
                        if(response.isSuccessful){
                            Log.d("getshop", "성공")
                            //가게 정보 갱신
                            tv_place_detail_title.text = response.body()!!.shopname
                            tv_place_detail_watch.text = response.body()!!.operating_time
                            tv_place_detail_address.text = response.body()!!.address

                            //지도 만들기
                            mapCoords.clear()
                            mapCoords.add(LatLng(response.body()!!.latitude, response.body()!!.longitude))
                            Log.d("getshop", "위도경도: " + response.body()!!.latitude.toString() + "/" + response.body()!!.longitude.toString())
                            Log.d("getshop", "위도경도/map: " + mapCoords)
                            settingMap()


                            //좋아요버튼 이미지
                            if(response.body()!!.like == 0){
                                btn_place_detail_add_mypick.setImageResource(R.drawable.btn_add_mypick)
                                mypick = false
                            }else{
                                btn_place_detail_add_mypick.setImageResource(R.drawable.btn_selected_mypick)
                                mypick = true
                            }
                            Log.d("getshop_like: ", response.body()!!.like.toString() + "/" + mypick.toString())

                            //가게 이미지 갱신
                            val getphoto = response.body()!!.main_photo - 1
                            Log.d("getshop", getphoto.toString() + "/" + response.body()!!.photos[0])
                            if(response.body()!!.photos[getphoto].startsWith("h"))
                                Glide.with(getApplicationContext()).load(response.body()!!.photos[getphoto]).into(iv_place_detail_main)
                            else
                                Glide.with(getApplicationContext()).load(url + response.body()!!.photos[getphoto]).into(iv_place_detail_main)

                            //가게 이미지들 넣기
                            val getphotos = response.body()!!.photos
                            if(getphotos.isNullOrEmpty()){
                                Log.d("getshop", "가게 이미지들이 없습니당.")
                            }
                            else {
                                val position = placePictureRecyclerViewAdapter.itemCount
                                placePictureRecyclerViewAdapter.dataList.addAll(getphotos)
                                placePictureRecyclerViewAdapter.notifyItemInserted(position)
                            }

                            //가게 메뉴 넣기
                            try{
                                var jsonObject = JSONObject(response.body()!!.menu)

                                val i = jsonObject.keys().iterator()

                                //반복자를 사용하여 json의 key값을 다 가지고 옴
                                while(i.hasNext()){
                                    val temp : String = i.next().toString()
                                    menu_nameList.add(temp)
                                }
                                for(j in menu_nameList){
                                    menu_priceList.add(jsonObject.getString(j))
                                }
                                Log.d("getshop", "list size값 : " + menu_nameList.size.toString())
                                //menuList에 넣기
                                for(j in 0 until menu_nameList.size step 1){
                                    menuList.add(menu_nameList[j])
                                    menuList.add(menu_priceList[j])
                                }
                                placeMenuGridViewAdapter = PlaceMenuGridViewAdapter(applicationContext, menuList)
                                gv_place_detail_menu.adapter = placeMenuGridViewAdapter
                                //gridview가 안보이는 거 때문에(아마 listview가 여러개라 그런가봄) padding조절
                                gv_place_detail_menu.setPadding(0,0,0,30 * menu_nameList.size)
                            }catch(e : JSONException){
                                Log.d("getshop", e.printStackTrace().toString())
                            }
                        }
                   }

        })
        Log.d("getshop", "완료")
    }


    /******************리뷰 리스트 불러오기********************/

    private fun getShopReviewListResponse(place_info : Int){
        val getreview : Call<GetShopReviewListResponse> = networkService.getShopReviewListResponse((sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0")), place_info)

        getreview.enqueue(object: Callback<GetShopReviewListResponse>{
            override fun onFailure(call: Call<GetShopReviewListResponse>, t: Throwable) {
                Log.d("getreview: ", "실패 " + t.message)
            }

            override fun onResponse(
                call: Call<GetShopReviewListResponse>,
                response: Response<GetShopReviewListResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("getreview: ", "성공 " + response.isSuccessful.toString())
                    val temp : ArrayList<Review> = response.body()!!.reviews
                    if(temp.isNullOrEmpty()) {
                        Log.d("getreview", "리뷰가 null값입니다.")
                        tv_place_detail_no_review.visibility = View.VISIBLE
                    }
                    else{
                        Log.d("getreview", "리뷰가 있습니다. " + response.body()!!.reviews.toString())
                        val position = placeReivewRecyclerViewAdapter.itemCount
                        placeReivewRecyclerViewAdapter.dataList.addAll(temp)
                        placeReivewRecyclerViewAdapter.notifyItemInserted(position)
                    }
                }
            }

        })
    }

    fun PlaceReviewRecycler(){
        placeReivewRecyclerViewAdapter = PlaceReivewRecyclerViewAdapter(reviewList)
        rv_place_detail_review.adapter = placeReivewRecyclerViewAdapter
        rv_place_detail_review.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        placePictureRecyclerViewAdapter = PlacePictureRecyclerViewAdapter(picturesList)
        rv_place_detail_picture.adapter = placePictureRecyclerViewAdapter
        rv_place_detail_picture.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false)
    }



    /*************리뷰추가********************/

    fun ReviewDialogCreate(){
        val builder = AlertDialog.Builder(this)
        val dv = layoutInflater.inflate(R.layout.dialog_review_insert, null)
        builder.setView(dv)

        builder.setPositiveButton("확인"){dialog, i ->
            var input_grade : Double = dv.rb_dialog_review.rating.toDouble()
            var input_review : String = dv.et_dialog_review.text.toString()
            Log.d("review_write_fun: ",  "review: " + input_review)
            var input_nick : String = JWTDecode().DecodeToken(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"))

            input_nick = input_nick.replace("\"", "")
            Log.d("review_write_fun: ",  "nickname: " + input_nick)

            //내용을 입력하지 않을시
            if(dv.et_dialog_review.text.toString().trim().isNotEmpty()){
                postShopReviewWriteResponse(place_info, input_grade, input_nick, input_review)
            }else{
                toast("내용을 입력하세요.")
            }
        }
            .setNegativeButton("취소"){dialog, i ->
                toast("취소 클릭!!")
            }
        builder.show()
        rv_place_detail_review.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    /***********************리뷰 작성 post*************************/

    private fun postShopReviewWriteResponse(place_info : Int, input_grade : Double, input_nick : String, input_review : String){
        var jsonObject = JSONObject()
        jsonObject.put("shop_id", place_info)
        jsonObject.put("grade", input_grade)
        jsonObject.put("review", input_review)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.d("review_write_fun: ", "gson")

        val postShopReviewWriteResponse : Call<PostCodeAndMessageResponse> =
            networkService.postShopReviewWriteResponse((sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0")), gsonObject)
        postShopReviewWriteResponse.enqueue(object : Callback<PostCodeAndMessageResponse>{
            override fun onFailure(call: Call<PostCodeAndMessageResponse>, t: Throwable) {
                Log.d("review_write_fun: ", "실패 " + t.message)
            }

            override fun onResponse(
                call: Call<PostCodeAndMessageResponse>,
                response: Response<PostCodeAndMessageResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("review_write_fun: ", "작동 " + response.body()!!.message)
                    Log.d("review_write_fun: ", "작동 " + response.body()!!.code)
                    Log.d("review_wride_fun: ", "input_review: " + input_review)
                    reviewList.add(Review(place_info, input_grade, input_nick, input_review))
                    placeReivewRecyclerViewAdapter.notifyItemInserted(reviewList.size)
                    tv_place_detail_no_review.visibility = View.GONE
                    rv_place_detail_review.visibility = View.VISIBLE
                    //getShopReviewListResponse(place_info)
                }
            }

        })
    }



    /****************좋아요 버튼***********************/
    fun ClickMypick(){
        //mypick가 선택되어져 있을때
        if(mypick){
            Log.d("mypick_state: ", mypick.toString())
            postShopUnLikeResponse()
        }
        //mypick가 선택되어있지 않을때
        else{
            Log.d("mypick_state: ", mypick.toString())
            postShopLikeResponse()
        }

    }

    //좋아요 리스트에 추가
    private fun postShopLikeResponse(){

        var jsonObject = JSONObject()
        jsonObject.put("id", place_info)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.d("like_fun: ", "gson")

        val postShopLikeResponse : Call<PostCodeAndMessageResponse> =
            networkService.postShopLikeResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"), gsonObject)

        postShopLikeResponse.enqueue(object : Callback<PostCodeAndMessageResponse>{
            override fun onFailure(call: Call<PostCodeAndMessageResponse>, t: Throwable) {
                Log.e("like_fun: ", t.toString())
            }

            override fun onResponse(
                call: Call<PostCodeAndMessageResponse>,
                response: Response<PostCodeAndMessageResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("like_fun: ", "성공")
                    if(response.body()!!.code == 200){
                        Log.d("like_fun: ", response.body()!!.message)
                        Log.d("like_fun", response.body()!!.code.toString())
                        toast("찜 리스트 추가되었습니다.")

                        btn_place_detail_add_mypick.setImageResource(R.drawable.btn_selected_mypick)
                        iv_place_detail_anim_mypick.startAnimation(anim)
                        mypick = true
                    }else{
                        Log.d("like_fun: ", "fail" + response.body()!!.message)
                        Log.d("like_fun", "fail" + response.body()!!.code.toString())
                    }
                }
            }

        })
    }

    //좋아요 리스트에 삭제
    private fun postShopUnLikeResponse() {

        var jsonObject = JSONObject()
        jsonObject.put("id", place_info)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        Log.d("like_un_fun: ", "gson")

        val postShopUnLikeResponse : Call<PostCodeAndMessageResponse> =
            networkService.postShopUnLikeResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"), gsonObject)

        postShopUnLikeResponse.enqueue(object : Callback<PostCodeAndMessageResponse>{
            override fun onFailure(call: Call<PostCodeAndMessageResponse>, t: Throwable) {
                Log.e("like_un_fun: ", t.toString())
            }

            override fun onResponse(
                call: Call<PostCodeAndMessageResponse>,
                response: Response<PostCodeAndMessageResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("like_un_fun: ", "성공")

                    if(response.body()!!.code == 200){
                        Log.d("like_un_fun: ", response.body()!!.message)
                        Log.d("like_un_fun: ", response.body()!!.code.toString())
                        toast("찜 리스트에서 삭제되었습니다.")

                        btn_place_detail_add_mypick.setImageResource(R.drawable.btn_add_mypick)
                        mypick = false
                    }else{
                        Log.d("like_fun: ", "fail/" + response.body()!!.message)
                        Log.d("like_fun", "fail/" + response.body()!!.code.toString())
                    }
                }

            }

        })

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
        Log.d("onmap", "동작!")


        if(mapCoords.size >0){
            //마커 세팅
            val marker = Marker()

            naverMap?.let{

                var pos = it.cameraPosition
                Log.d("onmap", "camerPosition: " + pos.toString())

                pos  = CameraPosition(mapCoords[mapCoords.size-1], 16.0)
                it.cameraPosition = pos
                Log.d("onmap", "camerPosition: " + pos.toString())

                marker.position = mapCoords[mapCoords.size-1]
                Log.d("onmap", "marker.position: " + marker.position.toString())
                marker.icon = MarkerIcons.BLACK
                marker.iconTintColor = Color.RED
                marker.map = it

                var la : Double = 0.0
                var lo : Double = 0.0
                //카메라 포커스 이동
                for(coord in mapCoords) {
                    la += coord.latitude
                    lo += coord.longitude
                }

                val map = mutableListOf<LatLng>()
                map.add(LatLng(la, lo))

                val coord = map[0]
                it.moveCamera(CameraUpdate.scrollTo(coord))

            }
        }

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

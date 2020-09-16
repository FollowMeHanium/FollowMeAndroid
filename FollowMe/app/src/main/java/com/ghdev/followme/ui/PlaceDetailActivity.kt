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
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.GetShopInfoResponse
import com.ghdev.followme.data.test.GetRecommendListInfo
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.home.HomeFragment.Companion.PLACE_INFO
import com.ghdev.followme.data.test.ReviewInfo
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.dialog_review_insert.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var placeReivewRecyclerViewAdapter: PlaceReivewRecyclerViewAdapter
    var reviewList : ArrayList<ReviewInfo> = ArrayList()

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

        init()
        getShopInfoResponse()
        PlaceReviewRecycler()
        //getRecomendInfo()
    }



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

    private fun getShopInfoResponse(){
        val getshop : Call<GetShopInfoResponse> = networkService.getShopInfoResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"),3)
        Log.d("getshop", "동작")

        getshop.enqueue(object: Callback<GetShopInfoResponse>{
            override fun onFailure(call: Call<GetShopInfoResponse>, t: Throwable) {
                Log.d("getshop", "실패")
            }

            override fun onResponse(
                call: Call<GetShopInfoResponse>,
                response: Response<GetShopInfoResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("getshop", "성공")
                    val temp = response.body()!!.id
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
        builder.setSingleChoiceItems(mycourse, checkedItem) { dialog, which ->
            // user checked an item
        }
        builder.setPositiveButton("확인"){dialog, which ->
            //user clicked OK
        }
        builder.setNegativeButton("취소", null)
        val dialog = builder.create()
        dialog.show()
    }





    /***********연습***************/
    private fun getRecomendInfo(){
        val getReco : Call<GetRecommendListInfo> = networkService.getAllRecomendListInfoResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.88j2Z3_pB_z-xU4AGuYsptIiV9zFdH7bsweI8hR3NS8")

        getReco.enqueue(object: Callback<GetRecommendListInfo>{
            override fun onFailure(call: Call<GetRecommendListInfo>, t: Throwable) {
                Log.d("getReco", "실패")
            }

            override fun onResponse(
                call: Call<GetRecommendListInfo>,
                response: Response<GetRecommendListInfo>
            ) {
                if(response.isSuccessful){
                    Log.d("getReco", "성공")
                    val temp = response.body()!!.shops

                    if(temp.size >0){
                        Log.d("getReco", temp.toString())
                    }
                }
            }

        })
    }
}

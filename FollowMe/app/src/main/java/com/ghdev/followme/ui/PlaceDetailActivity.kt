package com.ghdev.followme.ui

import android.animation.Animator
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.test.MyCourseListInfo
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.home.HomeFragment.Companion.PLACE_INFO
import com.ghdev.followme.data.test.ReviewInfo
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.dialog_review_insert.*

class PlaceDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var placeReivewRecyclerViewAdapter: PlaceReivewRecyclerViewAdapter
    var reviewList : ArrayList<ReviewInfo> = ArrayList()

    //찜하기 여부
    var mypick : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        //Recyclerview에서 받은 정보 표시
        val place_info = intent.getParcelableExtra<PlaceInfo>(PLACE_INFO)
        tv_place_detail_title.text = place_info.name
        tv_place_detail_name.text = place_info.address
        Glide.with(this).load(place_info.img).into(iv_place_detail_main)

        init()
        PlaceReviewRecycler()
    }

    private fun init(){
        btn_place_detail_add_review.setOnClickListener(this)
        btn_place_detail_add_mypick.setOnClickListener(this)
        btn_place_detail_add_mycourse.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when(v){
            btn_place_detail_add_review ->{
                ReviewDialogCreate()
            }

            btn_place_detail_add_mypick ->{
                ClickMypick()
            }

            btn_place_detail_add_mycourse ->{
                MyCourseDialogCreate()
            }
        }
    }

    fun ReviewDialogCreate(){
        val builder = AlertDialog.Builder(this)
        val dv = layoutInflater.inflate(R.layout.dialog_review_insert, null)
        builder.setView(dv).show()
    }

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
}

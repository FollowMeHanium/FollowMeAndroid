package com.ghdev.followme.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.home.HomeFragment.Companion.PLACE_INFO
import com.ghdev.followme.data.test.ReviewInfo
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.dialog_review_insert.*

class PlaceDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var placeReivewRecyclerViewAdapter: PlaceReivewRecyclerViewAdapter
    var reviewList : ArrayList<ReviewInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        //Recyclerview에서 받은 정보 표시
        val place_info = intent.getParcelableExtra<PlaceInfo>(PLACE_INFO)
        tv_place_detail_title.text = place_info.name
        tv_place_detail_name.text = place_info.address
        Glide.with(this).load(place_info.img).into(iv_place_detail_main)

        init()
        PlaceReviewRcycler()
    }

    private fun init(){
        btn_place_detail_add_review.setOnClickListener(this)
    }

    fun PlaceReviewRcycler(){
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
                ReviewDialogCreate(v!!)
            }
        }
    }

    fun ReviewDialogCreate(view: View){
        /*val dialog: ReviewDialogFragment = ReviewDialogFragment().getInstance()
        //val fm = supportFragmentManager.beginTransaction()
        val fm = getFragmentManager()
        dialog.show(fm!!, "TAG_DIALOG_EVENT")*/

        val builder = AlertDialog.Builder(this)
        val dv = layoutInflater.inflate(R.layout.dialog_review_insert, null)
        builder.setView(dv).show()

    }

}

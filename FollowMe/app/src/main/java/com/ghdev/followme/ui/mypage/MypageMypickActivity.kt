package com.ghdev.followme.ui.mypage

import android.app.admin.DevicePolicyManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.HomeFragment
import com.ghdev.followme.ui.HotPlaceRecyclerViewAdapter
import com.ghdev.followme.ui.PlaceDetailActivity
import kotlinx.android.synthetic.main.activity_mypage_mypick.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_hot_place.*
import kotlinx.android.synthetic.main.item_mypick.*
import okhttp3.internal.notifyAll
import org.jetbrains.anko.verticalLayout

class MypageMypickActivity : AppCompatActivity(), View.OnClickListener{


    companion object{
        val PLACE_INFO = "place_info"
        var isInEditMode = false
        var selectionList: ArrayList<PlaceInfo> = ArrayList()
        fun prepareSelection(position : Int, dataList : ArrayList<PlaceInfo>){
            if(!selectionList.contains(dataList.get(position)))
            {
                //선택된 아이템 리스트에 해당 포지션 추가
                selectionList.add(dataList.get(position))
            }else{
                //선택된 아이템 리스트에 해당 포지션 삭제
                selectionList.remove(dataList.get(position))
            }
        }
    }

    lateinit var myPickPlaceRecyclerViewAdapter: MyPickPlaceRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_mypick)

        init()
        MyPickRecyclerView()
    }

    private fun init(){
        btn_mypick_editmode_false.setOnClickListener(this)
        btn_mypick_editmode_true.setOnClickListener(this)
        btn_mypick_editmode_delete.setOnClickListener(this)

    }

    fun MyPickRecyclerView(){

        var dataList: ArrayList<PlaceInfo> = ArrayList()

        dataList.add(PlaceInfo(R.drawable.img5, "비트포비아", "서울특별시 강남구 역삼1동 824-30"))
        dataList.add(PlaceInfo(R.drawable.img6, "카페 프레도", "서울특별시 강남구 역삼1동"))
        dataList.add(PlaceInfo(R.drawable.img7, "꽃을피우고", "서울특별시 강남구 역삼동"))
        dataList.add(PlaceInfo(R.drawable.img8, "자세", "서울특별시 마포구 서교동"))
        dataList.add(PlaceInfo(R.drawable.img1, "오우 연남점", "서울특별시 마포구 서교동"))
        dataList.add(PlaceInfo(R.drawable.img2, "돈부리", "서울특별시 마포구 서교동"))
        dataList.add(PlaceInfo(R.drawable.img3, "랍스타파티", "서울특별시 마포구 서교동 독막로7길"))
        dataList.add(PlaceInfo(R.drawable.img4, "라공방", "서울특별시 강남구 역삼동 825-20"))

        myPickPlaceRecyclerViewAdapter = MyPickPlaceRecyclerViewAdapter(dataList){PlaceInfo->
            //editmode가 아닐때만 가게 세부 정보 보기
            //editmode일때는 itme 클릭시 삭제만
            if(!isInEditMode){
                val intent = Intent(this, PlaceDetailActivity::class.java)
                intent.putExtra(PLACE_INFO, PlaceInfo)
                startActivity(intent)
            }
        }
        rv_mypick.adapter = myPickPlaceRecyclerViewAdapter
        rv_mypick.layoutManager = GridLayoutManager(this, 2)


    }

    override fun onClick(v: View?) {
        when(v){
            //editmode 전환
            btn_mypick_editmode_false -> {
                //button 나타내기
                btn_mypick_editmode_false.visibility = View.GONE
                btn_mypick_editmode_delete.visibility = View.VISIBLE
                btn_mypick_editmode_true.visibility = View.VISIBLE

                //editmode활성화
                isInEditMode = true
            }

            btn_mypick_editmode_true -> {
                //button 나타내기
                btn_mypick_editmode_true.visibility = View.GONE
                btn_mypick_editmode_delete.visibility = View.GONE
                btn_mypick_editmode_false.visibility = View.VISIBLE

                //editmode 비활성화 및 clear
                isInEditMode = false
                selectionList.clear()
                setContentView(R.layout.activity_mypage_mypick)
            }
            btn_mypick_editmode_delete ->{

            }
        }
    }



}

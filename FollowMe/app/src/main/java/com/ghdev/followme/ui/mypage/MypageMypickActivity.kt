package com.ghdev.followme.ui.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.network.get.GetShopLikeListResponse
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.PlaceDetailActivity
import kotlinx.android.synthetic.main.activity_mypage_mypick.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageMypickActivity : AppCompatActivity(), View.OnClickListener{

    companion object{
        val PLACE_INFO = "place_info"
        //EditMode 구별 위한 변수
        var isInEditMode = false
        //찜 선택된 item list
        var selectionList: ArrayList<Shop> = ArrayList()
        //찜 선택 취소한 item의 id list -> 서버에 통신용
        var cancleList: ArrayList<Int> = ArrayList()
    }

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy{
        ApplicationController.instance.prefs
    }


    lateinit var myPickPlaceRecyclerViewAdapter: MyPickPlaceRecyclerViewAdapter
    var dataList: ArrayList<Shop> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_mypick)

        init()
        MyPickRecyclerView()
        getShopLikeListResponse()
    }

    private fun init(){
        btn_mypick_editmode_false.setOnClickListener(this)
        btn_mypick_editmode_true.setOnClickListener(this)
        btn_mypick_editmode_delete.setOnClickListener(this)
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
                //레이아웃 초기화
                rv_mypick.removeAllViews()
            }

            btn_mypick_editmode_delete ->{
                if(isInEditMode){
                    removeData(selectionList)
                    selectionList.clear()
                    rv_mypick.removeAllViews()
                }

            }
        }
    }

    fun MyPickRecyclerView(){
        myPickPlaceRecyclerViewAdapter =
            MyPickPlaceRecyclerViewAdapter(dataList){Shop ->
                //Edit모드가 아니면 -> detailView로 넘어가기
                if(!isInEditMode){
                    val intent = Intent(this, PlaceDetailActivity::class.java)
                    intent.putExtra("place_idx", Shop.id)
                    startActivity(intent)
                }else{
                 //Edit모드라면 -> 삭제할 리스트 정하기
                    prepareSelection(Shop)
                    Log.d("clicked datalist: ", selectionList.toString())
                }
            }
        rv_mypick.adapter = myPickPlaceRecyclerViewAdapter
        rv_mypick.layoutManager = GridLayoutManager(this, 2)


    }

    /*************************Shop Like List 통신********************/

    private fun getShopLikeListResponse(){
       /* val getshop : Call<GetShopLikeListResponse> = networkService.getShopLikeListResponse(sharedPrefs.getString(
            PreferenceHelper.PREFS_KEY_ACCESS,"0"), 8)

        */

        val getshop : Call<GetShopLikeListResponse> = networkService.getShopLikeListResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxfQ.ldsBBxz_tUoqEMKD39ugh1rW32kR6tNLfQ-j7nLKi5Y")

        Log.d("getlike", "동작")

        getshop.enqueue(object : Callback<GetShopLikeListResponse>{
            override fun onFailure(call: Call<GetShopLikeListResponse>, t: Throwable) {
                Log.d("getlike", "실패")
            }

            override fun onResponse(
                call: Call<GetShopLikeListResponse>,
                response: Response<GetShopLikeListResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("getlike", "성공")
                    val temp : ArrayList<Shop> = response.body()!!.shops

                    if(temp.size > 0){
                        val position = myPickPlaceRecyclerViewAdapter.itemCount
                        myPickPlaceRecyclerViewAdapter.dataList.addAll(temp)
                        myPickPlaceRecyclerViewAdapter.notifyItemInserted(position)
                    }
                }

            }

        })
    }

    //뒤로가기 버튼
    override fun onBackPressed() {
        //만약 isInEditMode가 true인 상태에서 백버튼 클릭시
        if(isInEditMode){
            btn_mypick_editmode_true.visibility = View.GONE
            btn_mypick_editmode_delete.visibility = View.GONE
            btn_mypick_editmode_false.visibility = View.VISIBLE

            isInEditMode = false
            selectionList.clear()
            rv_mypick.removeAllViews()
        }else{
            //이전 Activity실행
            super.onBackPressed()
        }

    }

    fun prepareSelection(dataList : Shop){
        if(!selectionList.contains(dataList))
        {
            //선택된 아이템 리스트에 해당 포지션 추가
            selectionList.add(dataList)
        }else{
            //선택된 아이템 리스트에 해당 포지션 삭제
            selectionList.remove(dataList)
        }
    }


    fun removeData(selectionList: ArrayList<Shop>){
        for(i in selectionList){
            dataList.remove(i)
            cancleList.add(i.id)
            rv_mypick.adapter?.notifyDataSetChanged()
        }
        //마지막으로 좋아요 list보내기.. -> 좋아요 취소된 배열 모아서 보내기?

    }

}

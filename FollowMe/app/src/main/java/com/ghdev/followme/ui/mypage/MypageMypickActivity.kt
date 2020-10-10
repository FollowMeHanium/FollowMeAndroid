package com.ghdev.followme.ui.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.data.PostCodeAndMessageResponse
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.get.GetShopLikeListResponse
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.PlaceDetailActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_mypage_mypick.*
import kotlinx.android.synthetic.main.activity_place_detail.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

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
        val getshop : Call<GetShopLikeListResponse> = networkService.getShopLikeListResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"))
        Log.d("getlike", "동작")

        getshop.enqueue(object : Callback<GetShopLikeListResponse>{
            override fun onFailure(call: Call<GetShopLikeListResponse>, t: Throwable) {
                Log.d("getlike", "실패" + t.message)
            }

            override fun onResponse(
                call: Call<GetShopLikeListResponse>,
                response: Response<GetShopLikeListResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("getlike", "성공")
                    val temp : ArrayList<Shop> = response.body()!!.shops
                    Log.d("getlike", temp.toString())
                    if(temp.size > 0){
                        Log.d("getlike", "temp는 0이 아님!")
                        tv_mypick_no_sign.visibility = View.GONE
                        val position = myPickPlaceRecyclerViewAdapter.itemCount
                        myPickPlaceRecyclerViewAdapter.dataList.addAll(temp)
                        myPickPlaceRecyclerViewAdapter.notifyItemInserted(position)
                    }else{
                        Log.d("getlike", "null값입니다.")
                        tv_mypick_no_sign.visibility = View.VISIBLE
                    }
                }
            }
        })
        Log.d("getlike", "완료")
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
        val sb = StringBuilder()
        for(i in selectionList){
            dataList.remove(i)
            cancleList.add(i.id)
            sb.append(",").append(i.id)
            rv_mypick.adapter?.notifyDataSetChanged()
        }
        val temp : String = cancleList.toString().replace("[", "")
        val input_cancleList : String = temp.replace("]","")

        //마지막으로 좋아요 list보내기.. -> 좋아요 취소된 배열 모아서 보내기?
        postShopUnLikeResponse(cancleList)
        cancleList.clear()

    }

    private fun postShopUnLikeResponse(input_cancleList : ArrayList<Int>) {

        var jsonObject = JSONObject()
        var jsonArray = JSONArray()

        for(j in input_cancleList){
            jsonArray.put(j)
        }

        jsonObject.put("id", jsonArray)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postShopUnLikeResponse : Call<PostCodeAndMessageResponse> =
            networkService.postShopUnLikeResponse(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS, "0"), gsonObject)

        postShopUnLikeResponse.enqueue(object : Callback<PostCodeAndMessageResponse>{
            override fun onFailure(call: Call<PostCodeAndMessageResponse>, t: Throwable) {
                Log.e("like_un_my: ", t.toString())
            }

            override fun onResponse(
                call: Call<PostCodeAndMessageResponse>,
                response: Response<PostCodeAndMessageResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("like_un_fmy: ", "성공")

                    if(response.body()!!.code == 200){
                        Log.d("like_un_my: ", response.body()!!.message)
                        Log.d("like_un_my: ", response.body()!!.code.toString())
                        toast("찜 리스트에서 삭제되었습니다.")

                    }else{
                        Log.d("like_un_my: ", "fail/" + response.body()!!.message)
                        Log.d("like_un_my", "fail/" + response.body()!!.code.toString())
                    }
                }

            }

        })

    }

}

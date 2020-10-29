package com.ghdev.followme.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghdev.followme.R
import com.ghdev.followme.db.PreferenceHelper
import com.ghdev.followme.network.ApplicationController
import com.ghdev.followme.network.NetworkService
import com.ghdev.followme.network.get.SearchResultResponse
import com.ghdev.followme.network.get.SearchResultResponseItem
import com.ghdev.followme.network.get.ShopDAO
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_search_result.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity(), View.OnClickListener, SearchResultViewAdapter.OnItemClick {

    lateinit var searchResultViewAdapter: SearchResultViewAdapter
    var categoryThemaText = ""
    private var thema_id = -1
    private lateinit var placeLists: MutableList<ShopDAO>
    val YYYYMMDD = "(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])"

    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    private val sharedPrefs by lazy {
        ApplicationController.instance.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        // 값 받기
        tv_search_result.text = intent.getStringExtra("sendData")


        placeLists = ArrayList<ShopDAO>()
        clickInit()
        postSearch()
//      setPlace()
    }

    private fun setRecyclerView() {
        var searchDataList: ArrayList<SearchResultResponseItem> = ArrayList()

        searchResultViewAdapter = SearchResultViewAdapter(this, searchDataList)
        rv_shop_result.adapter = searchResultViewAdapter
        rv_shop_result.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

//    private fun setEditTextSearch() {
//
//        et_add_course.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if(et_add_course.text != null || et_add_course.toString() != "")
//                    postSearch()
//                else
//                    rv_search_place.visibility = View.INVISIBLE
//            }
//        })
//    }

    //검색통신
    //private 동시 선언 불가?
    private fun postSearch() {
        val query: String = tv_search_result.text.toString()

        val jsonObject = JSONObject()
        jsonObject.put("from", 0)
        jsonObject.put("query", query)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postSearchResponse: Call<SearchResultResponse> =
            networkService.postSeach(sharedPrefs.getString(PreferenceHelper.PREFS_KEY_ACCESS,"0"), gsonObject)
        postSearchResponse.enqueue(object : Callback<SearchResultResponse> {

            //통신 실패 시 수행되는 메소드
            override fun onFailure(call: Call<SearchResultResponse>, t: Throwable) {
                Log.e("검색 통신 fail", t.toString())
            }

            //통신 성공 시 수행되는 메소드
            override fun onResponse(
                call: Call<SearchResultResponse>,
                response: Response<SearchResultResponse>
            ) {
                if (response.isSuccessful) {
                    val temp: ArrayList<SearchResultResponseItem> = response.body()!!
                    if (temp.size > 0) {
                        rv_shop_result.visibility = View.VISIBLE
                        setRecyclerView()
                        val position = searchResultViewAdapter.itemCount
                        searchResultViewAdapter.dataList.addAll(temp)
                        searchResultViewAdapter.notifyItemChanged(position)
                        searchResultViewAdapter.setOnItemClickListener(this@SearchResultActivity)
                    }
                }
            }
        })
    }

    private fun clickInit() {
        btn_search_back.setOnClickListener(this)
        btn_search_search.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!) {
            btn_search_back -> {
                finish()
            }

            btn_search_search -> {
                val intent = Intent(this@SearchResultActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onClick(id: Int, shopname: String) {
        TODO("Not yet implemented")
    }
}

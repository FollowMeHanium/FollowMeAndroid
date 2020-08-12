package com.ghdev.followme.ui.mypage

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.ui.mypage.MypageMypickActivity.Companion.isInEditMode
import com.ghdev.followme.ui.mypage.MypageMypickActivity.Companion.selectionList
import kotlinx.coroutines.selects.select

class MyPickPlaceRecyclerViewAdapter (
    val dataList: ArrayList<PlaceInfo>,
    val dataListClick: (PlaceInfo) -> Unit)
    : RecyclerView.Adapter<MyPickPlaceRecyclerViewAdapter.Holder>() {


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): MyPickPlaceRecyclerViewAdapter.Holder {

        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_mypick, viewGroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyPickPlaceRecyclerViewAdapter.Holder, position: Int) {
        val info: PlaceInfo = dataList[position]

        holder.placename.text = info.name
        holder.address.text = info.address
        //holder.star.rating = dataList[position].star.toFloat()
        Glide.with(holder.itemView.context).load(info.img).into(holder.imgurl)

        holder.container.setOnClickListener {
                dataListClick(info)
            if(isInEditMode){
                if(selectionList.contains(info)){
                    holder.container_checked.visibility = View.VISIBLE
                }else{
                    holder.container_checked.visibility = View.GONE
                }
            }

        }


    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgurl = itemView.findViewById(R.id.iv_mypick_item_img) as ImageView
        var placename = itemView.findViewById(R.id.tv_mypick_item_title) as TextView
        //var star = itemView.findViewById(R.id.rb_star_mycourse_item) as RatingBar
        var address = itemView.findViewById(R.id.tv_mypick_item_address) as TextView
        var container = itemView.findViewById(R.id.cl_mypick_container) as ConstraintLayout

        //btn
        var btn_unchecked = itemView.findViewById(R.id.btn_mypick_editmode_unchecked) as ImageView
        var btn_checked = itemView.findViewById(R.id.btn_mypick_editmode_checked) as ImageView

        //선택시 itme 빨간 배경
        var container_checked = itemView.findViewById(R.id.iv_mypick_container_checked_item) as ImageView

    }

    fun removeData(list: ArrayList<PlaceInfo>){
        for(i in list){
            dataList.remove(i)
            notifyDataSetChanged()
        }
    }
}


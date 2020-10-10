package com.ghdev.followme.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.mypage.MypageMypickActivity.Companion.isInEditMode
import com.ghdev.followme.ui.mypage.MypageMypickActivity.Companion.selectionList

class MyPickPlaceRecyclerViewAdapter (
    val dataList: ArrayList<Shop>,
    val dataListClick : (Shop) -> Unit)
    : RecyclerView.Adapter<MyPickPlaceRecyclerViewAdapter.Holder>() {

    val url = "http://3.15.22.4:3005"

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
        val info: Shop = dataList[position]

        holder.placename.text = info.shopname
        holder.address.text = info.address
        holder.star.rating = info.grade_avg.toFloat()


        if(info.main_photo.startsWith("h"))
            Glide.with(holder.itemView.context).load(info.main_photo).into(holder.imgurl)
        else if(info.main_photo == null)
            holder.imgurl.setBackgroundResource(R.drawable.ic_restaurant_menu_black_24dp)
        else
        Glide.with(holder.itemView.context).load(url + info.main_photo).into(holder.imgurl)

        holder.container.setOnClickListener {

            //MypickActivity에서 직접 Activitiy전환 및 datalist 수정
            dataListClick(info)

            //edit모드일 때 -> 클릭시 선택된 item 배경 변경
            if(isInEditMode){
                if(selectionList.contains(info)){
                    holder.container_checked.visibility = View.VISIBLE
                }else{
                    holder.container_checked.visibility = View.GONE
                }
            }else{

            }

        }


    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgurl = itemView.findViewById(R.id.iv_mypick_item_img) as ImageView
        var placename = itemView.findViewById(R.id.tv_mypick_item_title) as TextView
        var star = itemView.findViewById(R.id.rb_star_mypick_item) as RatingBar
        var address = itemView.findViewById(R.id.tv_mypick_item_address) as TextView
        var container = itemView.findViewById(R.id.cl_mypick_container) as ConstraintLayout

        //btn
        var btn_unchecked = itemView.findViewById(R.id.btn_mypick_editmode_unchecked) as ImageView
        var btn_checked = itemView.findViewById(R.id.btn_mypick_editmode_checked) as ImageView

        //선택시 itme 빨간 배경
        var container_checked = itemView.findViewById(R.id.iv_mypick_container_checked_item) as ImageView

    }
}


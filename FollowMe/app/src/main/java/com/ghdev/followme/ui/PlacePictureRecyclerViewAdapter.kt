package com.ghdev.followme.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R


class PlacePictureRecyclerViewAdapter (val dataList : ArrayList<String>)
    : RecyclerView.Adapter<PlacePictureRecyclerViewAdapter.Holder>(){

    val url = "http://3.15.22.4:3005"

    override fun onCreateViewHolder(
        viewGroup : ViewGroup,
        viewType: Int
    ): PlacePictureRecyclerViewAdapter.Holder {
        val view : View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hot_place_pictures, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val pictures : String? = dataList[position]

        Glide.with(holder.itemView.context).load(url + pictures).into(holder.imgurl)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imgurl = itemView.findViewById(R.id.iv_place_detail_pictures) as ImageView
    }
}
package com.ghdev.followme.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.test.PlaceInfo
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.PlaceDetailActivity

class HotPlaceRecyclerViewAdapter (val ctx : Context, val dataList: ArrayList<Shop>)
    : RecyclerView.Adapter<HotPlaceRecyclerViewAdapter.Holder>() {

    val url = "http://3.15.22.4:3005"

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {

        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hot_place, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val shop : Shop = dataList[position]

        holder.placename.text = shop.shopname
        holder.address.text = shop.address
        holder.star.numStars = shop.grade_avg

        Glide.with(holder.itemView.context).load(url + shop.main_photo).into(holder.imgurl)

        holder.container.setOnClickListener {
            val intent = Intent(ctx, PlaceDetailActivity::class.java)
            intent.putExtra("place_idx", shop.id)
            ctx.startActivity(intent)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgurl = itemView.findViewById(R.id.img_hot_place_item) as ImageView
        var placename = itemView.findViewById(R.id.tv_place_name_item) as TextView
        var address = itemView.findViewById(R.id.tv_place_address_item) as TextView
        var star = itemView.findViewById(R.id.rb_star_hotplace_item) as RatingBar
        var container = itemView.findViewById(R.id.cl_hot_place_container) as ConstraintLayout
    }

}


package com.ghdev.followme.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ghdev.followme.R
import com.ghdev.followme.data.test.ReviewInfo
import com.ghdev.followme.network.get.Review

class PlaceReivewRecyclerViewAdapter(
    val dataList: ArrayList<Review>
) : RecyclerView.Adapter<PlaceReivewRecyclerViewAdapter.Holder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): PlaceReivewRecyclerViewAdapter.Holder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_place_review, viewGroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: PlaceReivewRecyclerViewAdapter.Holder, position: Int) {
        val info : Review = dataList[position]

        holder.user_nick.text = info.nickname
        holder.contents.text = info.review
        holder.rating.rating = info.grade.toFloat()
    }

   inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
       var user_nick = itemView.findViewById(R.id.tv_item_place_review_user_nick) as TextView
       var contents = itemView.findViewById(R.id.tv_item_place_review_contents) as TextView
       var rating = itemView.findViewById(R.id.rb_star_place_review) as RatingBar
   }
}
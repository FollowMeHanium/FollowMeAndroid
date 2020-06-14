package com.ghdev.followme.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.data.test.CourseData
import kotlinx.android.synthetic.main.item_mycourse.*

class CourseRecyclerViewAdapter (val dataList: ArrayList<CourseData>)
    : RecyclerView.Adapter<CourseRecyclerViewAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_mycourse, viewGroup, false)



        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.date.text = dataList[position].date
        holder.placename1.text = dataList[position].place[0].name
        holder.placename2.text = dataList[position].place[1].name
        holder.placename3.text = dataList[position].place[2].name
        holder.title.text = dataList[position].title
        holder.star.rating = dataList[position].star.toFloat()

        Glide.with(holder.itemView.context).load(dataList[position].background).into(holder.background)

        //##detailview로 가도록 구현
        holder.container.setOnClickListener {

        }

    }


    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var date = itemView.findViewById(R.id.tv_date_mycourse) as TextView
        var star = itemView.findViewById(R.id.rb_star_mycourse_item) as RatingBar
        var placename1 = itemView.findViewById(R.id.tv_place1_mycourse) as TextView
        var placename2 = itemView.findViewById(R.id.tv_place2_mycourse) as TextView
        var placename3 = itemView.findViewById(R.id.tv_place3_mycourse) as TextView
        var title = itemView.findViewById(R.id.tv_title_mycourse) as TextView
        var background = itemView.findViewById(R.id.iv_course_item_backgroung) as ImageView
        var container = itemView.findViewById(R.id.cl_mycourse_container) as ConstraintLayout

    }
}
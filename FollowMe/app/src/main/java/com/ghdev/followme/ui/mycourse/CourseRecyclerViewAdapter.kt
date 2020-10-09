package com.ghdev.followme.ui.mycourse

import android.content.Context
import android.content.Intent
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
import com.ghdev.followme.network.get.Course

class CourseRecyclerViewAdapter (val ctx : Context, val dataList: ArrayList<Course>)
    : RecyclerView.Adapter<CourseRecyclerViewAdapter.Holder>() {

    val url = "http://3.15.22.4:3005/"

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_mycourse, viewGroup, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        if(dataList[position].dday == null)
            holder.date.text = "null로 들어옵니다."
        else
            holder.date.text = dataList[position].dday.replace("-", ".")

        //##shop 의 크기 예외처리
        //혹시나 서버에서 잘 못 할 수도 있기에에
        holder.placename1.text = dataList[position].shops[0].shopname
        holder.placename2.text = dataList[position].shops[1].shopname
        holder.placename3.text = dataList[position].shops[2].shopname
        holder.title.text = dataList[position].title
        holder.star.rating = (dataList[position].grade_avg/2).toFloat()

        Glide.with(holder.itemView.context).load(url + dataList[position].main_photo).into(holder.background)

        //##detailview로 가도록 구현
        holder.container.setOnClickListener {
            val intent = Intent(ctx, MycourseDetailActivity::class.java)
            intent.putExtra("course_idx", dataList[position].id)

            if(dataList[position].user_nickname == null || dataList[position].user_nickname == "")
                intent.putExtra("user_nickname", "nickname")
            else
                intent.putExtra("user_nickname", dataList[position].user_nickname)
            ctx.startActivity(intent)
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
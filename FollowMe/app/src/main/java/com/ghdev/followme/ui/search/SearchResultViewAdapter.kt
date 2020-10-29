package com.ghdev.followme.ui.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ghdev.followme.R
import com.ghdev.followme.network.get.SearchResultResponseItem
import com.ghdev.followme.network.get.Shop
import com.ghdev.followme.ui.PlaceDetailActivity
import kotlinx.android.synthetic.main.activity_place_detail.*

class SearchResultViewAdapter (
    val ctx: Context,
    val dataList: ArrayList<SearchResultResponseItem>
) : RecyclerView.Adapter<SearchResultViewAdapter.Holder>() {

    val url = "http://3.15.22.4:3005/"

    private var mCallback : OnItemClick? = null;

    fun setOnItemClickListener(listener: OnItemClick) {
        this.mCallback = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewAdapter.Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_place, parent, false)
        return Holder(view)
    }

//    override fun getItemCount(): Int = dataList.size
    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val shop : SearchResultResponseItem = dataList[position]

        // 더미데이터 없애기
        if(dataList[position].shopname == "") {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.shopname.text = dataList[position].shopname
        }

        // 가게 이미지
        if(dataList[position].photo.startsWith("h"))
            Glide.with(holder.itemView.context).load(dataList[position].photo).into(holder.img)
        else
            Glide.with(holder.itemView.context).load(url + dataList[position].photo).into(holder.img)

        // Glide.with(holder.itemView.context).load(url + dataList[position].photo).into(holder.img)

        holder.container.setOnClickListener {
            //R.layout.activity_mycourse_add.visibility = View.VISIBLE
            //val intent = Intent(ctx, MycourseDetailActivity::class.java)
            //intent.putExtra("course_idx", dataList[position].id)
            //ctx.startActivity(intent)

            //클릭과 동시에 recyclerview가 없어져야되고, item이 reset, editText에 있는 글들도 reset되어야 함.
            mCallback?.onClick(1, dataList[position].shopname)
        }

        holder.address.text = dataList[position].address
        holder.grade.text = "★ " + (dataList[position].grade_avg/2).toString()

        holder.container.setOnClickListener {
            val intent = Intent(ctx, PlaceDetailActivity::class.java)
            intent.putExtra("place_idx", shop.id)
            ctx.startActivity(intent)
        }
    }



    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.findViewById(R.id.search_place_img) as ImageView
        var shopname = itemView.findViewById(R.id.search_place_title) as TextView
        var address = itemView.findViewById(R.id.search_place_address) as TextView
        var grade = itemView.findViewById(R.id.search_place_score) as TextView
        var container = itemView.findViewById(R.id.cl_search_result_item) as ConstraintLayout
    }

    public interface OnItemClick {
        fun onClick(id : Int, shopname : String)
    }
}
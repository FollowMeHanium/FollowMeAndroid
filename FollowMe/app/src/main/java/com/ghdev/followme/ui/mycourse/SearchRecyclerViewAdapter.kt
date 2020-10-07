package com.ghdev.followme.ui.mycourse

import android.content.Context
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

class SearchRecyclerViewAdapter(
    val ctx: Context,
    val dataList: ArrayList<SearchResultResponseItem>
) : RecyclerView.Adapter<SearchRecyclerViewAdapter.Holder>() {

    val url = "http://3.15.22.4:3005/"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRecyclerViewAdapter.Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: SearchRecyclerViewAdapter.Holder, position: Int) {
        holder.shopname.text = dataList[position].shopname
        Glide.with(holder.itemView.context).load(url + dataList[position].photo).into(holder.img)

        holder.container.setOnClickListener {
            //R.layout.activity_mycourse_add.visibility = View.VISIBLE
            //val intent = Intent(ctx, MycourseDetailActivity::class.java)
            //intent.putExtra("course_idx", dataList[position].id)
            //ctx.startActivity(intent)

            //클릭과 동시에 recyclerview가 없어져야되고, item이 reset, editText에 있는 글들도 reset되어야 함.

        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.findViewById(R.id.img_shop) as ImageView
        var shopname = itemView.findViewById(R.id.tv_place_name) as TextView
        var container = itemView.findViewById(R.id.cl_search_item) as ConstraintLayout
    }

}

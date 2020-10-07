package com.ghdev.followme.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ghdev.followme.R
import kotlinx.android.synthetic.main.item_hot_place_menu.view.*


class PlaceMenuGridViewAdapter(private var ctx: Context?, private var dataList: ArrayList<String>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, viewGroup: View?, parent: ViewGroup?): View {
        val menu : String = dataList[position]
        val inflater = ctx!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = inflater.inflate(R.layout.item_hot_place_menu, null)

        view.tv_item_place_menu.text = menu

        return view
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return position
    }

}
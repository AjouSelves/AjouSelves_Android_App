package com.goodsbyus.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.goodsbyus.R

class ListAdapter (val array: Array<String>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.layout_listview, null)

        val check = view.findViewById<TextView>(R.id.title)
        check.text = array[position]


        return view
    }

    override fun getItem(position: Int): Any {

        return array[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {

        return array.size
    }

}
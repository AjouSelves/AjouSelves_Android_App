package com.goodsbyus.viewPager

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.goodsbyus.R
import com.bumptech.glide.Glide


class ViewPagerAdapter(imageList: List<String>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    var item = imageList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(item)
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)){

        fun bind(_list: List<String>) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.imageView)
                val newUrl = "http://44.202.49.100:3000$_list"
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            }

    }
}
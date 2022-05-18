package com.goodsbyus.viewPager

import android.util.Log
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
            val plus=_list[adapterPosition]
            val newUrl = "http://52.206.105.200:3000$plus"

            Log.d("test", newUrl)

            Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }

    }
}
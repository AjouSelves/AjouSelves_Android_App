package com.goodsbyus

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ListViewAdapter(val goodsList: List<GoodsModel>) : RecyclerView.Adapter<ListViewAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: GoodsModel) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            Glide.with(itemView).load(_list.url).placeholder(R.drawable.ic_launcher_foreground).
            override(80,80).into(imageView)
            itemView.findViewById<TextView>(R.id.title).text = _list.title
            itemView.findViewById<TextView>(R.id.nickname).text = _list.id
        }
    }
    override fun getItemCount(): Int = goodsList.size

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        )
    }
    private var activity: SecondActivity? = null
    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(goodsList[position])
        holder.itemView.setOnClickListener{
            itemClickListner.onClick(it,position)
        }


        /*val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 600
        holder.itemView.requestLayout()*/
    }

    interface ItemClickListener{
        fun onClick(view: View,position: Int)
    }
    //를릭 리스너
    private lateinit var itemClickListner: ItemClickListener
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}
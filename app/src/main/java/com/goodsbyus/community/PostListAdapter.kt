package com.goodsbyus.community


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goodsbyus.R
import com.goodsbyus.datas.PostList


class PostListAdapter internal constructor(val postList: List<PostList>)
    : RecyclerView.Adapter<PostListAdapter.ListViewHolder>(), Filterable{


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: PostList) {

            itemView.findViewById<TextView>(R.id.iv_title).text = _list.title
            itemView.findViewById<TextView>(R.id.iv_explained).text= _list.title


            val ran=IntRange(0,9)
            itemView.findViewById<TextView>(R.id.iv_created).text = _list.created_at.slice(ran)
        }
    }
    override fun getItemCount(): Int = postList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(postList[position])
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

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }


}
package com.goodsbyus.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.goodsbyus.R
import com.goodsbyus.datas.ItemGetModel
import java.time.format.DateTimeFormatter


class ListViewAdapter(val goodsList: List<ItemGetModel>) : RecyclerView.Adapter<ListViewAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: ItemGetModel) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            if(_list.url!=null) {
                val newUrl = "http://52.206.105.200:3000" + _list.url
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .override(150, 150).into(imageView)
            }
            itemView.findViewById<TextView>(R.id.iv_title).text = _list.title
            itemView.findViewById<TextView>(R.id.iv_category).text = _list.category

            var progress : Double=0.0
            if(_list.min_num!=0){
                progress=_list.cur_num.toDouble()/_list.min_num.toDouble()*100
            }

            itemView.findViewById<TextView>(R.id.iv_progressView).text = String.format("%.1f %% 달성 !", progress)

            val ran=IntRange(0,9)
            itemView.findViewById<TextView>(R.id.iv_created).text = _list.created_at.slice(ran)
        }
    }
    override fun getItemCount(): Int = goodsList.size

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
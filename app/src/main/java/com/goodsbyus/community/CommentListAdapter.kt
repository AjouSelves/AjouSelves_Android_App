package com.goodsbyus.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.goodsbyus.R
import com.goodsbyus.datas.PostDetail

class CommentListAdapter(private val commentsList: List<PostDetail>) : RecyclerView.Adapter<CommentListAdapter.ListViewHolder>(){


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: PostDetail) {

            itemView.findViewById<TextView>(R.id.iv_title).text ="익명"
            itemView.findViewById<TextView>(R.id.iv_explained).text= _list.comments


            val ran=IntRange(0,9)
            itemView.findViewById<TextView>(R.id.iv_created).text = _list.created_at.slice(ran)
        }
    }
    override fun getItemCount(): Int = commentsList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(commentsList[position])

        /*val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 600
        holder.itemView.requestLayout()*/
    }

}
package com.goodsbyus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ColorAdapter(private val colorList: List<ColorModel>) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(color: ColorModel) {
            itemView.findViewById<ImageView>(R.id.iv_color).setColorFilter(color.color)
            itemView.findViewById<TextView>(R.id.product).text = color.productText
            itemView.findViewById<TextView>(R.id.detail).text = color.detailText

            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "It's so ${color.productText}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getItemCount(): Int = colorList.size

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colorList[position])
    }
}
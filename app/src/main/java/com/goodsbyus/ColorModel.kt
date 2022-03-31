package com.goodsbyus

import android.graphics.Color

data class ColorModel (
    val productText: String,
    val colorString: String,
    val detailText: String) {
    companion object {
        val colorList = listOf(
            ColorModel("red", "#FF0000","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("crimson", "#DC143C","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("firebrick", "#B22222","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("maroon", "#800000","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("darkred", "#8B0000","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("brown", "#A52A2A","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("sienna", "#A0522D","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("saddlebrown", "#8B4513","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("indianred", "#CD5C5C","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("rosybrown", "#BC8F8F","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("lightcoral", "#F08080","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("salmon", "#FA8072","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("darksalmon", "#E9967A","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("coral", "#FF7F50","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("tomato", "#FF6347","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("sandybrown", "#F4A460","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("lightsalmon", "#FFA07A","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("peru", "#CD853F","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("chocolate", "#D2691E","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("orangered", "#FF4500","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("orange", "#FFA500","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("darkorange", "#FF8C00","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("tan", "#D2B48C","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("peachpuff", "#FFDAB9","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("bisque", "#FFE4C4","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("moccasin", "#FFE4B5","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("navajowhite", "#FFDEAD","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("wheat", "#F5DEB3","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("burlywood", "#DEB887","내가 진짜 좋아하는 Goods by Us"),
            ColorModel("darkgoldenrod", "#B8860B","내가 진짜 좋아하는 Goods by Us")
        )
    }
    val color get() = Color.parseColor(colorString)
}





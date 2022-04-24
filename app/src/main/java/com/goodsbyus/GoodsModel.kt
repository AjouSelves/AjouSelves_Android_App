package com.goodsbyus

import android.graphics.Color
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class GoodsModel (
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String) {
    companion object {
        val goodsList = listOf(
            GoodsModel("test_id1", "Test title1","https://s3.marpple.co/files/u_1150555/2021/12/original/31eca686f0dd678adbd12faf5b1d5f693fb405791.jpg"),
            GoodsModel("test_id2", "Test title2","https://t1.daumcdn.net/cfile/tistory/9997C03E5C616C2720"),
            GoodsModel("test_id3", "Test title3","https://static.wixstatic.com/media/20259e_6e1bc381681c4f21a2415ea4fc5c2e6e~mv2.png/v1/crop/x_0,y_62,w_3456,h_4608/fill/w_272,h_300,al_c,usm_0.66_1.00_0.01,enc_auto/KakaoTalk_Photo_2021-09-01-17-28-22.png"),
            GoodsModel("test_id4", "Test title4","https://cdn.imweb.me/upload/S20200830244f269ed848e/656eb6ed91f12.jpg"),
            GoodsModel("test_id5", "Test title5","https://file.mk.co.kr/meet/neds/2020/11/image_readtop_2020_1174561_16054915004432908.jpg"),
            GoodsModel("test_id6", "Test title6","https://cdn.econovill.com/news/photo/202106/535189_445017_4134.jpg"),
            GoodsModel("test_id7", "Test title7","https://cdnweb01.wikitree.co.kr/webdata/editor/202010/24/img_20201024183026_60837f89.webp"),
            GoodsModel("test_id8", "Test title8","https://www.travelnbike.com/news/photo/202009/89970_176024_4744.jpg"))
    }
}





package com.goodsbyus.mypage.qr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.goodsbyus.R
import com.goodsbyus.datas.GetPay
import com.goodsbyus.datas.MyGoods
import com.goodsbyus.mypage.MyGoodsInfo
import com.goodsbyus.mypage.adapter.MyFundingAdapter
import com.goodsbyus.retrofit2.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayQr : AppCompatActivity() {

    private fun getExtra(): Int{
        return intent.getIntExtra("projid",0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_qr)

        val projid=getExtra()

        RetrofitBuilder.api.getPayLink(projid).enqueue(object :
            Callback<GetPay> {
            override fun onResponse(
                call: Call<GetPay>,
                response: Response<GetPay>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                    if(data.paylink!=null) {
                        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.paylink))
                        startActivity(intent)
                        finish()
                    }


                    Toast.makeText(this@PayQr, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetPay>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(this@PayQr, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
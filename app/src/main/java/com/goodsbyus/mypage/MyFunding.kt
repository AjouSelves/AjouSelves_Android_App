package com.goodsbyus.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.goodsbyus.R
import com.goodsbyus.databinding.ActivityMyFundingBinding
import com.goodsbyus.datas.MyGoods
import com.goodsbyus.mypage.adapter.MyFundingAdapter
import com.goodsbyus.retrofit2.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFunding : AppCompatActivity() {

    private var _binding: ActivityMyFundingBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_funding)

        _binding = ActivityMyFundingBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)	//왼쪽 버튼 사용설정(기본은 뒤로가기)
        supportActionBar!!.setTitle("내가 펀딩한 프로젝트")


        getData()

        val view=binding.root
        setContentView(view)
    }

    private fun getData(){
        RetrofitBuilder.api.getMyFunding().enqueue(object :
            Callback<MyGoods> {
            override fun onResponse(
                call: Call<MyGoods>,
                response: Response<MyGoods>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                    if(data.status=="success") {
                        binding.rvList.adapter = MyFundingAdapter(data.data).apply {
                            setItemClickListener(
                                object : MyFundingAdapter.ItemClickListener {
                                    override fun onClick(view: View, position: Int) {
                                        val projid = goodsList[position].projid

                                        val intent =
                                            Intent(this@MyFunding, MyFundingInfo::class.java)

                                        intent.apply {
                                            this.putExtra("projid", projid) // 데이터 넣기
                                        }
                                        startActivity(intent)
                                    }
                                })
                        }
                    }


                    //Toast.makeText(this@MyFunding, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MyGoods>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@MyFunding, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    /*private fun getData(){
        RetrofitBuilder.api.getMyFunding().enqueue(object :
            Callback<List<MyFundingModel>> {
            override fun onResponse(
                call: Call<List<MyFundingModel>>,
                response: Response<List<MyFundingModel>>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑


                    binding.rvList.adapter = MyFundingAdapter(data).apply{
                        setItemClickListener(
                            object : MyFundingAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val projid=goodsList[position].projid

                                    val intent = Intent(this@MyFunding, GoodsInfo::class.java)

                                    intent.apply {
                                        this.putExtra("projid",projid) // 데이터 넣기
                                    }
                                    startActivity(intent)
                                }
                            })
                    }


                    Toast.makeText(this@MyFunding, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MyFundingModel>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(this@MyFunding, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }*/
}
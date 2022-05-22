package com.goodsbyus.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.goodsbyus.*
import com.goodsbyus.databinding.ActivityGoodsInfoBinding
import com.goodsbyus.datas.DetailModel
import com.goodsbyus.datas.FundingResponse

import com.goodsbyus.retrofit2.RetrofitBuilder
import com.goodsbyus.viewPager.ViewPagerAdapter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodsInfo : AppCompatActivity() {
    private var _binding: ActivityGoodsInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    fun getExtra(): Int{
        return intent.getIntExtra("projid",0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_delete -> {
                //삭제 버튼 눌렀을 때
                Toast.makeText(this@GoodsInfo, "삭제 이벤트 실행", Toast.LENGTH_LONG).show()
                deleteProj()
                true
            }
            R.id.action_rewrite -> {
                //수정 버튼 눌렀을 때
                Toast.makeText(this@GoodsInfo, "수정 이벤트 실행", Toast.LENGTH_LONG).show()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_info)

        _binding = ActivityGoodsInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)	//왼쪽 버튼 사용설정(기본은 뒤로가기)
        supportActionBar!!.setDisplayShowTitleEnabled(false)		//타이틀 안 보이게 설정
        
        val projid=getExtra()


            binding.fundingButton.setOnClickListener {
                Log.d("test", "버튼")
                Log.d("test", "$projid")
                RetrofitBuilder.api.getFunding(projid).enqueue(object :
                    Callback<FundingResponse> {
                    override fun onResponse(
                        call: Call<FundingResponse>,
                        response: Response<FundingResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("test", response.body().toString())
                            var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                            if (data.status == "fail") {
                                //if (data.text == "already joined") {
                                    Toast.makeText(
                                        this@GoodsInfo,
                                        "이미 참여한 펀딩입니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                //}
                            } else {
                                Toast.makeText(this@GoodsInfo, "펀딩 성공!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<FundingResponse>, t: Throwable) {
                        Log.d("test", "실패$t")
                        Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                    }

                })
            }

            RetrofitBuilder.api.getRequest(projid).enqueue(object :
                Callback<List<DetailModel>> {
                override fun onResponse(
                    call: Call<List<DetailModel>>,
                    response: Response<List<DetailModel>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                        val title = data[0].title
                        val nickname = data[0].nickname
                        val category = data[0].category
                        val explained = data[0].explained

                        binding.titleView.text = title
                        binding.nicknameView.text = nickname
                        binding.categoryView.text = category
                        binding.explainedView.text = explained

                        binding.viewPager.adapter =
                            data[0].photos?.let { ViewPagerAdapter(it) } // 어댑터 생성
                        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL



                        Toast.makeText(this@GoodsInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DetailModel>>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })

        val view = binding.root
        setContentView(view)
        }
    private fun deleteProj(){
        
        val projid=getExtra()
        RetrofitBuilder.api.deleteProject(projid).enqueue(object :
            Callback<FundingResponse> {
            override fun onResponse(
                call: Call<FundingResponse>,
                response: Response<FundingResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                    if (data.status == "fail") {
                        if (data.text =="글 작성자만 삭제가 가능합니다.") {
                            Toast.makeText(
                                this@GoodsInfo,
                                data.text,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@GoodsInfo, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                        this@GoodsInfo.onBackPressed()
                    }
                }
            }

            override fun onFailure(call: Call<FundingResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}





package com.goodsbyus.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.goodsbyus.R
import com.goodsbyus.SecondActivity
import com.goodsbyus.databinding.ActivityMyGoodsInfoBinding
import com.goodsbyus.datas.DetailModel
import com.goodsbyus.datas.FundingResponse
import com.goodsbyus.retrofit2.RetrofitBuilder
import com.goodsbyus.viewPager.ViewPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyGoodsInfo : AppCompatActivity() {
    private var _binding: ActivityMyGoodsInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getExtra(): Int{
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
                Toast.makeText(this@MyGoodsInfo, "삭제 이벤트 실행", Toast.LENGTH_LONG).show()
                deleteProj()
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

        _binding = ActivityMyGoodsInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)	//왼쪽 버튼 사용설정(기본은 뒤로가기)


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
                                this@MyGoodsInfo,
                                "이미 참여한 펀딩입니다",
                                Toast.LENGTH_SHORT
                            ).show()
                            //}
                        } else {
                            Toast.makeText(this@MyGoodsInfo, "펀딩 성공!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<FundingResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(this@MyGoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
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
                    val minNum = data[0].min_num
                    val curNum = data[0].cur_num
                    val category = data[0].category
                    val explained = data[0].explained

                    supportActionBar!!.setTitle(title)//타이틀설정 - 툴바

                    binding.titleView.text = title
                    binding.categoryView.text = category
                    binding.explainedView.text = explained
                    binding.minnumView.text= minNum.toString()

                    var progress : Double=0.0
                    if(minNum!=0){
                        progress=curNum.toDouble()/minNum.toDouble()*100
                    }

                    binding.progressView.text = String.format("%.1f%% 달성", progress)

                    binding.viewPager.adapter =
                        data[0].photos?.let { ViewPagerAdapter(it) } // 어댑터 생성
                    binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL



                    Toast.makeText(this@MyGoodsInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DetailModel>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(this@MyGoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
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
                                this@MyGoodsInfo,
                                data.text,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@MyGoodsInfo, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                        this@MyGoodsInfo.onBackPressed()
                        val intent = Intent(this@MyGoodsInfo, SecondActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<FundingResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(this@MyGoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
package com.goodsbyus.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.goodsbyus.R
import com.goodsbyus.databinding.ActivityPostInfoBinding
import com.goodsbyus.datas.*
import com.goodsbyus.retrofit2.RetrofitBuilder
import com.goodsbyus.viewPager.ViewPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostInfo : AppCompatActivity() {
    private var _binding: ActivityPostInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getExtra(): Int {
        return intent.getIntExtra("postid", 0)
    }


    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_info)

        _binding = ActivityPostInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("커뮤니티")//타이틀설정 - 툴바//왼쪽 버튼 사용설정(기본은 뒤로가기)


        getData()


        binding.commentButton.setOnClickListener {
            val input = binding.commentEdit.text.toString().replace("'","""\'""")
            val postid=getExtra()

            val initializeRequest = PostComment(postid=postid, comment=input)
            RetrofitBuilder.api.addComment(initializeRequest).enqueue(object :
                Callback<InitializeResponse> {
                override fun onResponse(
                    call: Call<InitializeResponse>,
                    response: Response<InitializeResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                        if(data.status=="success"){
                            binding.commentEdit.setText("")
                            getData()
                        }
                    }
                }

                override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    //Toast.makeText(this@PostInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })
        }



        val view = binding.root
        setContentView(view)
    }

    private fun getData(){
        val postid=getExtra()
        RetrofitBuilder.api.getPostInfo(postid).enqueue(object :
            Callback<PostDetailModel> {
            override fun onResponse(
                call: Call<PostDetailModel>,
                response: Response<PostDetailModel>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val title = data.data[0].title
                    val created_at = data.data[0].created_at
                    val nickname = data.data[0].nickname
                    val explained = data.data[0].explained


                    binding.titleView.text = title

                    binding.explainedView.text = explained

                    binding.viewPager.adapter =
                        data.data[0].photos?.let { ViewPagerAdapter(it) } // 어댑터 생성
                    binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                    val comments= mutableListOf<PostDetail>()
                    for(i in 1 until data.data.size){
                        comments.add(data.data[i])
                    }

                    val newComments: List<PostDetail> = comments

                    binding.rvList.adapter = CommentListAdapter(newComments)

                    //Toast.makeText(this@PostInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostDetailModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@PostInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
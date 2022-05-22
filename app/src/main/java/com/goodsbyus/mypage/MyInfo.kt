package com.goodsbyus.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.goodsbyus.R

import com.goodsbyus.databinding.ActivityMyInfoBinding
import com.goodsbyus.datas.DetailModel
import com.goodsbyus.datas.UserInfo

import com.goodsbyus.retrofit2.RetrofitBuilder
import com.goodsbyus.viewPager.ViewPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfo : AppCompatActivity() {
    private var _binding: ActivityMyInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_info)

        _binding = ActivityMyInfoBinding.inflate(layoutInflater)

        RetrofitBuilder.api.getUserInfo().enqueue(object :
            Callback<List<UserInfo>> {
            override fun onResponse(
                call: Call<List<UserInfo>>,
                response: Response<List<UserInfo>>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val email = data[0].email
                    val name = data[0].name
                    val phone = data[0].phonenumber
                    val nickname = data[0].nickname
                    val status = data[0].status
                    val birth = data[0].birth
                    val address = data[0].address
                    val account = data[0].account
                    val profile = data[0].profilelink

                    binding.emailView.text = email
                    binding.nameView.text = name
                    binding.phoneView.text = phone
                    binding.nicknameView.text = nickname
                    binding.statusView.text = status
                    binding.birthView.text = birth
                    binding.addressView.text = address
                    binding.accountView.text = account
                    binding.profileView.text = profile

                    Toast.makeText(this@MyInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(this@MyInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })

        val view = binding.root
        setContentView(view)
    }
}
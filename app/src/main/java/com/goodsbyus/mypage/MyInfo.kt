package com.goodsbyus.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.goodsbyus.R

import com.goodsbyus.databinding.ActivityMyInfoBinding

import com.goodsbyus.datas.UserInfo

import com.goodsbyus.retrofit2.RetrofitBuilder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfo : AppCompatActivity() {
    private var _binding: ActivityMyInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_rewrite -> {
                //수정 버튼 눌렀을 때
                Toast.makeText(this@MyInfo, "수정 이벤트 실행", Toast.LENGTH_LONG).show()
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
        setContentView(R.layout.activity_my_info)

        _binding = ActivityMyInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.myToolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)	//왼쪽 버튼 사용설정(기본은 뒤로가기)

        /*RetrofitBuilder.api.getUserInfo().enqueue(object :
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

        })*/

        val view = binding.root
        setContentView(view)
    }
}
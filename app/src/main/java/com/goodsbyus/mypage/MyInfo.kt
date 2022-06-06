package com.goodsbyus.mypage

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.goodsbyus.GlobalApplication
import com.goodsbyus.R
import com.goodsbyus.SecondActivity

import com.goodsbyus.databinding.ActivityMyInfoBinding
import com.goodsbyus.datas.*

import com.goodsbyus.retrofit2.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_info.*

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
                changeUserInfo()
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
        supportActionBar!!.setTitle("내 정보")

        RetrofitBuilder.api.getUserInfo().enqueue(object :
            Callback<UserInfo> {
            override fun onResponse(
                call: Call<UserInfo>,
                response: Response<UserInfo>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val email = data.data[0].email
                    val name = data.data[0].name
                    val phone: String? = data.data[0].phonenumber
                    val nickname: String? = data.data[0].nickname
                    val status: String? = data.data[0].status
                    val birth: String? = data.data[0].birth
                    val address: String? = data.data[0].address
                    val account: String? = data.data[0].account
                    val profile: String? = data.data[0].profilelink

                    val ran=IntRange(0,9)

                    binding.emailView.text = email
                    binding.nameView.text = name
                    if(phone!=null){
                        binding.editPhone.setText(phone)
                    }
                    if(nickname!=null) {
                        binding.editNickname.setText(nickname)
                    }
                    if(birth!=null) {
                        binding.editDate.setText(birth.slice(ran))
                    }
                    if(address!=null) {
                        binding.editPostalAddress.setText(address)
                    }
                    if(account!=null) {
                        binding.editNumber.setText(account)
                    }

                    //Toast.makeText(this@MyInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@MyInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })

        val view = binding.root
        setContentView(view)
    }
    private fun changeUserInfo(){
        val phone = editPhone.text.toString()
        val nickname = editNickname.text.toString()
        val date = editDate.text.toString()
        val address = editPostalAddress.text.toString()
        val number = editNumber.text.toString()
        val password = editPassword.text.toString()
        val passwordCheck = editPasswordCheck.text.toString()

        if(password!=passwordCheck || password.isEmpty()){
            dialog("fail")
        }
        else {
            val initializeRequest = UserPutData(
                password = password, phonenumber = phone, nickname = nickname,
                address = address, account = number
            )

            RetrofitBuilder.api.putUserInfo(initializeRequest).enqueue(object :
                Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        dialog("success")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("test", "실패$t")

                }
            })
        }
    }

    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            Toast.makeText(this@MyInfo, "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
        else if(type.equals("fail")){
            dialog.setTitle("회원정보 수정 실패")
            dialog.setMessage("비밀번호를 확인해 주세요.")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}
package com.goodsbyus.login


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.goodsbyus.*
import com.goodsbyus.datas.LoginInfo
import com.goodsbyus.datas.LoginResponse
import com.goodsbyus.retrofit2.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    val TAG: String = "LoginActivity"


    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context=applicationContext

        sharedPreferences = context.getSharedPreferences("loginInfo", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        // 로그인 버튼
        btn_login.setOnClickListener {

            //editText로부터 입력된 값을 받아온다
            val id = edit_id.text.toString()
            val pw = edit_pw.text.toString()

            val initializeRequest= LoginInfo(
                email=id, password=pw)

            RetrofitBuilder.api.loginRequest(initializeRequest).enqueue(object :
                Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful) {
                        Log.d("test", response.body().toString())

                        var data = response.body()!!

                        editor.putString("email", id)
                        editor.putString("password", pw)
                        editor.commit()
                        /*} else {
                            editor.putString("email", "")
                            editor.putString("password", "")
                            editor.commit()
                        }*/

                        if(data.message=="토큰이 발급되었습니다."){
                            val token=data.token

                            GlobalApplication.prefs.setString("tokens",token)
                            dialog("success")
                        }
                        else{
                            dialog("fail")
                        }

                        //Toast.makeText(context, "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("test", "실패$t")

                    dialog("fail")
                    //Toast.makeText(this, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })


        }

        // 회원가입 버튼
        btn_register.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
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

    override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
        return super.getSharedPreferences(name, mode)
    }

    /*fun saveData(loginEmail :String, password :String){
        val pref = getSharedPreferences("userEmail", MODE_PRIVATE) //shared key 설정
        val edit = pref.edit() // 수정모드
        edit.putString("email", loginEmail) // 값 넣기
        edit.putString("password", password)
        edit.apply() // 적용하기
    }*/
}

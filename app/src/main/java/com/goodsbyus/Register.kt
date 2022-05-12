package com.goodsbyus

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Register : AppCompatActivity() {

    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false
    var emailCheck=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        checkButton.setOnClickListener{
            val email = editTextTextEmailAddress.text.toString()
            val initializeRequest=MailCheck(
                email=email)

            RetrofitBuilder.api.checkRequest(initializeRequest).enqueue(object :
                Callback<InitializeResponse> {
                override fun onResponse(
                    call: Call<InitializeResponse>,
                    response: Response<InitializeResponse>
                ) {
                    if(response.isSuccessful) {
                        Log.d("test", response.body().toString())

                        if(response.body()?.status=="success") {
                            emailCheck=true
                            Toast.makeText(this@Register, "사용 가능한 email 입니다", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            emailCheck=false
                            Toast.makeText(this@Register, "이미 등록된 email 입니다", Toast.LENGTH_SHORT).show()
                        }

                        //Toast.makeText(context, "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    //Toast.makeText(this, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })
        }

        button.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val email = editTextTextEmailAddress.text.toString()
            val pw = editTextTextPassword.text.toString()
            val pw_re = editTextTextPasswordCheck.text.toString()
            val name=editTextTextPersonName.text.toString()
            val phone= editTextPhone.text.toString()
            val nickname=editTextTextNickname.text.toString()
            val birth=editTextDate.text.toString()
            val address=editTextTextPostalAddress.text.toString()
            val account=editTextNumber.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(email.isEmpty() || pw.isEmpty() || pw_re.isEmpty()){
                isExistBlank = true
            }
            else{
                if(pw == pw_re){
                    isPWSame = true
                }
            }

            if(!isExistBlank && isPWSame && emailCheck){

                // 회원가입 성공 토스트 메세지 띄우기
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                val initializeRequest=RegisterInfo(
                    email=email, password=pw, name=name, phonenumber = phone, nickname=nickname,
                    status="재학생",socialtype="local",sex=0,birth=birth,address=address,account=account, profilelink = "kakao/balh")

                RetrofitBuilder.api.registerRequest(initializeRequest).enqueue(object :
                    Callback<InitializeResponse> {
                    override fun onResponse(
                        call: Call<InitializeResponse>,
                        response: Response<InitializeResponse>
                    ) {
                        if(response.isSuccessful) {
                            Log.d("test", response.body().toString())

                            //Toast.makeText(context, "업로드 성공!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                        Log.d("test", "실패$t")
                        //Toast.makeText(this, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                    }

                })

                // 로그인 화면으로 이동
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            else{

                // 상태에 따라 다른 다이얼로그 띄워주기
                if(isExistBlank){   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                }
                else if(!isPWSame){ // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                }
                else if(!emailCheck){
                    dialog("not checked")
                }
            }





        }
    }

    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        // 입력한 비밀번호가 다를 경우
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }
        else if(type.equals("not checked")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("이메일 중복 확인을 해주세요")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

    object RetrofitBuilder {
        var api: API

        init {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://44.202.49.100:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api = retrofit.create(API::class.java)
        }
    }
}


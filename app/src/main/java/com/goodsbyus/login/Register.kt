package com.goodsbyus.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.goodsbyus.*
import com.goodsbyus.datas.EmailResponse
import com.goodsbyus.datas.InitializeResponse
import com.goodsbyus.datas.MailCheck
import com.goodsbyus.datas.RegisterInfo
import com.goodsbyus.retrofit2.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class Register : AppCompatActivity() {

    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false
    var emailCheck=false
    var emailCheckFinal=false
    var tempNumber = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        emailCheckButton.setEndIconOnClickListener{
            val email = editEmailAddress.text.toString()
            val initializeRequest= MailCheck(
                email=email)

            RetrofitBuilder.api.checkRequest(initializeRequest).enqueue(object :
                Callback<EmailResponse> {
                override fun onResponse(
                    call: Call<EmailResponse>,
                    response: Response<EmailResponse>
                ) {
                    if(response.isSuccessful) {
                        Log.d("test", response.body().toString())

                        if(response.body()?.number!=null) {
                            emailCheck=true
                            tempNumber=response.body()!!.number
                            Toast.makeText(this@Register, "사용 가능한 email 입니다", Toast.LENGTH_SHORT).show()
                            checkText.text="인증번호가 전송 되었습니다."
                        }
                        else{
                            emailCheck=false
                            Toast.makeText(this@Register, "이미 등록된 email 입니다", Toast.LENGTH_SHORT).show()
                        }

                        //Toast.makeText(context, "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    //Toast.makeText(this, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })
        }

        numberCheckButton.setEndIconOnClickListener {
            val checkNumber=editCheckNum.text.toString().toInt()
            checkText.text="이메일 인증이 완료되었습니다."
            if(checkNumber==tempNumber){
                emailCheckFinal=true;

            } else{
                false
            }
        }



        saveButton.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val email = editEmailAddress.text.toString()
            val pw = editPassword.text.toString()
            val pw_re = editPasswordCheck.text.toString()
            val name=editPersonName.text.toString()
            val phone= editPhone.text.toString()
            val nickname=editNickname.text.toString()
            val birth=editDate.text.toString()
            val address=editPostalAddress.text.toString()
            val account=editNumber.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(email.isEmpty() || pw.isEmpty() || pw_re.isEmpty() || phone.isEmpty() ||
                    name.isEmpty() || nickname.isEmpty()){
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
                val initializeRequest= RegisterInfo(
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
                else if(!emailCheckFinal){
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
}


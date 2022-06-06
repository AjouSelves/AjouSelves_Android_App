package com.goodsbyus

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.goodsbyus.datas.LoginInfo
import com.goodsbyus.datas.LoginResponse
import com.goodsbyus.login.LoginActivity
import com.goodsbyus.retrofit2.RetrofitBuilder
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        //shared에 있는 'userEmail'이란 데이터를 불러온다는 뜻. 0 대신 MODE_PRIVATE라고 입력하셔도 됩니다.
        val savedEmail =sharedPreferences.getString("email", "")
        val savedPassword =sharedPreferences.getString("password","")

        if(savedEmail!="" && savedPassword!=""){

            val initializeRequest= LoginInfo(email = savedEmail!!, password = savedPassword!!)

            if (initializeRequest != null) {
                RetrofitBuilder.api.loginRequest(initializeRequest).enqueue(object :
                    Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful) {
                            Log.d("test", response.body().toString())

                            var data = response.body()!!

                            if(data.message=="토큰이 발급되었습니다."){
                                val token=data.token

                                GlobalApplication.prefs.setString("tokens",token)
                                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                finish()
                            } else{
                                editor.putString("email", "")
                                editor.putString("password", "")
                                editor.commit()
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("test", "실패$t")
                    }

                })
            }
        }


        // 로그인 정보 확인
        val keyHash = Utility.getKeyHash(this)//onCreate 안에 입력해주자
        Log.d("Hash", keyHash)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                //Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        /*val kakao_login_button = findViewById<ImageButton>(R.id.kakao_login_button) // 카카오 로그인 버튼
        kakao_login_button.setOnClickListener {
            if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }*/

        val login_button = findViewById<Button>(R.id.login_button) // 로그인 버튼
        login_button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
class GlobalApplication : Application() {
    companion object{
        lateinit var prefs : PreferenceUtil
    }
    override fun onCreate() {
        prefs=PreferenceUtil(applicationContext)
        super.onCreate()
        KakaoSdk.init(this, "682513bdd453e2e92707d48a4c3f8c98")
    }
}

class PreferenceUtil(context: Context)
{
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tokens", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String
    {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String)
    {
        prefs.edit().putString(key, str).apply()
    }

}
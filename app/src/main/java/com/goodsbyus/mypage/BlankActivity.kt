package com.goodsbyus.mypage

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.goodsbyus.MainActivity
import com.goodsbyus.R

class BlankActivity : AppCompatActivity() {
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)

        sharedPreferences= getSharedPreferences("loginInfo", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("email", "")
        editor.putString("password", "")
        editor.commit()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }
}
package com.goodsbyus


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.goodsbyus.home.HomeFragment
import com.goodsbyus.mypage.MypageFragment
import com.goodsbyus.setting.SettingFragment
import com.goodsbyus.community.CommunityFragment
import com.goodsbyus.firstScreen.FirstScreenActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SecondActivity : AppCompatActivity() {
    private val frame: ConstraintLayout by lazy { // activity_main의 화면 부분
        findViewById(R.id.body_container)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.bottom_navigation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val intent = Intent(this, FirstScreenActivity::class.java)
        startActivity(intent)

        // 애플리케이션 실행 후 첫 화면 설정
        supportFragmentManager.beginTransaction().add(frame.id, HomeFragment()).commit()

        // 하단 네비게이션 바 클릭 이벤트 설정
        bottomNagivationView.setOnItemSelectedListener {item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_community -> {
                    replaceFragment(CommunityFragment())
                    true
                }
                R.id.nav_setting -> {
                    replaceFragment(SettingFragment())
                    true
                }
                R.id.nav_mypage -> {
                    replaceFragment(MypageFragment())
                    true
                }
                else -> false
            }
        }

    }

    // 화면 전환 구현 메소드
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frame.id, fragment).commit()
    }

    //백 버튼 이벤트
    var backKeyPressedTime : Long=0
    override fun onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime+2500){
            backKeyPressedTime=System.currentTimeMillis()
            return
        }

        else{
            finishAffinity()
        }
    }
}
package com.goodsbyus.mypage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goodsbyus.MainActivity
import com.goodsbyus.SecondActivity
import com.goodsbyus.databinding.FragmentMypageBinding
import com.goodsbyus.login.LoginActivity
import com.kakao.sdk.user.UserApiClient


class MypageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        //val kakao_logout_button = findViewById<Button>(R.id.kakao_logout_button) //로그아웃 버튼



        binding.kakaoLogoutButton.setOnClickListener {
            /*UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(getActivity(), "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(getActivity(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }

            }*/
            sharedPreferences=LoginActivity().getSharedPreferences("loginInfo", 0);
            editor = sharedPreferences.edit()
            editor.putString("email", "")
            editor.putString("password", "")
            editor.commit()
            val intent = Intent(getActivity(), MainActivity::class.java)
            startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.myFundingButton.setOnClickListener {
            val intent = Intent(getActivity(), MyFunding::class.java)
            startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.myInfoButton.setOnClickListener {
            val intent = Intent(getActivity(), MyInfo::class.java)
            startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
        }

        /*val arr = arrayOf("test1","test2")

        listAdapter = ListAdapter(arr)
        binding.listViewMy.adapter = listAdapter*/

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
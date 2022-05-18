package com.goodsbyus.mypage

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.goodsbyus.MainActivity
import com.goodsbyus.databinding.FragmentMypageBinding
import com.kakao.sdk.user.UserApiClient


class MypageFragment : Fragment() {
    lateinit var listAdapter: ListAdapter
    private var _binding: FragmentMypageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        //val kakao_logout_button = findViewById<Button>(R.id.kakao_logout_button) //로그아웃 버튼

        binding.kakaoLogoutButton.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(getActivity(), "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(getActivity(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(getActivity(), MainActivity::class.java)
                startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
            }
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
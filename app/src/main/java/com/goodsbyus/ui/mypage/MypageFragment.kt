package com.goodsbyus.ui.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.goodsbyus.MainActivity
import com.goodsbyus.R
import com.goodsbyus.databinding.FragmentMypageBinding
import com.kakao.sdk.user.UserApiClient

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

     /*lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }*/


   private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mypageViewModel =
            ViewModelProvider(this).get(MypageViewModel::class.java)

        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMypage
        mypageViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
   /*override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ):
           View? { return inflater.inflate(R.layout.fragment_mypage, container, false) }*/


    override fun onStart(){
        super.onStart()

        //val kakao_logout_button = mainActivity.findViewById<Button>(R.id.kakao_logout_button) //로그아웃 버튼

        /*kakao_logout_button.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(mainActivity, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(mainActivity, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(getActivity(), MainActivity::class.java)
                startActivity(intent)
            }
        }

        val nickname = mainActivity.findViewById<TextView>(R.id.nickname) // 로그인 버튼

        UserApiClient.instance.me { user, error ->
            nickname.text = "닉네임: ${user?.kakaoAccount?.profile?.nickname}"
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
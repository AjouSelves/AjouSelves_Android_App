package com.goodsbyus.mypage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.goodsbyus.MainActivity
import com.goodsbyus.SecondActivity
import com.goodsbyus.databinding.FragmentMypageBinding
import com.goodsbyus.datas.UserInfo
import com.goodsbyus.login.LoginActivity
import com.goodsbyus.retrofit2.RetrofitBuilder
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        RetrofitBuilder.api.getUserInfo().enqueue(object :
            Callback<List<UserInfo>> {
            override fun onResponse(
                call: Call<List<UserInfo>>,
                response: Response<List<UserInfo>>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val email = data[0].email
                    val name = data[0].name
                    val phone = data[0].phonenumber
                    val nickname = data[0].nickname
                    val status = data[0].status
                    val birth = data[0].birth
                    val address = data[0].address
                    val account = data[0].account
                    val profile = data[0].profilelink

                    binding.emailView.text = email
                    binding.nameView.text = name
                    /*binding.phoneView.text = phone
                    binding.nicknameView.text = nickname
                    binding.statusView.text = status
                    binding.birthView.text = birth
                    binding.addressView.text = address
                    binding.accountView.text = account
                    binding.profileView.text = profile*/

                    Toast.makeText(activity, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(activity, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })



        binding.kakaoLogoutButton.setOnClickListener {
            /*UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(getActivity(), "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(getActivity(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }

            }*/

            val intent = Intent(getActivity(), BlankActivity::class.java)
            startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.myFundingButton.setOnClickListener {
            val intent = Intent(getActivity(), MyFunding::class.java)
            startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.myProjButton.setOnClickListener {
            val intent = Intent(getActivity(), MyGoods::class.java)
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
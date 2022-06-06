package com.goodsbyus.mypage

import android.content.*
import android.content.Context.MODE_PRIVATE
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.goodsbyus.MainActivity
import com.goodsbyus.SecondActivity
import com.goodsbyus.databinding.FragmentMypageBinding
import com.goodsbyus.datas.FundingResponse
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
            Callback<UserInfo> {
            override fun onResponse(
                call: Call<UserInfo>,
                response: Response<UserInfo>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val email = data.data[0].email
                    val name = data.data[0].name

                    binding.emailView.text = email
                    binding.nameView.text = name

                    //Toast.makeText(activity, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(activity, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
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

        binding.deleteUserButton.setOnClickListener {
            dialog("delete")
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

    private fun deleteAccount(){
        RetrofitBuilder.api.deleteUser().enqueue(object :
            Callback<FundingResponse> {
            override fun onResponse(
                call: Call<FundingResponse>,
                response: Response<FundingResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                    if (data.status == "fail") {
                            Toast.makeText(
                                activity,
                                data.text,
                                Toast.LENGTH_SHORT
                            ).show()
                    } else {
                        Toast.makeText(activity, "회원 탈퇴 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        activity!!.finish()
                    }
                }
            }

            override fun onFailure(call: Call<FundingResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(activity, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun dialog(type: String){
        var dialog = AlertDialog.Builder(requireActivity())

        if(type.equals("delete")){
            dialog.setTitle("회원 탈퇴")
            dialog.setMessage("회원 탈퇴를 진행합니다. (삭제된 데이터는 복구할 수 없습니다.)")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        deleteAccount()
                    DialogInterface.BUTTON_NEGATIVE ->
                        Log.d(ContentValues.TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.setNegativeButton("아니오",dialog_listener)
        dialog.show()
    }
}
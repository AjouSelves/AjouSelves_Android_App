package com.goodsbyus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2

import com.bumptech.glide.Glide
import com.goodsbyus.viewPager.ViewPagerAdapter
import com.goodsbyus.databinding.FragmentGoodsInfoBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodsInfoFragment : Fragment() {
    private var _binding: FragmentGoodsInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoodsInfoBinding.inflate(inflater, container, false)


        binding.fundingButton.setOnClickListener{
            setFragmentResultListener("requestKey") { requestKey, bundle ->
                var projid = bundle.getInt("projid")

                HomeFragment.RetrofitBuilder.api.getFunding(projid).enqueue(object :
                    Callback<FundingResponse> {
                    override fun onResponse(
                        call: Call<FundingResponse>,
                        response: Response<FundingResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("test", response.body().toString())
                            var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                            if (data.status == "fail") {
                                if (data.text == "already joined") {
                                    Toast.makeText(
                                        getActivity(),
                                        "이미 참여한 펀딩입니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(getActivity(), "펀딩 성공!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<FundingResponse>, t: Throwable) {
                        Log.d("test", "실패$t")
                        Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }


        setFragmentResultListener("requestKey") { requestKey, bundle ->
            var projid = bundle.getInt("projid")

            HomeFragment.RetrofitBuilder.api.getRequest(projid).enqueue(object :
                Callback<List<DetailModel>> {
                override fun onResponse(
                    call: Call<List<DetailModel>>,
                    response: Response<List<DetailModel>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                        val title=data[0].title
                        val nickname=data[0].nickname
                        val category=data[0].category
                        val explained=data[0].explained

                        binding.titleView.text=title
                        binding.nicknameView.text=nickname
                        binding.categoryView.text=category
                        binding.explainedView.text=explained

                        binding.viewPager.adapter =
                            data[0].photos?.let { ViewPagerAdapter(it) } // 어댑터 생성
                        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL


                        /*for(i in data[0].photos!!){
                            val newUrl = "http://44.202.49.100:3000$i"
                            //Log.d("test", newUrl)
                            getActivity()?.let {
                                Glide.with(it)
                                    .load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                                    .into(binding.imageView)
                            }
                        }*/

                        Toast.makeText(getActivity(), "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<DetailModel>>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })
        }



        /*setFragmentResultListener("requestKey") { requestKey, bundle ->
            var id_str :String?= bundle.getString("id")
            var url_str = bundle.getString("url")
            var title_str :String?= bundle.getString("title")


            binding.idView.text="id_"
            Glide.with(this)
                .load(url_str)
                .into(binding.imageView)
            binding.titleView.text="title_"
        }*/

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.goodsbyus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goodsbyus.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.myToolbar.inflateMenu(R.menu.sample_menu)

        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.action_plus -> {
                    replaceFragment(PlusFragment())
                    true
                }
                R.id.action_notification -> {
                    // notification
                    replaceFragment(NotificationFragment())
                    true
                }
                else -> false
            }
        }

        binding.rvList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        getData()

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            getData()

        }

        val view = binding.root
        return view
    }

    fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().add(R.id.body_container, fragment)
            .addToBackStack(null).commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    object RetrofitBuilder {
        var api: API

        init {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://44.202.49.100:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api = retrofit.create(API::class.java)
        }
    }
    fun getData(){
        RetrofitBuilder.api.getList().enqueue(object :
            Callback<List<ItemGetModel>> {
            override fun onResponse(
                call: Call<List<ItemGetModel>>,
                response: Response<List<ItemGetModel>>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑



                    binding.rvList.adapter = ListViewAdapter(data).apply{
                        setItemClickListener(
                            object : ListViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val projid=goodsList[position].projid

                                    setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    replaceFragment(GoodsInfoFragment())
                                }
                            })
                    }


                    Toast.makeText(getActivity(), "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemGetModel>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}



package com.goodsbyus.community

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.goodsbyus.R

import com.goodsbyus.databinding.FragmentCommunityBinding

import com.goodsbyus.datas.PostList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.goodsbyus.retrofit2.RetrofitBuilder


class CommunityFragment : Fragment() {



    private var _binding: FragmentCommunityBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)

        binding.myToolbar.inflateMenu(R.menu.sample_menu)

        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                /*R.id.action_search -> {
                    val intent = Intent(context,SearchProject::class.java)

                    val data: List<ItemGetModel> = getItemList

                    Log.d("test",getItemList.toString())

                    /*intent.apply {
                        this.putExtra("dataSize",data.size)
                        Log.d("test",data.size.toString())
                        for(i in data){
                            this.putExtra("data$i",i)
                        }
                    }*/

                    startActivity(intent)
                    true
                }*/
                R.id.action_plus -> {
                    //replaceFragment(PlusFragment())
                    //val intent = Intent(context,AddProject::class.java)

                    //startActivity(intent)
                    true
                }
                /*R.id.action_notification -> {
                    // notification
                    replaceFragment(NotificationFragment())
                    true
                }*/
                else -> false
            }
        }

        binding.rvList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        getData()

        binding.refreshLayout.setOnRefreshListener {
            getData()
            binding.refreshLayout.isRefreshing = false
        }

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        showSampleData(isLoading = true)
        RetrofitBuilder.api.getPost().enqueue(object :
            Callback<List<PostList>> {
            override fun onResponse(
                call: Call<List<PostList>>,
                response: Response<List<PostList>>
            ) {
                if (response.isSuccessful) {
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑


                    binding.rvList.adapter = PostListAdapter(data).apply{
                        setItemClickListener(
                            object : PostListAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    //val projid=postList[position]

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    /*val intent = Intent(context,GoodsInfo::class.java)

                                    intent.apply {
                                        this.putExtra("projid",projid) // 데이터 넣기
                                    }
                                    startActivity(intent)*/

                                    //replaceFragment(GoodsInfoFragment())
                                }
                            })
                    }

                    showSampleData(isLoading = false)
                    Toast.makeText(getActivity(), "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PostList>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun showSampleData(isLoading: Boolean) {
        if (isLoading) {
            binding.sflSample.startShimmer()
            binding.sflSample.visibility = View.VISIBLE
            binding.rvList.visibility = View.GONE
        } else {
            binding.sflSample.stopShimmer()
            binding.sflSample.visibility = View.GONE
            binding.rvList.visibility = View.VISIBLE
        }
    }

}



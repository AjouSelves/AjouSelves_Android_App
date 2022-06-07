package com.goodsbyus.home

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
import com.goodsbyus.SecondActivity
import com.goodsbyus.databinding.FragmentHomeBinding
import com.goodsbyus.datas.ItemGetModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.goodsbyus.retrofit2.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class HomeFragment : Fragment() {



    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var getItemList = mutableListOf<ItemGetModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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
                    val intent = Intent(context,AddProject::class.java)

                    startActivity(intent)
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

        //binding.rvList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        getData()

        binding.refreshLayout.setOnRefreshListener {
            getData()
            binding.refreshLayout.isRefreshing = false
        }

        val view = binding.root
        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().add(R.id.body_container, fragment)
            .addToBackStack(null).commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {
        showSampleData(isLoading = true)
        RetrofitBuilder.api.getList().enqueue(object :
            Callback<List<ItemGetModel>> {
            override fun onResponse(
                call: Call<List<ItemGetModel>>,
                response: Response<List<ItemGetModel>>
            ) {
                if (response.isSuccessful) {
                    getItemList.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                    for (item in data) {
                        getItemList.add(item)
                    }


                    binding.rvList.adapter = ListViewAdapter(data).apply{
                            setItemClickListener(
                                object : ListViewAdapter.ItemClickListener {
                                    override fun onClick(view: View, position: Int) {
                                        val projid=goodsList[position].projid

                                        //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                        val intent = Intent(context,GoodsInfo::class.java)

                                        intent.apply {
                                            this.putExtra("projid",projid) // 데이터 넣기
                                        }
                                        startActivity(intent)

                                        //replaceFragment(GoodsInfoFragment())
                                    }
                                })
                        }

                    showSampleData(isLoading = false)
                    //Toast.makeText(getActivity(), "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemGetModel>>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
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



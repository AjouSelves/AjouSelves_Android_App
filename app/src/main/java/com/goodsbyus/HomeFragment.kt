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
import com.goodsbyus.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

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

                                    /*val title=goodsList[position].title
                                    val state=goodsList[position].state
                                    val category=goodsList[position].category
                                    val min_num=goodsList[position].min_num
                                    val cur_num=goodsList[position].cur_num
                                    val required=goodsList[position].required
                                    val explained=goodsList[position].explained
                                    val nickname=goodsList[position].nickname
                                    val userid=goodsList[position].userid
                                    val profilelink=goodsList[position].profilelink
                                    val photos=goodsList[position].photos

                                    setFragmentResult("requestKey", bundleOf("title" to title))
                                    setFragmentResult("requestKey", bundleOf("state" to state))
                                    setFragmentResult("requestKey", bundleOf("category" to category))
                                    setFragmentResult("requestKey", bundleOf("min_num" to min_num))
                                    setFragmentResult("requestKey", bundleOf("cur_num" to cur_num))
                                    setFragmentResult("requestKey", bundleOf("required" to required))
                                    setFragmentResult("requestKey", bundleOf("explained" to explained))
                                    setFragmentResult("requestKey", bundleOf("nickname" to nickname))
                                    setFragmentResult("requestKey", bundleOf("userid" to userid))
                                    setFragmentResult("requestKey", bundleOf("profilelink" to profilelink))
                                    setFragmentResult("requestKey", bundleOf("photos" to photos))*/

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

        /*binding.rvList.adapter = ListViewAdapter(GoodsModel.goodsList).apply{
            setItemClickListener(
                object : ListViewAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int) {
                        val id_str=goodsList[position].id
                        val title_str=goodsList[position].title
                        val url_str=goodsList[position].url
                        setFragmentResult("requestKey", bundleOf("id" to id_str))
                        setFragmentResult("requestKey", bundleOf("title" to title_str))
                        setFragmentResult("requestKey", bundleOf("url" to url_str))

                        replaceFragment(GoodsInfoFragment())
                    }
                })
        }*/


        val view = binding.root
        return view
    }

    fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.body_container, fragment)
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
}



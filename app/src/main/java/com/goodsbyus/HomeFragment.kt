package com.goodsbyus

import android.content.BroadcastReceiver
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
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Response(json: String) : JSONObject(json) {
    val type: String? = this.optString("type")
    val data = this.optJSONArray("data")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
        ?.map { Foo(it.toString()) } // transforms each JSONObject of the array into Foo
}

class Foo(json: String) : JSONObject(json) {
    val id = this.optInt("id")
    val title: String? = this.optString("title")
}


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

        /*val response=" {\"id\": 1,\"title\":\"accusamus beatae ad facilis cum similique qui sunt\"," +
                "\"url\": \"https://via.placeholder.com/600/92c952\"}"

        val gson= Gson()
        val colorList=gson.fromJson(response,ColorModel::class.java)*/
        /*for(i in 1 until 14) {
            RetrofitBuilder.api.getRequest(i).enqueue(object :
                Callback<ITEM_GET_Model> {
                override fun onResponse(
                    call: Call<ITEM_GET_Model>,
                    response: Response<ITEM_GET_Model>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                        Toast.makeText(getActivity(), "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ITEM_GET_Model>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })
        }*/

        RetrofitBuilder.api.getRequest(2).enqueue(object :
            Callback<List<ItemGetModel>> {
            override fun onResponse(
                call: Call<List<ItemGetModel>>,
                response: Response<List<ItemGetModel>>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Toast.makeText(getActivity(), "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemGetModel>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })

        binding.rvList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        binding.rvList.adapter = ListViewAdapter(GoodsModel.goodsList).apply{
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
        }


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



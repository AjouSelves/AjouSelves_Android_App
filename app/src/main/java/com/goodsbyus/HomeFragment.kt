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
    val api = APIS.create();


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
        RetrofitBuilder.api.getRequest().enqueue(object :
            Callback<ITEM_GET_Model> {
            override fun onResponse(
                call: Call<ITEM_GET_Model>,
                response: Response<ITEM_GET_Model>
            ) {
                if(response.isSuccessful) {
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



    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val viewBinding=
        viewBinding.myToolbar.inflateMenu(R.menu.sample_menu)

        viewBinding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    // Navigate to settings screen
                    true
                }
                R.id.action_menu -> {
                    // Save profile changes
                    true
                }
                R.id.action_notification -> {
                    // Save profile changes
                    true
                }
                else -> false
            }
        }
    }*/

}



package com.goodsbyus.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.goodsbyus.R
import com.goodsbyus.databinding.ActivitySearchProjectBinding
import com.goodsbyus.datas.ItemGetModel
import com.goodsbyus.retrofit2.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProject : AppCompatActivity() {
    //private val data by lazy { intent.getParcelableArrayListExtra<ItemGetModel>("data")!! }
    private lateinit var binding: ActivitySearchProjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_project)

        binding= ActivitySearchProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        var searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                //텍스트 입력/수정시에 호출
                override fun onQueryTextChange(s: String): Boolean {
                    //ListViewAdapter.getFilter().filter(s)
                    //Log.d(TAG, "SearchVies Text is changed : $s")
                    return true
                }
            }

        RetrofitBuilder.api.getList().enqueue(object :
            Callback<List<ItemGetModel>> {
            override fun onResponse(
                call: Call<List<ItemGetModel>>,
                response: Response<List<ItemGetModel>>
            ) {
                if (response.isSuccessful) {

                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑

                    binding.searchView.setOnQueryTextListener(searchViewTextListener)

                    binding.rvList.adapter = ListViewAdapter(data).apply{
                        setItemClickListener(
                            object : ListViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val projid=goodsList[position].projid

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    val intent = Intent(this@SearchProject,GoodsInfo::class.java)

                                    intent.apply {
                                        this.putExtra("projid",projid) // 데이터 넣기
                                    }
                                    startActivity(intent)

                                    //replaceFragment(GoodsInfoFragment())
                                }
                            })
                    }


                    Toast.makeText(this@SearchProject, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemGetModel>>, t: Throwable) {
                Log.d("test", "실패$t")
                Toast.makeText(this@SearchProject, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })

        /*var getItemList = mutableListOf<ItemGetModel>()
        if(intent.hasExtra("dataSize")) {
            val dataSize = intent.getIntExtra("dataSize", 0)
            for(i in 0..dataSize){
                val item=intent.getParcelableExtra<ItemGetModel>("data$i")
                if (item != null) {
                    getItemList.add(item)
                }
            }
        }*/

    }
}



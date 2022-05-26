package com.goodsbyus.community


import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.goodsbyus.*
import com.goodsbyus.databinding.ActivityAddPostBinding
import com.goodsbyus.databinding.ActivityAddProjectBinding
import com.goodsbyus.datas.*
import com.goodsbyus.retrofit2.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_second.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.*
import java.io.*
import java.util.*

class AddPost : AppCompatActivity() {
    private var _binding: ActivityAddPostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        _binding = ActivityAddPostBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("커뮤니티 게시글 작성")

        val view = binding.root
        setContentView(view)


        binding.saveButton.setOnClickListener {

            val inputTitle = binding.et1.text.toString()
            val inputExplain = binding.et2.text.toString()


            val initializeRequest = PostModel(title=inputTitle, explained = inputExplain)

            RetrofitBuilder.api.postRequest(initializeRequest).enqueue(object :
                Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test1", response.body().toString())
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                        Toast.makeText(this@AddPost, "업로드 성공!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddPost,SecondActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()

                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(this@AddPost, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}
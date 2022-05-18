package com.goodsbyus.home

import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.goodsbyus.*
import com.goodsbyus.retrofit2.RetrofitBuilder
import com.goodsbyus.databinding.ActivityAddProjectBinding
import com.goodsbyus.datas.InitializeResponse
import com.goodsbyus.datas.Posts
import com.goodsbyus.retrofit2.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class AddProject : AppCompatActivity() {
    private var _binding: ActivityAddProjectBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        _binding = ActivityAddProjectBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val requestGalleryLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            try{
                val calRatio=calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )

                val resolver: ContentResolver
                resolver=this.contentResolver

                val option=BitmapFactory.Options()
                option.inSampleSize=calRatio

                var inputStream=resolver.openInputStream(it.data!!.data!!)
                val bitmap=BitmapFactory.decodeStream(inputStream,null,option)

                inputStream!!.close()
                inputStream=null
                bitmap?.let{
                    binding.userImageView.setImageBitmap(bitmap)
                } ?:let{
                    Log.d("kkang","bitmap null")
                }
            } catch(e: Exception){
                e.printStackTrace()
            }
        }

        binding.galleryButton.setOnClickListener{
            val intent= Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type="image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.saveButton.setOnClickListener{
            val inputTitle=binding.et1.text.toString()
            val inputExplained=binding.et2.text.toString()
            val inputCategory=binding.et3.text.toString()
            val inputMinnum=binding.et4.text.toString()

            val initializeRequest= Posts(
                1, title = inputTitle, explained = inputExplained, min_num = inputMinnum.toInt(),
                category = inputCategory, required="사이즈")

            RetrofitBuilder.api.initRequest(initializeRequest).enqueue(object :
                Callback<InitializeResponse> {
                override fun onResponse(
                    call: Call<InitializeResponse>,
                    response: Response<InitializeResponse>
                ) {
                    if(response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                        Toast.makeText(this@AddProject, "업로드 성공!", Toast.LENGTH_SHORT).show()


                        this@AddProject.onBackPressed()
                        //removeFragment()
                    }
                }

                override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(this@AddProject, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val resolver: ContentResolver
        resolver=this.contentResolver

        val options= BitmapFactory.Options()
        options.inJustDecodeBounds=true
        try{
            var inputStream=resolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream=null
        } catch(e: Exception){
            e.printStackTrace()
        }

        val (height: Int, width: Int)=options.run{outHeight to outWidth}
        var inSampleSize=1

        if(height>reqHeight || width>reqWidth){
            val halfHeight: Int=height/2
            val halfWidth: Int=width/2

            while(halfHeight/inSampleSize >= reqHeight &&
                halfWidth/inSampleSize >= reqWidth){
                inSampleSize*=3
            }
        }
        return inSampleSize
    }
}


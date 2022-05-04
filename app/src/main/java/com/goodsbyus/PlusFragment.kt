package com.goodsbyus

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.goodsbyus.databinding.FragmentPlusBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class PlusFragment : Fragment() {

    private var _binding: FragmentPlusBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var secondActivity: SecondActivity
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    lateinit var requestContactsLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentPlusBinding.inflate(inflater, container, false)

        val view = binding.root

        secondActivity=context as SecondActivity

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
                resolver=secondActivity.contentResolver

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
            val intent=Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type="image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.saveButton.setOnClickListener{
            val inputTitle=binding.et1.text.toString()
            val inputExplained=binding.et2.text.toString()
            val inputCategory=binding.et3.text.toString()
            val inputMinnum=binding.et4.text.toString()

            val initializeRequest=Posts(
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
                        Toast.makeText(getActivity(), "업로드 성공!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })

        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        secondActivity=context as SecondActivity
        val resolver: ContentResolver
        resolver=secondActivity.contentResolver

        val options=BitmapFactory.Options()
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


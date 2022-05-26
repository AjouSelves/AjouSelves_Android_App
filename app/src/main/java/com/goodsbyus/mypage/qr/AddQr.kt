package com.goodsbyus.mypage.qr

import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.goodsbyus.R
import com.goodsbyus.databinding.ActivityAddQrBinding

import com.goodsbyus.datas.PayLinkModel
import com.goodsbyus.datas.PayResponse
import com.goodsbyus.datas.StateModel
import com.goodsbyus.retrofit2.RetrofitBuilder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddQr : AppCompatActivity() {
    private var _binding: ActivityAddQrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!

    private fun getExtra(): Int{
        return intent.getIntExtra("projid",0)
    }

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
        setContentView(R.layout.activity_add_qr)

        _binding = ActivityAddQrBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("QR 코드 등록")

        val projid=getExtra()
        val view = binding.root
        lateinit var filePath : String
        setContentView(view)

        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            try {
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )

                val resolver: ContentResolver
                resolver = this.contentResolver

                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = resolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

                filePath=getRealPathFromURI(it.data!!.data!!)

                Log.d("test",filePath)

                inputStream!!.close()
                inputStream = null
                bitmap?.let {
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let {
                    Log.d("kkang", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.galleryButton.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.saveButton.setOnClickListener {

            val inputLink = binding.et1.text.toString()


            val initializeRequest = PayLinkModel(paylink = inputLink)

            RetrofitBuilder.api.postPayLink(projid,initializeRequest).enqueue(object :
                Callback<PayResponse> {
                override fun onResponse(
                    call: Call<PayResponse>,
                    response: Response<PayResponse>
                ) {
                    if(response.isSuccessful) {
                        Log.d("test", response.body().toString())
                        var data = response.body() // GsonConverter를 사용해 데이터매핑
                        Toast.makeText(this@AddQr, "업로드 성공1!", Toast.LENGTH_SHORT).show()

                        val initializeRequest= StateModel(state=2)

                        RetrofitBuilder.api.putState(projid,initializeRequest).enqueue(object :
                            Callback<PayResponse> {
                            override fun onResponse(
                                call: Call<PayResponse>,
                                response: Response<PayResponse>
                            ) {
                                if(response.isSuccessful) {
                                    Log.d("test", response.body().toString())
                                    var data = response.body() // GsonConverter를 사용해 데이터매핑
                                    Toast.makeText(this@AddQr, "업로드 성공2!", Toast.LENGTH_SHORT).show()

                                    finish()


                                }
                            }

                            override fun onFailure(call: Call<PayResponse>, t: Throwable) {
                                Log.d("test", "실패$t")
                                Toast.makeText(this@AddQr, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                }

                override fun onFailure(call: Call<PayResponse>, t: Throwable) {
                    Log.d("test", "실패$t")
                    Toast.makeText(this@AddQr, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val resolver: ContentResolver
        resolver = this.contentResolver

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = resolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 3
            }
        }
        return inSampleSize
    }
    private fun getRealPathFromURI(uri: Uri): String {
        var buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path!!
        }
        var columnIndex = 0
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }
}


/*class AddPQr : AppCompatActivity() {
    private var _binding: ActivityAddQrBinding? = null

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
        setContentView(R.layout.activity_add_project)

        _binding = ActivityAddQrBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("QR 코드 등록")

        val view = binding.root
        lateinit var filePath : String
        setContentView(view)

        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            try {
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )

                val resolver: ContentResolver
                resolver = this.contentResolver

                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = resolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

                filePath=getRealPathFromURI(it.data!!.data!!)

                Log.d("test",filePath)

                inputStream!!.close()
                inputStream = null
                bitmap?.let {
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let {
                    Log.d("kkang", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.galleryButton.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.saveButton.setOnClickListener {

            val inputTitle = binding.et1.text.toString()
            val inputExplained = binding.et2.text.toString()
            val inputCategory = binding.et3.text.toString()
            val inputMinnum = binding.et4.text.toString()

            val useridRequest = RequestBody.create(MediaType.parse("text/plain"), "1");
            val titleRequest = RequestBody.create(MediaType.parse("text/plain"), inputTitle);
            val explainedRequest =
                RequestBody.create(MediaType.parse("text/plain"), inputExplained);
            val categoryRequest = RequestBody.create(MediaType.parse("text/plain"), inputCategory);
            val minRequest = RequestBody.create(MediaType.parse("text/plain"), inputMinnum);


            /*val initializeRequest = Posts(
                1, title = inputTitle, explained = inputExplained, min_num = inputMinnum.toInt(),
                category = inputCategory, required = "사이즈"
            )*/

            var file = File(filePath)
            var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

            RetrofitBuilder.api.initPicRequest(
                body, useridRequest, titleRequest, explainedRequest,
                categoryRequest, minRequest
            ).enqueue(object : Callback<InitializeResponse> {
                override fun onResponse(
                    call: Call<InitializeResponse>,
                    response: Response<InitializeResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddProject, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                        //val fragment : HomeFragment = supportFragmentManager.findFragmentById(R.id.home_fragment) as HomeFragment
                        //fragment.getData()
                        val intent = Intent(this@AddProject, SecondActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }

                override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                    Toast.makeText(this@AddProject, "실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("test", "실패$t")
                    finish()
                }
            })


            /*RetrofitBuilder.api.initRequest(initializeRequest).enqueue(object :
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

            })*/

        }
    }

    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val resolver: ContentResolver
        resolver = this.contentResolver

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = resolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 3
            }
        }
        return inSampleSize
    }
    private fun getRealPathFromURI(uri: Uri): String {
        var buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path!!
        }
        var columnIndex = 0
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }
}*/
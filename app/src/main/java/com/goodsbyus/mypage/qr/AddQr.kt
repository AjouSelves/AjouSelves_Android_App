package com.goodsbyus.mypage.qr

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.goodsbyus.R
import com.goodsbyus.databinding.ActivityAddQrBinding

import com.goodsbyus.datas.PayLinkModel
import com.goodsbyus.datas.PayResponse
import com.goodsbyus.datas.StateModel
import com.goodsbyus.home.AddProject
import com.goodsbyus.mypage.MyGoods
import com.goodsbyus.retrofit2.RetrofitBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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

    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        _binding = ActivityAddQrBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("QR 코드 등록")

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) //permission check
            == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), // 1
                AddProject.PERMISSION_REQUEST_CODE
            ) // 2
            showDialog("Permission granted")
        }

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

            val inputPaylink = binding.et1.text.toString()

            val PaylinkRequest = RequestBody.create(MediaType.parse("text/plain"), inputPaylink);


            /*val initializeRequest = Posts(
                1, title = inputTitle, explained = inputExplained, min_num = inputMinnum.toInt(),
                category = inputCategory, required = "사이즈"
            )*/

            var file = File(filePath)
            var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var body = MultipartBody.Part.createFormData("photo", file.name, requestFile)
            val projid=getExtra()

            val paylink=PayLinkModel(paylink = inputPaylink)

            RetrofitBuilder.api.postPayLink(
                projid, PaylinkRequest, body
            ).enqueue(object : Callback<PayResponse> {
                override fun onResponse(
                    call: Call<PayResponse>,
                    response: Response<PayResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddQr, "등록되었습니다.", Toast.LENGTH_SHORT).show()

                        putState(projid)

                    }
                }

                override fun onFailure(call: Call<PayResponse>, t: Throwable) {
                    Toast.makeText(this@AddQr, "실패했습니다.", Toast.LENGTH_SHORT).show()
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
    //Todo : response 형식 다름
    private fun putState(projid: Int){
        val initializeRequest=StateModel(state=2)

        RetrofitBuilder.api.putState(
            projid,initializeRequest
        ).enqueue(object : Callback<PayResponse> {
            override fun onResponse(
                call: Call<PayResponse>,
                response: Response<PayResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    Toast.makeText(this@AddQr, "등록되었습니다.2", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddQr, MyGoods::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
            }

            override fun onFailure(call: Call<PayResponse>, t: Throwable) {
                Toast.makeText(this@AddQr, "실패했습니다.2", Toast.LENGTH_SHORT).show()
                Log.d("test", "실패$t")
                finish()
            }
        })
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

    private fun showDialog(s: String) {
        if(s == "Permission granted"){
            showDialogToGetPermission()
        }
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("권한 요청")
            .setMessage("사진을 업로드하기 위해 권한이 필요합니다." +
                    "설정에서 파일에 접근 요청을 허용해 주세요.")

        builder.setPositiveButton("네") { dialogInterface, i ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)   // 6
        }
        builder.setNegativeButton("나중에") { dialogInterface, i ->
            // ignore
        }
        val dialog = builder.create()
        dialog.show()
    }
}


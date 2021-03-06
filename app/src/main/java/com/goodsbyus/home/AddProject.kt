package com.goodsbyus.home


import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.goodsbyus.*
import com.goodsbyus.databinding.ActivityAddProjectBinding
import com.goodsbyus.datas.InitializeResponse
import com.goodsbyus.datas.Posts
import com.goodsbyus.retrofit2.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_second.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.*
import java.io.*
import java.util.*

class AddProject : AppCompatActivity() {
    private var _binding: ActivityAddProjectBinding? = null

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

    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        _binding = ActivityAddProjectBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("?????? ????????????")
        binding.saveButton.isEnabled=false

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) //permission check
            == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), // 1
                PERMISSION_REQUEST_CODE) // 2
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
                    binding.saveButton.isEnabled=true
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

            val inputTitle = binding.et1.text.toString().replace("'","""\'""")
            val inputExplained = binding.et2.text.toString().replace("'","""\'""")
            val inputCategory = binding.et3.text.toString().replace("'","""\'""")
            val inputMinnum = binding.et4.text.toString()
            val inputAmount = binding.et5.text.toString()

            val useridRequest = RequestBody.create(MediaType.parse("text/plain"), "1");
            val titleRequest = RequestBody.create(MediaType.parse("text/plain"), inputTitle);
            val explainedRequest =
                RequestBody.create(MediaType.parse("text/plain"), inputExplained);
            val categoryRequest = RequestBody.create(MediaType.parse("text/plain"), inputCategory);
            val minRequest = RequestBody.create(MediaType.parse("text/plain"), inputMinnum);
            val amountRequest = RequestBody.create(MediaType.parse("text/plain"), inputAmount);


            /*val initializeRequest = Posts(
                1, title = inputTitle, explained = inputExplained, min_num = inputMinnum.toInt(),
                category = inputCategory, required = "?????????"
            )*/

            var file = File(filePath)
            var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var body = MultipartBody.Part.createFormData("photo", file.name, requestFile)

            RetrofitBuilder.api.initPicRequest(
                body, useridRequest, titleRequest, explainedRequest,
                categoryRequest, minRequest, amountRequest
            ).enqueue(object : Callback<InitializeResponse> {
                override fun onResponse(
                    call: Call<InitializeResponse>,
                    response: Response<InitializeResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddProject, "?????????????????????.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddProject,SecondActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                    else {
                        when (response.code()) {
                            400 -> Log.d("test", response.body()!!.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                    Toast.makeText(this@AddProject, "??????????????????.", Toast.LENGTH_SHORT).show()
                    Log.d("test", "??????$t")
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
                        var data = response.body() // GsonConverter??? ????????? ???????????????
                        Toast.makeText(this@AddProject, "????????? ??????!", Toast.LENGTH_SHORT).show()


                        this@AddProject.onBackPressed()
                        //removeFragment()
                    }
                }

                override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
                    Log.d("test", "??????$t")
                    Toast.makeText(this@AddProject, "????????? ?????? ..", Toast.LENGTH_SHORT).show()
                }

            })*/

        }
    }

    private fun showDialog(s: String) {
        if(s == "Permission granted"){
            showDialogToGetPermission()
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


    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("?????? ??????")
            .setMessage("????????? ??????????????? ?????? ????????? ???????????????." +
                    "???????????? ????????? ?????? ????????? ????????? ?????????.")

        builder.setPositiveButton("???") { dialogInterface, i ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)   // 6
        }
        builder.setNegativeButton("?????????") { dialogInterface, i ->
            // ignore
        }
        val dialog = builder.create()
        dialog.show()
    }
}
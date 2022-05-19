package com.bonustrack02.parktp

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.loader.content.CursorLoader
import com.bonustrack02.parktp.databinding.ActivityWriteBinding
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import gun0912.tedimagepicker.builder.TedImagePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class WriteActivity : AppCompatActivity() {
    val binding : ActivityWriteBinding by lazy { ActivityWriteBinding.inflate(layoutInflater) }
    var selectedUriList : List<Uri>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)

        binding.writeBtnComplete.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("글 작성 확인")
                .setMessage("이대로 업로드하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface, i ->
                    uploadReview()
                    finish()
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i -> })
                .show()
        }

        binding.writeImgAdd1.setOnClickListener { selectImages() }
        binding.writeImgAdd2.setOnClickListener { selectImages() }
    }

    private fun selectImages() {
        TedImagePicker.with(this)
            .errorListener { message -> Log.e("ImagePickError", "message : $message") }
            .cancelListener { Toast.makeText(this, "이미지 선택을 취소했습니다.", Toast.LENGTH_SHORT).show() }
            .max(2, "최대 2장까지 선택가능합니다.")
            .selectedUri(selectedUriList)
            .startMultiImage { list : List<Uri> -> showMultiImage(list) }
    }

    private fun showMultiImage(uriList : List<Uri>) {
        selectedUriList = uriList
        if (selectedUriList?.size == 1) {
            Glide.with(this).load(selectedUriList!![0]).into(binding.writeImgAdd1)
        } else if (selectedUriList?.size == 2) {
            Glide.with(this).load(selectedUriList!![0]).into(binding.writeImgAdd1)
            Glide.with(this).load(selectedUriList!![1]).into(binding.writeImgAdd2)
        }
    }

    val firebaseAuth = FirebaseAuth.getInstance()

    private fun uploadReview() {
        val firebaseStorage = FirebaseStorage.getInstance()

        if (selectedUriList?.size == 1) {
            val sdf = SimpleDateFormat("yyyyMMdd hh:mm:ss")
            val fileName = sdf.format(Date()) + ".png"

            val imgRef : StorageReference = firebaseStorage.getReference("uploads/$fileName")

            val uploadTask = imgRef.putFile(selectedUriList!![0])
            uploadTask.addOnSuccessListener {
                val park_id = intent.getStringExtra("id")
                val title = binding.editTitle.text.toString()
                val content = binding.editContent.text.toString()
                val rating = binding.ratingbar.rating.toString()
                val user_id = "dummy@gmail.com" //firebaseAuth.currentUser?.email.toString()

                val dataPart = HashMap<String, String>()
                if (park_id != null) dataPart["park_id"] = park_id
                dataPart["title"] = title
                dataPart["content"] = content
                dataPart["rating"] = rating
                dataPart["user_id"] = user_id

                val call = RetrofitHelper.getScalarsInstance().create(RetrofitService::class.java).postReviewToServer(dataPart).enqueue(
                    object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            val s = response.body()
                            Log.i("php", "$s")
                            Toast.makeText(this@WriteActivity, "$s", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e("php", "${t.message}")
                        }

                    })
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
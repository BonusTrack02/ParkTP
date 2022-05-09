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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class WriteActivity : AppCompatActivity() {
    val binding : ActivityWriteBinding by lazy { ActivityWriteBinding.inflate(layoutInflater) }

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
                    if (imgUri1 == null && imgUri2 == null) {
                        uploadReview()
                    } else {
                        uploadImages()
                        uploadReview()
                    }
                    finish()
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i -> })
                .show()
        }

        binding.writeImgAdd1.setOnClickListener { selectImage1() }
        binding.writeImgAdd2.setOnClickListener { selectImage2() }
    }

    var imgUri1 : Uri? = null
    var imgUri2 : Uri? = null

    private fun selectImage1() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher1.launch(intent)
    }

    val resultLauncher1 : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
        if (result.resultCode == RESULT_OK) {
            imgUri1 = result.data!!.data!!
            Glide.with(this).load(imgUri1).into(binding.writeImgAdd1)
            Log.i("imgUri", imgUri1.toString())
        } else return@ActivityResultCallback
    })

    private fun selectImage2() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher2.launch(intent)
    }

    val resultLauncher2 : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            if (result.resultCode == RESULT_OK) {
                imgUri2 = result.data!!.data!!
                Log.i("Uri", imgUri2.toString())
                Glide.with(this).load(imgUri2).into(binding.writeImgAdd2)
                Log.i("imgUri", imgUri2.toString())
            } else return@ActivityResultCallback
        })

    private fun uploadImages() {
        val firebaseStorage = FirebaseStorage.getInstance()
        val rootRef = firebaseStorage.reference

        if (imgUri1 != null) {
            val sdf1 = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SS")
            val fileName1 = "IMG_${sdf1.format(Date())}-1.png"

            var imgRef1 = firebaseStorage.getReference("uploads/$fileName1")
            var uploadTask = imgRef1.putFile(imgUri1!!)
            Log.i("imgUpload", "uploading")
            uploadTask.addOnSuccessListener {
                Log.i("upload", "upload success1")
                val img1Ref = rootRef.child("uploads/$fileName1")
                img1Ref.downloadUrl.addOnSuccessListener {
                    img01 = it.toString()
                }
            }
        }

        if (imgUri2 != null) {
            val sdf2 = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SS")
            val fileName2 = "IMG_${sdf2.format(Date())}-2.png"

            var imgRef2 = firebaseStorage.getReference("uploads/$fileName2")
            var uploadTask2 = imgRef2.putFile(imgUri2!!)
            Log.i("imgUpload", "uploading")
            uploadTask2.addOnSuccessListener {
                Log.i("upload", "upload success2")
                val img2Ref = rootRef.child("uploads/$fileName2")
                img2Ref.downloadUrl.addOnSuccessListener {
                    img02 = it.toString()
                }
            }
        }
    }

    lateinit var img01 : String
    lateinit var img02 : String
    val firebaseAuth = FirebaseAuth.getInstance()

    private fun uploadReview() {
        val park_id = intent.getStringExtra("id")
        val title = binding.editTitle.text.toString()
        val content = binding.editContent.text.toString()
        val rating = binding.ratingbar.rating.toString()
        val user_id = firebaseAuth.currentUser?.email.toString()

        val dataPart = HashMap<String, String>()
        if (park_id != null) dataPart["park_id"] = park_id
        dataPart["title"] = title
        dataPart["content"] = content
        dataPart["rating"] = rating
        dataPart["img01"] = img01
        dataPart["img02"] = img02
        dataPart["user_id"] = user_id

        val retrofitService = RetrofitHelper.getScalarsInstance().create(RetrofitService::class.java)
        val call = retrofitService.postReviewToServer(dataPart)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val s = response.body()
                Log.i("response", "" + s)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@WriteActivity, "error" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
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
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

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
                    uploadImages()
                    uploadReview()
                    finish()
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i -> })
                .show()
        }

        binding.writeImgAdd1.setOnClickListener { selectImage1() }
        binding.writeImgAdd2.setOnClickListener { selectImage2() }
    }

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
            } else return@ActivityResultCallback
        })

    lateinit var imgUri1 : Uri
    lateinit var imgUri2 : Uri

    private fun uploadImages() {
        val firebaseStorage = FirebaseStorage.getInstance()

        val sdf1 = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SS")
        val fileName1 = "IMG_${sdf1.format(Date())}-1.png"

        val sdf2 = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss.SS")
        val fileName2 = "IMG_${sdf2.format(Date())}-2.png"

        var imgRef1 = firebaseStorage.getReference("uploads/$fileName1")
        var imgRef2 = firebaseStorage.getReference("uploads/$fileName2")

        var uploadTask = imgRef1.putFile(imgUri1)
        uploadTask.addOnSuccessListener {
            Log.i("upload", "upload success1")
        }

        var uploadTask2 = imgRef2.putFile(imgUri2)
        uploadTask2.addOnSuccessListener {
            Log.i("upload", "upload success2")
        }
    }

    private fun uploadReview() {
        val id = intent.getStringExtra("id")
        val title = binding.editTitle.text.toString()
        val content = binding.editContent.text.toString()
        val rating : Float = binding.ratingbar.rating
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
package com.bonustrack02.parktp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bonustrack02.parktp.databinding.ActivityWriteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
                    finish()
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i -> })
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
package com.bonustrack02.parktp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bonustrack02.parktp.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {
    val binding : ActivityWriteBinding by lazy { ActivityWriteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
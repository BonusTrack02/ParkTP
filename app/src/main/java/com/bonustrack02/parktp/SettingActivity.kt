package com.bonustrack02.parktp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bonustrack02.parktp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    val binding : ActivitySettingBinding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
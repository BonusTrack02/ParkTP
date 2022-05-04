package com.bonustrack02.parktp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.bonustrack02.parktp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding : ActivitySplashBinding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    var permissions : Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED
            || checkSelfPermission(permissions[1]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, 100)
        } else {
            handler.sendEmptyMessageDelayed(0, 2000)
        }
    }

    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        handler.removeMessages(0)
                        finish()
                    } else if (result == PackageManager.PERMISSION_GRANTED) {
                        handler.sendEmptyMessageDelayed(0, 2000)
                    }
                }
            }
        }
    }
}
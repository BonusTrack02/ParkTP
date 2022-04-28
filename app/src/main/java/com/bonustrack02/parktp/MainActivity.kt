package com.bonustrack02.parktp

import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.bonustrack02.parktp.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val fragmentList : MutableList<Fragment> = mutableListOf()
    lateinit var providerClient : FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fragmentList.add(GraphFragment())
        fragmentList.add(MapFragment())
        fragmentList.add(InfoFragment())

        supportFragmentManager.beginTransaction().add(R.id.container, fragmentList[0]).commit()
        binding.bnv.setOnItemSelectedListener {
            supportFragmentManager.fragments.forEach {
                supportFragmentManager.beginTransaction().hide(it).commit()
            }

            val tran = supportFragmentManager.beginTransaction()

            when (it.itemId) {
                R.id.bnv_analytics -> {
                    tran.show(fragmentList[0])
                }
                R.id.bnv_map -> {
                    if (!supportFragmentManager.fragments.contains(fragmentList[1])) tran.add(R.id.container, fragmentList[1])
                    tran.show(fragmentList[1])
                }
                R.id.bnv_info -> {
                    if (!supportFragmentManager.fragments.contains(fragmentList[2])) tran.add(R.id.container, fragmentList[2])
                    tran.show(fragmentList[2])
                }
            }
            tran.commit()
            true
        }

        binding.bnv.selectedItemId = R.id.bnv_analytics

        providerClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 5000

        providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    val locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            val location : Location = p0.lastLocation
            G.lat = location.latitude
            G.lng = location.longitude
        }
    }

    var wasPressed = false
    var lastTime = 0L
    override fun onBackPressed() {
        if (!wasPressed) {
            Toast.makeText(this, "한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            wasPressed = true
            lastTime = Date().time
        } else {
            var nowTime = Date().time
            if (nowTime - lastTime > 2500) wasPressed = false

            else super.onBackPressed()
        }
    }
}
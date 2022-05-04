package com.bonustrack02.parktp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bonustrack02.parktp.databinding.ActivityParkDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ParkDetailActivity : AppCompatActivity() {
    val binding : ActivityParkDetailBinding by lazy { ActivityParkDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)

        val fragmentManager = supportFragmentManager
        val mapFragment : SupportMapFragment = fragmentManager.findFragmentById(R.id.detail_map) as SupportMapFragment

        val parkLat = intent.getStringExtra("position_lat")!!.toDouble()
        val parkLng = intent.getStringExtra("position_lng")!!.toDouble()

        mapFragment.getMapAsync {
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(parkLat, parkLng), 18f))

            it.uiSettings.isZoomControlsEnabled = true

            val markerOptions = MarkerOptions()
            binding.toolbarTitle.text = intent.getStringExtra("title")
            markerOptions.title(intent.getStringExtra("title"))
            markerOptions.position(
                LatLng(intent.getStringExtra("position_lat")!!.toDouble(),
                    intent.getStringExtra("position_lng")!!.toDouble()))
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place2))
            markerOptions.anchor(0.5f, 1.5f)

            it.addMarker(markerOptions)
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
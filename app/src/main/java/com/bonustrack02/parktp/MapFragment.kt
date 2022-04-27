package com.bonustrack02.parktp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bonustrack02.parktp.databinding.FragmentMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places

class MapFragment : Fragment() {

    val binding : FragmentMapBinding by lazy { FragmentMapBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    lateinit var providerClient : FusedLocationProviderClient
    var lat : Double = 0.0
    var lng : Double = 0.0
    var parkMarkers : MutableList<LatLng> = mutableListOf()

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager : FragmentManager = childFragmentManager
        val mapFragment : SupportMapFragment? = fragmentManager.findFragmentById(R.id.gmap) as? SupportMapFragment

        val ac = activity as MainActivity
        binding.fab.setOnClickListener {
            val tran = ac.supportFragmentManager.beginTransaction()

            var mapRecyclerFragment = MapRecyclerFragment()

            tran.add(R.id.container, mapRecyclerFragment)
            tran.addToBackStack(null)
            tran.commit()
        }

        /////Retrofit
        val retrofit = RetrofitHelper.getInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        //val call = retrofitService.getParkJson("KakaoAK ${R.string.kakao_rest_key}", "공원", )

        providerClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 3000

        providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        mapFragment!!.getMapAsync { p0 ->
            var myLocation = LatLng(lat, lng)
            Log.i("latlng", "$lat , $lng")
            p0.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16f))

            p0.isMyLocationEnabled = true
        }
    }

    val locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            val location : Location = p0.lastLocation
            lat = location.latitude
            lng = location.longitude
        }
    }

    override fun onPause() {
        super.onPause()

        providerClient.removeLocationUpdates(locationCallback)
    }
}
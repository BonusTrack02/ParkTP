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

class MapFragment : Fragment() {

    val binding : FragmentMapBinding by lazy { FragmentMapBinding.inflate(layoutInflater) }
    var lat : Double = 0.0
    var lng : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentManager : FragmentManager = requireActivity().supportFragmentManager
        val mapFragment : SupportMapFragment? = fragmentManager.findFragmentById(R.id.gmap) as? SupportMapFragment

        var permissions : MutableList<String> = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (requireContext().checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions.toTypedArray(), 100)
        }

        providerClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 3000

        providerClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        mapFragment?.getMapAsync(object : OnMapReadyCallback {
            @SuppressLint("MissingPermission")
            override fun onMapReady(p0: GoogleMap) {
                var myLocation = LatLng(lat, lng)
                Log.i("latlng", "$lat , $lng")
                p0.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16f))

                var marker = MarkerOptions()
                marker.title("내 위치")
                marker.position(myLocation)
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_24))
                marker.anchor(0.5f, 1.5f)

                p0.addMarker(marker)

                val settings = p0.uiSettings
                settings.isZoomControlsEnabled = true
                p0.isMyLocationEnabled = true
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    lateinit var providerClient : FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ac = activity as MainActivity
        binding.fab.setOnClickListener {
            val tran = ac.supportFragmentManager.beginTransaction()

            var mapRecyclerFragment = MapRecyclerFragment()

            tran.add(R.id.container, mapRecyclerFragment)
            tran.addToBackStack(null)
            tran.commit()
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

        if (providerClient != null) providerClient.removeLocationUpdates(locationCallback)
    }
}
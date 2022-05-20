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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bonustrack02.parktp.G.Companion.lat
import com.bonustrack02.parktp.G.Companion.lng
import com.bonustrack02.parktp.databinding.FragmentMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment() {

    val binding : FragmentMapBinding by lazy { FragmentMapBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    var markers : MutableList<Marker> = mutableListOf()
    var googleMap:GoogleMap? = null
    var responseItem : ResponseItem? = null

    var lastLat = lat
    var lastLng = lng

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
        AddMarkersScalars()

        mapFragment!!.getMapAsync { p0 ->
            googleMap = p0
            var myLocation = LatLng(lat, lng)
            Log.i("latlng", "$lat , $lng")
            p0.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16f))

            p0.isMyLocationEnabled = true
            p0.setOnMyLocationButtonClickListener {
                val mainActivity = activity as MainActivity
                mainActivity.providerClient.requestLocationUpdates(
                    mainActivity.locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                false
            }
        }
    }

    val locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            val location : Location = p0.lastLocation
            G.lat = location.latitude
            G.lng = location.longitude

            if (lastLat == lat && lastLng == lng) {
                for (marker in markers) {
                    marker.remove()
                }
                markers.clear()

                AddMarkersScalars()
            } else {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 16f))
                for (marker in markers) {
                    marker.remove()
                }
                markers.clear()

                AddMarkersScalars()
            }
            Toast.makeText(requireContext(), "callback", Toast.LENGTH_SHORT).show()
            val mainActivity = activity as MainActivity
            mainActivity.providerClient.removeLocationUpdates(this)
        }
    }

    private fun loadDataAndAddMarkers() {
        val retrofit = RetrofitHelper.getInstance()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        val kakaoKey = resources.getString(R.string.kakao_rest_key)
        Toast.makeText(requireContext(), "$lng", Toast.LENGTH_SHORT).show()
        Toast.makeText(requireContext(), "$lat", Toast.LENGTH_SHORT).show()
        val call = retrofitService.getJson("공원", "$lng", "$lat", "10000")
        Log.i("latlng", "$lat  $lng")
        call.enqueue(object : Callback<ResponseItem> {
            override fun onResponse(call: Call<ResponseItem>, response: Response<ResponseItem>) {
                var responseItem : ResponseItem? = response.body()
                if (response.body() == null) return
                Log.i("Response", responseItem?.documents?.size.toString())
                val size = responseItem?.documents?.size
                if (size != null) {
                    for (i in 0 until size) {
                        if (responseItem?.documents?.get(i)?.category_name?.contains("공원")!!) {
                            markers.add(
                                googleMap?.addMarker(MarkerOptions()
                                    .title(responseItem.documents[i].place_name)
                                    .position(LatLng(responseItem.documents[i].y.toDouble(), responseItem.documents[i].x.toDouble()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place2))
                                    .anchor(0.5f, 1.5f))!!
                            )
                            Log.i("retrofit place name", responseItem.documents[i].place_name)
                        }
                    }
                } else return

                this@MapFragment.responseItem = responseItem
            }

            override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                Log.e("Retrofit error", t.message.toString())
            }
        })
    }

    private fun AddMarkersScalars() {
        val retrofit = RetrofitHelper.getScalarsInstanceForTest()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        val kakaoKey = resources.getString(R.string.kakao_rest_key)
        val call = retrofitService.getScalarsJson("KakaoAK $kakaoKey", "공원", "$lng", "$lat", "10000")
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var responseItem : String?
                if (response.body() == null) {
                    responseItem = response.errorBody().toString()
                } else {
                    responseItem = response.body()
                    val responseObject = JSONObject(responseItem)
                    val metaObject = responseObject.getJSONObject("meta")
                    val count = metaObject.getInt("total_count")

                    val docsArray  = responseObject.getJSONArray("documents")
                    Log.i("parse", "${docsArray.length()} : $count")
                }

                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("$responseItem").create().show()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.adView.setClientId("DAN-4U3grIhtKKHbIuAm")
        binding.adView.loadAd()
    }

    override fun onPause() {
        super.onPause()
        binding.adView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.adView.destroy()
    }
}
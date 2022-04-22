package com.bonustrack02.parktp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bonustrack02.parktp.databinding.FragmentMapBinding
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentManager : FragmentManager = requireActivity().supportFragmentManager
        val mapFragment : SupportMapFragment? = fragmentManager.findFragmentById(R.id.gmap) as? SupportMapFragment

        mapFragment?.getMapAsync {

        }

        var permissions : MutableList<String> = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (requireContext().checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions.toTypedArray(), 100)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    val binding : FragmentMapBinding by lazy { FragmentMapBinding.inflate(layoutInflater) }

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
}
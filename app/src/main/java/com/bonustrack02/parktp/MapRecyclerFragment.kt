package com.bonustrack02.parktp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bonustrack02.parktp.databinding.FragmentRecyclerMapBinding
import com.google.android.gms.maps.model.Marker

class MapRecyclerFragment : Fragment() {
    val binding : FragmentRecyclerMapBinding by lazy { FragmentRecyclerMapBinding.inflate(layoutInflater) }

    lateinit var markers : MutableList<Marker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.getMapFragmentMarkers()

        binding.recycler.adapter = MapAdapter(requireContext(), mainActivity.getMapFragmentResponseItem())
    }

    override fun onResume() {
        super.onResume()

        binding.recycler.adapter?.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ac = activity as MainActivity
        binding.fab.setOnClickListener {
            val tran = ac.supportFragmentManager.beginTransaction()

            tran.remove(this)
            tran.commit()
        }


    }
}
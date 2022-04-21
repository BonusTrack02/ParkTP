package com.bonustrack02.parktp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bonustrack02.parktp.databinding.FragmentRecyclerMapBinding

class MapRecyclerFragment : Fragment() {
    val binding : FragmentRecyclerMapBinding by lazy { FragmentRecyclerMapBinding.inflate(layoutInflater) }

    var mapItems = mutableListOf<MapItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapItems.add(MapItem("서울숲", "편의점 있음. 공중화장실 있음"))

        binding.recycler.adapter = MapAdapter(requireContext(), mapItems)
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

        binding.fab.setOnClickListener {

        }
    }
}
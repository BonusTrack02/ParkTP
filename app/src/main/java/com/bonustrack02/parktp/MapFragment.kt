package com.bonustrack02.parktp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bonustrack02.parktp.databinding.FragmentMapBinding

class MapFragment : Fragment() {
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
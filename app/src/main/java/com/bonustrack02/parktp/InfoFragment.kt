package com.bonustrack02.parktp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.bonustrack02.parktp.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    var infoItems : MutableList<InfoItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        infoItems.add(InfoItem("로그인한 계정 정보", R.drawable.ic_account_circle_24))
        infoItems.add(InfoItem("푸시 알림 서비스 설정", R.drawable.ic_settings_24))

        val adapter = InfoAdapter(activity as Context, infoItems)
        binding.listview.adapter = adapter

        binding.listview.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var intent : Intent = Intent()
                if (p2 == 0) {
                    intent.action = "account"
                } else if (p2 == 1) {
                    intent.action = "setting"
                }
                startActivity(intent)
            }
        })
    }

    val binding : FragmentInfoBinding by lazy { FragmentInfoBinding.inflate(layoutInflater) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
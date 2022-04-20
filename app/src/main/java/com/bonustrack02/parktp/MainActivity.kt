package com.bonustrack02.parktp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.bonustrack02.parktp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val fragmentList : MutableList<Fragment> = mutableListOf()

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
    }
}
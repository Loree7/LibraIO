package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.adapters.AdapterClass
import com.lorenzoprogramma.libraio.adapters.AdapterTabPage
import com.lorenzoprogramma.libraio.databinding.FragmentLoansBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils


class LoansFragment : Fragment() {

    private lateinit var binding: FragmentLoansBinding
    private lateinit var adapter: AdapterTabPage
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoansBinding.inflate(layoutInflater)

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2
        adapter = AdapterTabPage(parentFragmentManager, lifecycle)//da controllare

        tabLayout.addTab(tabLayout.newTab().setText("Prestiti attivi"))
        tabLayout.addTab(tabLayout.newTab().setText("Prestiti passati"))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab : TabLayout.Tab?){
                if (tab != null){
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }



        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }

        })

        binding.imageButtonBackToHome.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, HomeFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(true)
        }


        return binding.root
    }
}
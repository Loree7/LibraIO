package com.lorenzoprogramma.libraio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lorenzoprogramma.libraio.databinding.ActivityMainBinding
import com.lorenzoprogramma.libraio.fragments.HomeFragment
import com.lorenzoprogramma.libraio.fragments.LoginFragment
import com.lorenzoprogramma.libraio.fragments.WishlistFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addFragment(HomeFragment(), R.id.main_frame_layout)
        addFragment(LoginFragment(), R.id.loginFragmentContainer)


        binding.bottomNavigationView.setOnItemSelectedListener {
            val homeFragment     = supportFragmentManager.findFragmentById(R.id.homeF)
            when(it.itemId){
                R.id.home -> if(homeFragment == null ){replaceFragment(HomeFragment(),R.id.main_frame_layout)}
                R.id.wishlist -> replaceFragment(WishlistFragment(),R.id.main_frame_layout)
                else->{
                }
            }
            true
            //ciao
        }
    }

    private fun addFragment(fragment: Fragment, container : Int) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(container, fragment)
        transaction.commit()
    }
    private fun replaceFragment(fragment: Fragment, container : Int) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.commit()
    }


}
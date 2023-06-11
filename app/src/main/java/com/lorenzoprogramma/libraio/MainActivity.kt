package com.lorenzoprogramma.libraio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
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

        addFragment(LoginFragment(), R.id.loginFragmentContainer)


        binding.bottomNavigationView.setOnItemSelectedListener {
            val homeFragmentId = supportFragmentManager.findFragmentById(R.id.homeF)
            when(it.itemId){
                R.id.home -> if (homeFragmentId == null ) { replaceFragment(HomeFragment(),R.id.main_frame_layout) }
                R.id.wishlist -> replaceFragment(WishlistFragment(), R.id.main_frame_layout)
                else->{
                }
            }
            true
        }
    }

    private fun addFragment(fragment: Fragment, container: Int) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(container, fragment)
        transaction.commit()
    }
    private fun replaceFragment(fragment: Fragment, container: Int) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.commit()
    }


}
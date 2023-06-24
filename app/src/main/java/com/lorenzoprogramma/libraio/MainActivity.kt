package com.lorenzoprogramma.libraio

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lorenzoprogramma.libraio.databinding.ActivityMainBinding
import com.lorenzoprogramma.libraio.fragments.HomeFragment
import com.lorenzoprogramma.libraio.fragments.LoginFragment
import com.lorenzoprogramma.libraio.fragments.WishlistFragment
import com.lorenzoprogramma.libraio.utils.FragmentUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            FragmentUtils.addFragment(fragmentManager, LoginFragment(), R.id.loginFragmentContainer)
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            val homeFragmentId = supportFragmentManager.findFragmentById(R.id.homeF)
            when(it.itemId){
                R.id.home -> if (homeFragmentId == null ) { FragmentUtils.replaceFragment(fragmentManager, HomeFragment(), R.id.main_frame_layout) }
                R.id.wishlist -> {
                    val sharedPreferences = this.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val isLogged = sharedPreferences.getBoolean("isLogged", false)
                    if (isLogged) {
                        FragmentUtils.replaceFragment(fragmentManager, WishlistFragment(), R.id.main_frame_layout)
                    } else {
                        Toast.makeText(this, "Non puoi accedere a questa sezione come ospite", Toast.LENGTH_SHORT).show()
                    }
                }
                else->{
                }
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLogged", false)
        editor.apply()
    }


    fun toggleBottomNavigationView(toggle: Boolean) {
        if (toggle) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }


}
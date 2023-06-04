package com.lorenzoprogramma.libraio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lorenzoprogramma.libraio.databinding.ActivityMainBinding
import com.lorenzoprogramma.libraio.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.loginFragmentContainer, LoginFragment())
        transaction.commit()
    }
}
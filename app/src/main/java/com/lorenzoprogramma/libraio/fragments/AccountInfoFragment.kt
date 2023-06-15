package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentAccountInfoBinding

class AccountInfoFragment : Fragment() {
    private lateinit var binding: FragmentAccountInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountInfoBinding.inflate(inflater)
        val userInfo = arguments?.getParcelable<User>("user")
        println("info: $userInfo")
        if (userInfo != null) {
            setUpUserInfo(userInfo)
        }
        return binding.root
    }

    private fun setUpUserInfo(user: User) {
        binding.textViewName.text = user.name
        binding.textViewSurname.text = user.surname
        binding.textViewUsername.text = user.username
        binding.textViewConduct.text = user.conduct.toString()
    }

}
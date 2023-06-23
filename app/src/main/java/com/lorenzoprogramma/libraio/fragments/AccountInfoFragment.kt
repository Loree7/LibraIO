package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import android.text.Editable
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
        binding.textInputName.text = Editable.Factory.getInstance().newEditable(user.name)
        binding.textInputSurname.text = Editable.Factory.getInstance().newEditable(user.surname)
        binding.textInputUsername.text = Editable.Factory.getInstance().newEditable(user.username)
        binding.textInputPassword.text = Editable.Factory.getInstance().newEditable(user.userPassword)
        binding.ratingBar.rating = user.conduct?.toFloat() ?: 0f
//        binding.textViewName.text = user.name
//        binding.textViewSurname.text = user.surname
//        binding.textViewUsername.text = user.username
//        binding.textViewConduct.text = user.conduct.toString()
    }

}
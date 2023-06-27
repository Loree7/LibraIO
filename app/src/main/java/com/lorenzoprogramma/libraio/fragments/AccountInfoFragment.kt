package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentAccountInfoBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils
import com.lorenzoprogramma.libraio.utils.UserViewModel

class AccountInfoFragment : Fragment() {
    private lateinit var binding: FragmentAccountInfoBinding
    private var isPasswordVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountInfoBinding.inflate(inflater)
        val userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        val user = userViewModel.userVM
        if (user != null) {
            setUpUserInfo(user)
        }


        binding.imageButtonBack.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, HomeFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(true)
        }

        binding.imageButtonTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.textInputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.imageButtonTogglePassword.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                binding.textInputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.imageButtonTogglePassword.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }
        return binding.root
    }

    private fun setUpUserInfo(user: User) {
        binding.textInputName.text = Editable.Factory.getInstance().newEditable(user.name)
        binding.textInputSurname.text = Editable.Factory.getInstance().newEditable(user.surname)
        binding.textInputUsername.text = Editable.Factory.getInstance().newEditable(user.username)
        binding.textInputPassword.text = Editable.Factory.getInstance().newEditable(user.userPassword)
        binding.ratingBar.rating = user.conduct?.toFloat() ?: 0f
    }

}
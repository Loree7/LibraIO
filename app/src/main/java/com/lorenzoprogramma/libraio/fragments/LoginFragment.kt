package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.databinding.FragmentLoginBinding


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)

        binding.loginButton.setOnClickListener {
            if (checkCredentials(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())) {
                Toast.makeText(context, "Accesso riuscito!", Toast.LENGTH_SHORT).show()
                openHome()
            } else {
                Toast.makeText(context, "Accesso non riuscito!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewRegister.setOnClickListener {
            openRegisterModule()
        }
        return binding.root
    }

    private fun openHome() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

    private fun openRegisterModule() {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.loginFragmentContainer, RegisterFragment()).commit()
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        // inserire qua il controllo email/pass con il database
        val validEmail = "lucio"
        val validPassword = "123"

        return email == validEmail && password == validPassword
    }

}
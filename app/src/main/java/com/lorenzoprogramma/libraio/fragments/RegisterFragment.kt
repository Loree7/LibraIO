package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        var nome: String = binding.registerNomeEditText.text.toString()
        var cognome: String = binding.registerCognomeEditText.text.toString()
        var usurname: String = binding.registerUsurnameEditText.text.toString()
        var password: String = binding.registerPasswordEditText.text.toString()
        binding.registerButton.setOnClickListener {
            registerNewUser(nome, cognome, usurname, password)
        }
        return binding.root
    }

    fun registerNewUser(nome: String, cognome: String, usurname: String, password: String){
        //inserire nel DB i dati del nuovo utente registrato

        //apertura loginActivity e chiusura RegisterActivity
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }
}
package com.lorenzoprogramma.libraio.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lorenzoprogramma.libraio.databinding.ActivityLoginBinding
import com.lorenzoprogramma.libraio.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var nome: String = binding.registerNomeEditText.toString()
        var cognome: String = binding.registerCognomeEditText.toString()
        var usurname: String = binding.registerUsurnameEditText.toString()
        var password: String = binding.registerPasswordEditText.toString()
        binding.registerButton.setOnClickListener {
            userRegistration(nome, cognome, usurname, password)
        }
    }

    fun userRegistration(nome: String, cognome: String, usurname: String, password: String){
        //inserire nel DB i dati del nuovo utente registrato

        //apertura loginActivity e chiusura RegisterActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent)
        finish()
    }
}
package com.lorenzoprogramma.libraio.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if (checkCredentials(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())) {
                Toast.makeText(this, "Accesso riuscito!", Toast.LENGTH_SHORT).show()
                openHome()
            } else {
                Toast.makeText(this, "Accesso non riuscito!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewRegister.setOnClickListener {
            openRegisterActivity()
        }


    }

    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent)
    }

    private fun openHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        setResult(Activity.RESULT_OK, intent);
        startActivity(intent)
        finish()
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        // inserire qua il controllo email/pass con il database
        val validEmail = "lucio"
        val validPassword = "123"

        return email == validEmail && password == validPassword
    }

}
package com.lorenzoprogramma.libraio.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.data.UserLogin
import com.lorenzoprogramma.libraio.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)

//        binding.loginButton.setOnClickListener {
//            if (checkCredentials(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString())) {
//                Toast.makeText(context, "Accesso riuscito!", Toast.LENGTH_SHORT).show()
//                openHome()
//            } else {
//                Toast.makeText(context, "Accesso non riuscito!", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            checkCredentials(UserLogin(username, password)) {result ->
                if (result) {
                    binding.textViewError.visibility = View.GONE
                    openHome()
                } else {
                    binding.textViewError.visibility = View.VISIBLE
                }
            }


        }

        binding.textViewRegister.setOnClickListener {
            openRegisterModule()
        }
        return binding.root
    }

    private fun checkCredentials(userLogin: UserLogin, callback: (Boolean) -> Unit) {
        val query =
            "select username, password from user where username = '${userLogin.username}' and password = '${userLogin.userPassword}';"

        ClientNetwork.retrofit.loginUser(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    println("gg")
                    if (response.isSuccessful) {
                        println("success")
                        val result = (response.body()?.get("queryset") as JsonArray).size() == 1
                        callback.invoke(result)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback.invoke(false)
                    println("Problem on login call")
                }
            }
        )
    }

    private fun openHome() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.main_frame_layout, HomeFragment()).commit()
    }

    private fun openRegisterModule() {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.loginFragmentContainer, RegisterFragment()).commit()
    }

//    private fun checkCredentials(email: String, password: String): Boolean {
//        // inserire qua il controllo email/pass con il database
//        val validEmail = "lucio"
//        val validPassword = "123"
//
//        return email == validEmail && password == validPassword
}

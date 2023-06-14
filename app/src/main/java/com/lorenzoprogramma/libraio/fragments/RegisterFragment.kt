package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Response


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)


        binding.registerButton.setOnClickListener {
            val name: String = binding.registerNomeEditText.text.toString()
            val surname: String = binding.registerCognomeEditText.text.toString()
            val username: String = binding.registerUsernameEditText.text.toString()
            val password: String = binding.registerPasswordEditText.text.toString()

            if (name.isBlank() || surname.isBlank() || username.isBlank() || password.isBlank()) {
                binding.textViewErrorRegister.text = getString(R.string.blank_textfield)
            } else {
                checkIfUserExist(username) {isRegistered ->
                    if (isRegistered) {
                        binding.textViewErrorRegister.text = getString(R.string.isRegistered)
                        binding.textViewErrorRegister.visibility = View.VISIBLE
                        Toast.makeText(context, "Esiste", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.textViewErrorRegister.visibility = View.GONE
                        registerNewUser(name, surname, username, password) {result ->
                            if (result) {
                                Toast.makeText(context, "Utente registrato", Toast.LENGTH_SHORT).show()
                                closeRegisterModule()
                            } else {
                                Toast.makeText(context, "errore nella registrazione", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }


    private fun checkIfUserExist(username: String, callback: (Boolean) -> Unit) {
        val query = "SELECT * FROM user WHERE username = '$username';"
        ClientNetwork.retrofit.findUser(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSize = (response.body()?.get("queryset") as JsonArray).size()
                        val exist = resultSize == 1
                        println(exist)
                        callback(exist)
                    } else {
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false)
                    Log.e("Retrofit", "Errore: ${t.message}", t)
                    println("Problem on login call")
                }
            }
        )
    }

    private fun registerNewUser(name: String, surname: String, username: String, password: String, callback: (Boolean) -> Unit) {
        val query = "insert into user (name, surname, password, username) values ('${name}', '${surname}', '${password}', '${username}');"

        ClientNetwork.retrofit.registerUser(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        println("success")
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        println(resultSizePrimitive)
                        if (resultSize != null) {
                            val registrationSuccessful = resultSize == "insert executed!"
                            println(registrationSuccessful)
                            callback(registrationSuccessful)
                        } else {
                            callback(false)
                        }
                    } else {
                        val statusCode = response.code()
                        val errorMessage = response.message()
                        Log.e("Retrofit", "Errore $statusCode: $errorMessage")
                        println("non success")
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false)
                    Log.e("Retrofit", "Errore: ${t.message}", t)
                    println("Problem on register call")
                }
            }
        )
    }

    private fun closeRegisterModule() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.loginFragmentContainer, LoginFragment()).commit()
    }

}
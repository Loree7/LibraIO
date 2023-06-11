package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentRegisterBinding
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        var name: String = binding.registerNomeEditText.text.toString()
        var surname: String = binding.registerCognomeEditText.text.toString()
        var username: String = binding.registerUsernameEditText.text.toString()
        var password: String = binding.registerPasswordEditText.text.toString()

        binding.registerButton.setOnClickListener {

            checkIfUserExist(User(null, null, null, null, username, null)) {isRegistered ->
                if (isRegistered) {
                    binding.textViewErrorRegister.visibility = View.VISIBLE
                    Toast.makeText(context, "Esiste", Toast.LENGTH_SHORT).show()
                    println("esiste")
                } else {
                    binding.textViewErrorRegister.visibility = View.GONE
                    Toast.makeText(context, "NOn esiste", Toast.LENGTH_SHORT).show()
                    println("Non esiste")
//                    closeRegisterModule()
                }
            }
        }
        return binding.root
    }

    fun checkIfUserExist(user: User, callback: (Boolean) -> Unit) {
        val query = "select COUNT(*) AS count from user where username='${user.username}';"
//        val query2 = "insert into user (name, surname, password, username) values ('${user.name}', '${user.surname}', '${user.username}', '${user.userPassword}');"
        val requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"username\": \"$user.username\"}")

        ClientNetwork.retrofit.findUser(requestBody).enqueue(
            object : retrofit2.Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        val result = response.body() ?: false
                        callback(result)
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    println("problema con la registrazione")
                    callback.invoke(false)
                }
            }
        )

    }

    private fun closeRegisterModule() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.main_frame_layout, HomeFragment()).commit()
    }

}
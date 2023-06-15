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
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)


        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (username.isBlank() || password.isBlank()) {
                binding.textViewError.text = getString(R.string.blank_textfield)
            } else {

                checkCredentials(username, password) { result ->
                    if (result) {
                        val homeFragment = HomeFragment()
                        binding.textViewError.visibility = View.GONE
                        obtainUserInfo(username) {user ->
                            val bundle = Bundle()
                            bundle.putParcelable("user", user)
                            homeFragment.arguments = bundle
                            openHome(homeFragment)
                        }
                    } else {
                        binding.textViewError.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.textViewRegister.setOnClickListener {
            openRegisterModule()
        }
        return binding.root
    }

    private fun checkCredentials(username: String, password: String, callback: (Boolean) -> Unit) {
        val query =
            "select username, password from user where username = '${username}' and password = '${password}';"

        ClientNetwork.retrofit.loginUser(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray).size() == 1
                        callback(result)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(false)
                    println("Problem on login call")
                }
            }
        )
    }

    private fun openHome(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.main_frame_layout, fragment).commit()
        (activity as? MainActivity)?.toggleBottomNavigationView(true)
    }

    private fun openRegisterModule() {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.loginFragmentContainer, RegisterFragment()).commit()
    }

    private fun obtainUserInfo(username: String, callback: (User) -> Unit) {
        val query = "SELECT name, surname, username, password, conduct from user where username= '$username';"

        ClientNetwork.retrofit.getUserInfo(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val userResponse = (response.body()?.get("queryset") as JsonArray)
                        println(userResponse)
                        val name = userResponse[0].asJsonObject.get("name").asString
                        val surname = userResponse[0].asJsonObject.get("surname").asString
                        val usernameR = userResponse[0].asJsonObject.get("username").asString
                        val password = userResponse[0].asJsonObject.get("password").asString
                        val conduct = userResponse[0].asJsonObject.get("conduct").asInt

                        val user = User(name, surname, usernameR, password, conduct)
                        callback(user)
                    } else {
                        println("PROBLEMI PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Retrofit", "Errore: ${t.message}", t)
                    println("Problem request user info")
                }
            }
        )

    }

}

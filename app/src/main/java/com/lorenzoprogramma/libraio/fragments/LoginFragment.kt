package com.lorenzoprogramma.libraio.fragments

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
import com.lorenzoprogramma.libraio.databinding.FragmentLoginBinding
import retrofit2.Call
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
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Riempi tutti i campi", Toast.LENGTH_SHORT).show()
            } else {

                checkCredentials(username, password) { result ->
                    if (result) {
                        binding.textViewError.visibility = View.GONE
                        openHome()
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

    private fun openHome() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.main_frame_layout, CatalogFragment()).commit()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

    private fun openRegisterModule() {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.loginFragmentContainer, RegisterFragment()).commit()
    }

}

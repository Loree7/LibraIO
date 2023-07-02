package com.lorenzoprogramma.libraio.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentLoginBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils
import com.lorenzoprogramma.libraio.viewmodels.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime


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
                            val userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
                            userViewModel.userVM = user
                            val bundle = Bundle()
                            bundle.putParcelable("user", user)
                            homeFragment.arguments = bundle
                            val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences?.edit()
                            editor?.putBoolean("isLogged", true)
                            editor?.apply()

                            val updatedCopies = arrayListOf<String>()
                            var expiredLoans = false
                            println(user.username)
                            checkExpiredLoansForUser(user.username!!) {arrayOfIds ->
                                if (arrayOfIds.isNotEmpty()) {
                                    val callbackCount = arrayOfIds.size
                                    var processedCallback = 0
                                    println("arrayOfIds: $arrayOfIds")
                                    for (id in arrayOfIds) {
                                        changeStatusOfLoan(id, user.username!!) {success ->
                                            if (success) {
                                                processedCallback++
                                                if (processedCallback == callbackCount) {
                                                    for (isbn in updatedCopies) {
                                                        updateNumberOfCopies(isbn)
                                                    }
                                                    downgradeRatingOfUser(user.username!!)
                                                    expiredLoans = true

                                                    val bundle2 = Bundle()
                                                    println("login: $expiredLoans")
                                                    bundle2.putBoolean("exipired", expiredLoans)
                                                    homeFragment.arguments = bundle2
                                                }
                                            }
                                        }
                                        getIsbnFromExipiredLoan(id) {isbn ->
                                            println("isbn: $isbn")
                                            updatedCopies.add(isbn)
                                            processedCallback++
                                            if (processedCallback == callbackCount) {
                                                for (isbn in updatedCopies) {
                                                    updateNumberOfCopies(isbn)
                                                }
                                                downgradeRatingOfUser(user.username!!)
                                                expiredLoans = true

                                                val bundle2 = Bundle()
                                                println("login: $expiredLoans")
                                                bundle2.putBoolean("exipired", expiredLoans)
                                                homeFragment.arguments = bundle2
                                            }
                                        }
                                    }
                                }
                                FragmentUtils.removeFragment(requireActivity().supportFragmentManager, this)
                                FragmentUtils.addFragment(requireActivity().supportFragmentManager, homeFragment, R.id.main_frame_layout)
                                (activity as? MainActivity)?.toggleBottomNavigationView(true)
                            }
                        }
                    } else {
                        binding.textViewError.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.textViewRegister.setOnClickListener {
//            openRegisterModule()
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, RegisterFragment(), R.id.loginFragmentContainer)
        }

        binding.textViewGuest.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putBoolean("isLogged", false)
            editor?.apply()
            FragmentUtils.removeFragment(requireActivity().supportFragmentManager, this)
            FragmentUtils.addFragment(requireActivity().supportFragmentManager, HomeFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(true)
        }

        return binding.root
    }

    private fun checkCredentials(username: String, password: String, callback: (Boolean) -> Unit) {
        val query =
            "select username, password from user where username = '${username}' and password = '${password}';"

        ClientNetwork.retrofit.select(query).enqueue(
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

    private fun obtainUserInfo(username: String, callback: (User) -> Unit) {
        val query = "SELECT name, surname, username, password, conduct from user where username= '$username';"

        ClientNetwork.retrofit.select(query).enqueue(
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

    private fun updateNumberOfCopies(isbn: String) {
        val query = "update book set number_of_copies = number_of_copies + 1 where isbn='$isbn';"

        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        println(resultSizePrimitive)
                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }

    private fun checkExpiredLoansForUser(username: String, callback: (ArrayList<Int>) -> Unit) {
        val now = LocalDateTime.now()
        val query = "select id from loans where username_user = '$username' and end_date < '$now' and status = 1;"

        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        val arrayOfIds = ArrayList<Int>()

                        result?.forEach{ item ->
                            val id = item.asJsonObject.get("id").asInt
                            arrayOfIds.add(id)
                        }
                        callback(arrayOfIds)
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

    private fun getIsbnFromExipiredLoan(id: Int, callback: (String) -> Unit) {
        val query = "select isbn_book from loans where id = '$id' and status = 0;"
        ClientNetwork.retrofit.select(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        if (result.size() > 0) {
                            val isbnr = result[0].asJsonObject.get("isbn_book").asString
                            callback(isbnr)
                        }

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

    private fun downgradeRatingOfUser(username: String) {
        val query = "update user set conduct = conduct - 1 where username = '$username';"
        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        println(resultSizePrimitive)
                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }

    private fun changeStatusOfLoan(id: Int, username: String, callback: (Boolean) -> Unit) {
        val query = "update loans set status = 0 where id = '$id' and username_user = '$username';"

        ClientNetwork.retrofit.update(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        if (resultSize == "update executed!") {
                            callback(true)
                        } else {
                            callback(false)
                        }

                        println(resultSizePrimitive)
                    }else{
                        println("PROBLEMI on editcopies")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                }
            }
        )
    }

}

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
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentBookInfoBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils
import com.lorenzoprogramma.libraio.utils.UserViewModel
import retrofit2.Call
import retrofit2.Response

class BookInfoFragment : Fragment() {
    private lateinit var binding: FragmentBookInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookInfoBinding.inflate(inflater)

        val book = arguments?.getParcelable<Book>("book")
        val userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        val user = userViewModel.userVM
        if (book != null) {
            setUpBookInfo(book)
            if (user != null) {
                checkWishList(book.isbn!!, user.username!!) {result ->
                    if (result) { //E' stato gia' aggunto hai preferiti
                        binding.addToFavorites.setImageResource(R.drawable.baseline_favorite_24)
                    } else {
                        binding.addToFavorites.setImageResource(R.drawable.baseline_favorite_border_24)
                    }
                }
            }
        }

        binding.addToFavorites.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val isLogged = sharedPreferences.getBoolean("isLogged", false)
            println("isLogged: $isLogged")
            if (isLogged) {
                if (book != null && user != null) {
                    checkWishList(book.isbn!!, user.username!!) {isPresent ->
                        if (isPresent) { //E' presente
                            binding.addToFavorites.setImageResource(R.drawable.baseline_favorite_border_24)
                            removeFromWishList(book.isbn!!, user.username!!) {hasBeenRemoved ->
                                if (hasBeenRemoved) {
                                    Toast.makeText(context, "Libro rimosso dai preferiti", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            binding.addToFavorites.setImageResource(R.drawable.baseline_favorite_24)
                            addToWishList(book.isbn!!, user.username!!) {hasBeenAdded ->
                                if (hasBeenAdded) {
                                    Toast.makeText(context, "Libro aggiunto ai tuoi preferiti", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Devi essere loggato per poter accedere a questa funzionalita'", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageButtonBackToCatalog.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, CatalogFragment(), R.id.main_frame_layout)
        }



        return binding.root
    }

    private fun setUpBookInfo(book: Book) {
        binding.textViewBookTitle.text = book.title
        binding.textViewAuthor.text = book.author
        binding.textViewEditor.text = book.editor
        binding.textViewYear.text = book.year.toString()
        binding.imageViewCover.setImageBitmap(book.cover)
        binding.ratingBarBook.rating = book.rating?.toFloat() ?: 0f
        binding.textViewDescription.text = book.description

        if (book.nCopies!! == 0) {
            binding.imageViewDisponibility.setImageResource(R.drawable.icons8redcircle24)
            binding.textViewDisponibility.text = "Non disponibile"
            binding.textViewDisponibility.setTextColor(resources.getColor(R.color.red))
        } else {
            binding.imageViewDisponibility.setImageResource(R.drawable.icons8greencircle24)
            binding.textViewDisponibility.text = "Disponibile"
            binding.textViewDisponibility.setTextColor(resources.getColor(R.color.green))
        }

    }

    private fun checkWishList(isbn: String, username: String, callback: (Boolean) -> Unit) {
        val query = "select id from wishlist where isbn_book = '$isbn' and username_user = '$username';"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray).size() == 1
                        println(result)
                        callback(result)
                    }else{
                        println("PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                    println("Problem on checking the wishlist")
                }
            }
        )
    }

    private fun removeFromWishList(isbn: String, username: String, callback: (Boolean) -> Unit) {
        val query = "delete from wishlist where isbn_book = '$isbn' and username_user = '$username';"
        ClientNetwork.retrofit.delete(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSizePrimitive = (response.body()?.getAsJsonPrimitive("queryset"))
                        val resultSize = resultSizePrimitive?.asString

                        println(resultSizePrimitive)
                        if (resultSize != null) {
                            val registrationSuccessful = resultSize == "remove executed!"
                            println(registrationSuccessful)
                            callback(registrationSuccessful)
                        } else {
                            callback(false)
                        }
                    }else{
                        println("PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                    println("Problem on removing from the wishlist")
                }
            }
        )
    }

    private fun addToWishList(isbn: String, username: String, callback: (Boolean) -> Unit) {
        val query = "insert into wishlist (username_user, isbn_book) values ('$username', '$isbn');"

        ClientNetwork.retrofit.insert(query).enqueue(
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
}
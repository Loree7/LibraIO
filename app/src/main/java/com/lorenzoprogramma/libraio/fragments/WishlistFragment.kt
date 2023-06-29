package com.lorenzoprogramma.libraio.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.adapters.WishListAdapter
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.databinding.FragmentWishlistBinding
import com.lorenzoprogramma.libraio.viewmodels.UserViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishlistFragment : Fragment() {
    private lateinit var binding: FragmentWishlistBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWishlistBinding.inflate(inflater)

        binding.whishlistRecyclerView.layoutManager = GridLayoutManager(context, 2)
        val data = mutableListOf<Book>()
        val adapter = WishListAdapter(data)
        binding.whishlistRecyclerView.adapter = adapter

        val userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        val user = userViewModel.userVM
        if (user != null) {
            getIsbnOfBooksInMyWishlist(user.username!!) {isbnBooksInWishList ->
                println("size: ${isbnBooksInWishList.size}")
                if (isbnBooksInWishList.isEmpty()) {
                    showNoElementsDesign()
                } else {
                    for (isbn in isbnBooksInWishList) {
                        showBookRequested(isbn) {book ->
                            println("Book da aggiungere: $book")
                            data.add(book)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        adapter.setOnClickListener(object : WishListAdapter.OnClickListener {
            override fun onClick(position: Int, model: Book) {
                if (user != null) {
                    removeFromWishList(model.isbn!!, user.username!!) {hasBeenRemoved ->
                        if (hasBeenRemoved) {
                            data.remove(model)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(context, "Libro rimosso dai preferiti", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        })


        return binding.root
    }

    private fun getIsbnOfBooksInMyWishlist(username: String, callback: (ArrayList<String>) -> Unit) {
        val query = "select isbn_book from wishlist where username_user = '$username';"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        val resultSize = result.size()
                        println("result size: $resultSize")
                        //callback(result)
                        println("isbn ricevuto: $result")
                        val arrayOfIsbn = ArrayList<String>()
                        if (result.size() > 0) {
                            var completeCallbacks = 0

                            for (i in 0 until result.size()) {
                                val isbnR = result[i].asJsonObject.get("isbn_book").asString
                                println("isbn: $isbnR")
                                arrayOfIsbn.add(isbnR)
                                completeCallbacks++
                                if (completeCallbacks == result.size()) {
                                    callback(arrayOfIsbn)
                                }
                            }
                        } else {
                            callback(arrayOfIsbn)
                        }

                    }else{
                        println("PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                    println("Problem on Book request")
                }
            }
        )
    }

    private fun getBookCover(url: String, callback: (Bitmap?) -> Unit) {

        ClientNetwork.retrofit.get(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        if (response.body()!=null) {
                            val avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            callback(avatar)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    /*
                    * gestisci qui il fallimento della richiesta
                    */

                }

            }
        )
    }


    private fun showBookRequested(isbn: String, callback: (Book) -> Unit){
        val query = "select isbn, title, cover_path from book where isbn = '$isbn';"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        println(result)

                        val isbnR = result[0].asJsonObject.get("isbn").asString
                        val title = result[0].asJsonObject.get("title").asString
                        val cover = result[0].asJsonObject.get("cover_path").asString

                        getBookCover(cover) {coverImage ->
                            val book = Book(isbnR, title, null, coverImage, null, null, null, null, null, null)
                            println("getBook: $book")
                            callback(book)
                        }

                    }else{
                        println("PROBLEMI")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("retrofit", "ERRORE: ${t.message}", t)
                    println("Problem on Book request")
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
                            val removedSuccessfully = resultSize == "remove executed!"
                            println(removedSuccessfully)
                            callback(removedSuccessfully)
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

    private fun showNoElementsDesign() {
        binding.whishlistRecyclerView.visibility = View.GONE
        binding.imageViewNoElements.visibility = View.VISIBLE
        binding.textViewNoElements.visibility = View.VISIBLE
        binding.textViewNoElementsTip.visibility = View.VISIBLE
    }

}
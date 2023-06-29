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
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.adapters.AdapterClass
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.databinding.FragmentCatalogBinding
import com.lorenzoprogramma.libraio.viewmodels.CategoryViewModel
import com.lorenzoprogramma.libraio.utils.FragmentUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCatalogBinding.inflate(inflater)

//        val categoryRequested = argumentsxx?.getString("category")
        val categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
        val categoryRequested = categoryViewModel.categoryNameVM
        println("category: $categoryRequested")


        binding.searchBar.clearFocus()
        binding.catalogRecyclerView.layoutManager = GridLayoutManager(context,2)
        val data = mutableListOf<Book>()
        val adapter = AdapterClass(data)
        binding.catalogRecyclerView.adapter = adapter


        if (categoryRequested != null) {
            showBookOfCategory(categoryRequested) {books ->
                println("size ${books.size}")
                for (book in books) {
                    println("Books: $book")
                    data.add(book)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = arrayListOf<Book>()
                for (book in data) {
                    if (newText != null) {
                        if (book.isbn!!.toLowerCase().contains(newText.toLowerCase()) || book.title!!.toLowerCase().contains(newText.toLowerCase()) || book.author!!.toLowerCase().contains(newText.toLowerCase())) {
                            filteredList.add(book)
                        }
                    }
                }

                if (filteredList.isEmpty()) {
                    Toast.makeText(context, "Nessun libro trovato", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.setFilteredList(filteredList)
                }
                return true
            }

        })

        binding.imageButtonBackToCategories.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, BookCategoriesFragment(), R.id.main_frame_layout)
        }

        adapter.setOnClickListener(object : AdapterClass.OnClickListener {
            override fun onClick(position: Int, model: Book) {
                val isbnOfBookRequested = model.isbn
                if (isbnOfBookRequested != null) {
                    showBookRequested(isbnOfBookRequested) {book ->
                        val bookInfoFragment = BookInfoFragment()
                        val bundle = Bundle()
                        bundle.putParcelable("book", book)
                        bookInfoFragment.arguments = bundle
                        FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, bookInfoFragment, R.id.main_frame_layout)
                    }
                }
            }

        })

        return binding.root

    }
    private fun showBookOfCategory(category: String, callback: (ArrayList<Book>) -> Unit) {
        val query = "select isbn, title, author, cover_path, type from book where type = '$category';"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        //callback(result)
                        println(result)
                        val arrayOfBooks = ArrayList<Book>()
                        var completeCallbacks = 0

                        for (i in 0 until result.size()) {
                            val isbnR = result[i].asJsonObject.get("isbn").asString
                            val title = result[i].asJsonObject.get("title").asString
                            val author = result[i].asJsonObject.get("author").asString
                            val cover = result[i].asJsonObject.get("cover_path").asString
                            val type = result[i].asJsonObject.get("type").asString

                            getBookCover(cover) {coverImage ->
                                val book = Book(isbnR, title, author, coverImage, type, null, null, null, null, null)
                                println("Libro: $book")
                                arrayOfBooks.add(book)
                                completeCallbacks++

                                if (completeCallbacks == result.size()) {
                                    callback(arrayOfBooks)
                                }
                            }
                        }
//                        println("Array of books ${arrayOfBooks[0]}")

//                        val isbnR = result[0].asJsonObject.get("isbn").asString
//                        val title = result[0].asJsonObject.get("title").asString
//                        val author = result[0].asJsonObject.get("author").asString
//                        val cover = result[0].asJsonObject.get("cover_path").asString
//                        val type = result[0].asJsonObject.get("type").asString

//                        getBookCover(cover) {coverImage ->
//                            val book = Book(isbnR, title, author, coverImage, type)
//                            println("getBook: $book")
//                            callback(book)
//                        }
//                        val book = Book(isbnR, title, author, null)
//                        callback(book)

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
    private fun showBookRequested(isbn: String, callback: (Book) -> Unit){

        val query = "select isbn, title, author, cover_path, year, editor, type, number_of_copies, description, rating from book where isbn = '$isbn';"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        println(result)

                        val isbnR = result[0].asJsonObject.get("isbn").asString
                        val title = result[0].asJsonObject.get("title").asString
                        val author = result[0].asJsonObject.get("author").asString
                        val year = result[0].asJsonObject.get("year").asInt
                        val editor = result[0].asJsonObject.get("editor").asString
                        val type = result[0].asJsonObject.get("type").asString
                        val copies = result[0].asJsonObject.get("number_of_copies").asInt
                        val description = result[0].asJsonObject.get("description").asString
                        val rating = result[0].asJsonObject.get("rating").asInt
                        val cover = result[0].asJsonObject.get("cover_path").asString

                        getBookCover(cover) {coverImage ->
                            val book = Book(isbnR, title, author, coverImage, type, year, editor, copies, description, rating)
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
}
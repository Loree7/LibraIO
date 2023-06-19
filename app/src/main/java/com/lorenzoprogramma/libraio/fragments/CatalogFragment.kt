package com.lorenzoprogramma.libraio.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.adapters.AdapterClass
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.databinding.FragmentCatalogBinding
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


        binding.catalogRecyclerView.layoutManager = GridLayoutManager(context,2)
        val data = mutableListOf<Book>()
        val adapter = AdapterClass(data)
        binding.catalogRecyclerView.adapter = adapter


        addBook("9788807924286"){ book ->
            println("BOOK: $book")
            data.add(book)
            adapter.notifyDataSetChanged()
        }
//        addBook("9788858045169") { book ->
//            data.add(book)
//            adapter.notifyDataSetChanged()
//        }
        println("Data: $data")

        return binding.root

    }
    private fun addBook(isbn: String, callback: (Book) -> Unit){

        val query = "select isbn, title, author, cover_path from book where isbn = '$isbn';"
        ClientNetwork.retrofit.getBook(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        //callback(result)
                        println(result)

                        val isbnR = result[0].asJsonObject.get("isbn").asString
                        val title = result[0].asJsonObject.get("title").asString
                        val author = result[0].asJsonObject.get("author").asString
                        val cover = result[0].asJsonObject.get("cover_path").asString

                        getBookCover(cover) {coverImage ->
                            val book = Book(isbnR, title, author, coverImage)
                            println("getBook: $book")
                            callback(book)
                        }
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

    private fun getBookCover(url: String, callback: (Bitmap?) -> Unit) {

        ClientNetwork.retrofit.getCover(url).enqueue(
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
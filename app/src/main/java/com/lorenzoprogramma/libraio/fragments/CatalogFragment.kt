package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.databinding.FragmentCatalogBinding
import retrofit2.Call
import retrofit2.Response

class CatalogFragment : Fragment() {
    private lateinit var binding: FragmentCatalogBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<Book>
    lateinit var imageList: ArrayList<Int>
    lateinit var titleList: ArrayList<String>
    lateinit var authorList: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCatalogBinding.inflate(inflater)


        binding.catalogRecyclerView.layoutManager = GridLayoutManager(context,3)

        val data = ArrayList<Book>()
        /*for (){ //da modificare per determinare la grandezza della lista
            //aggiungere i dati
        }*/
        addBook("9788807924286"){book ->
            data.add(book)
        }


        val adapter = AdapterClass(data)
        binding.catalogRecyclerView.adapter = adapter

        return binding.root

    }
    fun addBook(isbn:String, callback: (Book) -> Unit){

        val query = "select isbn, cover_path, title, author from book where isbn = '$isbn';"
        ClientNetwork.retrofit.getBook(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        //callback(result)
                        println(result)

                        val isbn = result[0].asJsonObject.get("isbn").asString
                        val cover = result[0].asJsonObject.get("cover_path").asString
                        val title = result[0].asJsonObject.get("title").asString
                        val author = result[0].asJsonObject.get("author").asString
                        val book = Book(isbn, cover, title, author)
                        callback(book)
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
}
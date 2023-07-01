package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.adapters.AdapterClass
import com.lorenzoprogramma.libraio.adapters.EventsAdapter
import com.lorenzoprogramma.libraio.api.ClientNetwork
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.data.Categories
import com.lorenzoprogramma.libraio.data.Events
import com.lorenzoprogramma.libraio.databinding.FragmentEventsBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils
import retrofit2.Call
import retrofit2.Response

class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventsBinding.inflate(inflater)
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(context)


        val data = ArrayList<Events>()
        val adapter = EventsAdapter(data)
        binding.eventsRecyclerView.adapter = adapter
        EventsAdapter(data)


        getTitles { arrayOfTitles ->
            if(arrayOfTitles.isEmpty()){
                println("Vuoto")
            }else{
                for(title in arrayOfTitles){
                    getEvents(title) {event ->
                        data.add(event)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }



        binding.eventsRecyclerView.setHasFixedSize(true)


        binding.imageButtonBackToHome.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, HomeFragment(), R.id.main_frame_layout)
        }

        return binding.root
        }
    private fun getEvents(title : String, callback: (Events) -> Unit) {
        val query = "select author, title, start_date, categorie from events where title = '$title';"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        println(result)
                        val title = result[0].asJsonObject.get("title").asString
                        val author = result[0].asJsonObject.get("author").asString
                        val date = result[0].asJsonObject.get("start_date").asString
                        val categorie = result[0].asJsonObject.get("categorie").asString
                        val event = Events(title, author, date, categorie)
                        callback(event)
                    } else{
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
    private fun getTitles(callback: (ArrayList<String>) -> Unit) {
        val query = "select title from events;"
        ClientNetwork.retrofit.select(query).enqueue(
            object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val result = (response.body()?.get("queryset") as JsonArray)
                        println(result)
                        val arrayOfTitle = ArrayList<String>()
                        var completeCallbacks = 0
                        for (i in 0 until result.size()) {
                            val title = result[i].asJsonObject.get("title").asString
                            //val author = result[i].asJsonObject.get("author").asString
                            //val cover = result[i].asJsonObject.get("cover_path").asString
                            //val date = result[i].asJsonObject.get("cover_path").asString
                            //val type = result[i].asJsonObject.get("type").asString
                            //val categorie = result[i].asJsonObject.get("type").asString
//-----------------------------------------------------
                                arrayOfTitle.add(title)
                                completeCallbacks++

                                if (completeCallbacks == result.size()) {
                                    callback(arrayOfTitle)
                                }
                            }
                        } else{
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
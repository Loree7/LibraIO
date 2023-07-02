package com.lorenzoprogramma.libraio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lorenzoprogramma.libraio.data.Events
import com.lorenzoprogramma.libraio.databinding.EventCellDesignBinding
import java.time.format.DateTimeFormatter

class EventsAdapter(private val eventsList : ArrayList<Events> ) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    class ViewHolder(binding: EventCellDesignBinding): RecyclerView.ViewHolder(binding.root) {
        val editTextAuthor = binding.editTextAuthor
        val editTextTitle = binding.editTextTitle
        val editTextDate = binding.editTextDate
        val editTextCategories = binding.editTextCategories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = EventCellDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventsList[position]
        holder.editTextAuthor.text = event.authorEditText
        holder.editTextTitle.text = event.titleEditText
        holder.editTextDate.text = event.dateEditText
        holder.editTextCategories.text = event.categoriesEditText

    }
}
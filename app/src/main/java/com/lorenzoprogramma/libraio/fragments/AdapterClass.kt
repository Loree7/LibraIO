package com.lorenzoprogramma.libraio.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.databinding.CardCellBinding
import com.lorenzoprogramma.libraio.databinding.FragmentCatalogBinding

class AdapterClass(private val dataList: List<Book>) : RecyclerView.Adapter<AdapterClass.ViewHolder>() {

    class ViewHolder(binding : CardCellBinding): RecyclerView.ViewHolder(binding.root) {
        val bookImage:ImageView = binding.imageViewCover
        val bookTitle:TextView = binding.textViewTitle
//        val bookAuthor:TextView = binding.textViewAuthor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardCellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bookImage.setImageBitmap(currentItem.cover)
        holder.bookTitle.text = currentItem.title
//        holder.bookAuthor.text = currentItem.author
    }


}
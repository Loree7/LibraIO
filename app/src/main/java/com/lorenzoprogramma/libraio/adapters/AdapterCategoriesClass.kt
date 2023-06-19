package com.lorenzoprogramma.libraio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lorenzoprogramma.libraio.data.Categories
import com.lorenzoprogramma.libraio.databinding.BookCategoriesObjectsBinding
import com.lorenzoprogramma.libraio.databinding.FragmentBookCategoriesBinding

class AdapterCategoriesClass(private val dataList: List<Categories>) : RecyclerView.Adapter<AdapterCategoriesClass.ViewHolder>() {

    class ViewHolder(binding : BookCategoriesObjectsBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageViewCategories: ImageView = binding.imageViewCategories
        val textViewCategories: TextView = binding.textViewCategories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = BookCategoriesObjectsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.imageViewCategories.setImageBitmap(currentItem.categoriesImage)
        holder.textViewCategories.text = currentItem.categoriesName
    }
}
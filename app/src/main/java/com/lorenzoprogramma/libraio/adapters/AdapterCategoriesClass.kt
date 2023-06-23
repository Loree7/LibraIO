package com.lorenzoprogramma.libraio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lorenzoprogramma.libraio.data.Categories
import com.lorenzoprogramma.libraio.databinding.BookCategoriesObjectsBinding
import com.lorenzoprogramma.libraio.databinding.FragmentBookCategoriesBinding

class AdapterCategoriesClass(private val categoriesList: List<Categories>) : RecyclerView.Adapter<AdapterCategoriesClass.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    class ViewHolder(binding : BookCategoriesObjectsBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageViewCategories: ImageView = binding.imageViewCategories
        val textViewCategories: TextView = binding.textViewCategories
        val cardView: CardView = binding.cardViewBookCategories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = BookCategoriesObjectsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoriesList[position]
        holder.imageViewCategories.setImageResource(category.categoriesImage)
        holder.textViewCategories.text = category.categoriesName
        holder.cardView.setOnClickListener {
            onClickListener?.onClick(position, category)
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Categories)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}
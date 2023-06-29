package com.lorenzoprogramma.libraio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.data.Categories
import com.lorenzoprogramma.libraio.databinding.CardCellBinding

class AdapterClass(private var dataList: List<Book>) : RecyclerView.Adapter<AdapterClass.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    class ViewHolder(binding : CardCellBinding): RecyclerView.ViewHolder(binding.root) {
        val bookImage: ImageView = binding.imageViewCover
        val bookTitle: TextView = binding.textViewTitle
        val bookCard: CardView = binding.cardViewCatalog
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
        holder.bookCard.setOnClickListener {
            onClickListener?.onClick(position, currentItem)
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Book)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setFilteredList(filteredList: ArrayList<Book>) {
        this.dataList = filteredList
        notifyDataSetChanged()
    }


}
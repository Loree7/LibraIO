package com.lorenzoprogramma.libraio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.databinding.WishlistCellsBinding

class WishListAdapter(private val bookList: List<Book>) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class ViewHolder(binding: WishlistCellsBinding): RecyclerView.ViewHolder(binding.root) {
        val imageButtonFav = binding.imageButtonFavourites
        val textViewTitle = binding.textViewTitleWhishlist
        val imageViewCover = binding.imageViewCoverWishlist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = WishlistCellsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val book = bookList[position]
       holder.textViewTitle.text = book.title
       holder.imageViewCover.setImageBitmap(book.cover)
       holder.imageButtonFav.setOnClickListener {
           onClickListener?.onClick(position, book)
       }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Book)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}
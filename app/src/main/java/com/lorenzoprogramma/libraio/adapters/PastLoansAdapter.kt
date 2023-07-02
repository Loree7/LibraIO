package com.lorenzoprogramma.libraio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lorenzoprogramma.libraio.data.Book
import com.lorenzoprogramma.libraio.data.Loans
import com.lorenzoprogramma.libraio.databinding.LoansCellDesignBinding
import java.time.format.DateTimeFormatter

class PastLoansAdapter(private val bookList: List<Book>, private val loansList: List<Loans>) : RecyclerView.Adapter<PastLoansAdapter.ViewHolder>() {

    class ViewHolder(binding: LoansCellDesignBinding): RecyclerView.ViewHolder(binding.root) {
        val imageViewCover = binding.imageViewCover
        val textViewTitle = binding.textViewTitle
        val textViewDate = binding.textViewDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LoansCellDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        val loan = loansList[position]
        holder.textViewTitle.text = book.title
        holder.imageViewCover.setImageBitmap(book.cover)
        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formattedDateTime = loan.endDate.format(dateTimeFormat)

        holder.textViewDate.text = formattedDateTime
    }
}
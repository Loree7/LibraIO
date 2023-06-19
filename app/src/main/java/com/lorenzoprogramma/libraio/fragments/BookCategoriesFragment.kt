package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.adapters.AdapterCategoriesClass
import com.lorenzoprogramma.libraio.adapters.AdapterClass
import com.lorenzoprogramma.libraio.data.Categories
import com.lorenzoprogramma.libraio.databinding.FragmentBookCategoriesBinding


class BookCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentBookCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookCategoriesBinding.inflate(inflater)

        binding.bookCategoriesRecyclerView.layoutManager = LinearLayoutManager(context)

        val data = arrayListOf<Categories>()
        val adapter = AdapterCategoriesClass(data)
        binding.bookCategoriesRecyclerView.adapter = adapter


        //popolare con le immagini e i nomi delle categorie





        return binding.root
    }


}
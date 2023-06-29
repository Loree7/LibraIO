package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.adapters.AdapterCategoriesClass
import com.lorenzoprogramma.libraio.data.Categories
import com.lorenzoprogramma.libraio.databinding.FragmentBookCategoriesBinding
import com.lorenzoprogramma.libraio.viewmodels.CategoryViewModel
import com.lorenzoprogramma.libraio.utils.FragmentUtils


class BookCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentBookCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookCategoriesBinding.inflate(inflater)

        binding.bookCategoriesRecyclerView.layoutManager = LinearLayoutManager(context)

        val categoriesList = mutableListOf<Categories>()
        val adapter = AdapterCategoriesClass(categoriesList)
        binding.bookCategoriesRecyclerView.adapter = adapter
        val categories = arrayOf("arte", "fantasy", "horror", "psicologia", "romanzo", "sport", "storia", "thriller")

        for (categoryName in categories) {
            val imageResourceId = resources.getIdentifier(
                categoryName,
                "drawable",
                requireContext().packageName
            )
            categoriesList.add(Categories(imageResourceId, categoryName))
            println(categoriesList)
        }
        adapter.notifyDataSetChanged()

        adapter.setOnClickListener(object : AdapterCategoriesClass.OnClickListener {
            override fun onClick(position: Int, model: Categories) {
                val catalogFragment = CatalogFragment()
                val categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
                categoryViewModel.categoryNameVM = model.categoriesName
                val bundle = Bundle()
                bundle.putString("category", model.categoriesName)
                catalogFragment.arguments = bundle
                FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, catalogFragment, R.id.main_frame_layout)
            }

        })

        binding.imageButtonBackToHome.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, HomeFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(true)
        }

        return binding.root
    }


}
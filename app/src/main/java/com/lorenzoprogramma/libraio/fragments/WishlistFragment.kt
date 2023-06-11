package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.databinding.FragmentWishlistBinding

private lateinit var binding: FragmentWishlistBinding

    class WishlistFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
     ): View? {
            binding = FragmentWishlistBinding.inflate(inflater)
            return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }
}
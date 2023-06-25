package com.lorenzoprogramma.libraio.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.databinding.FragmentContactsBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils


class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactsBinding.inflate(layoutInflater)

        binding.imageButtonBackToCategories.setOnClickListener(){
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, HomeFragment(), R.id.main_frame_layout)
        }

        binding.textViewWebPage.setOnClickListener(){
            val url = "https://www.unipa.it/amministrazione/direzionegenerale/sba/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        return binding.root
    }


}

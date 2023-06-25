package com.lorenzoprogramma.libraio.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentCatalogBinding
import com.lorenzoprogramma.libraio.databinding.FragmentHomeBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils
import com.lorenzoprogramma.libraio.utils.UserViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.imageViewAccountInfo.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val isLogged = sharedPreferences.getBoolean("isLogged", false)
            if (isLogged) {
//                val user = arguments?.getParcelable<User>("user")
//                if (user != null) {
//                    val accountInfoFragment = AccountInfoFragment()
//                    val bundle = Bundle()
//                    bundle.putParcelable("user", user)
//                    accountInfoFragment.arguments = bundle
//                    FragmentUtils.replaceFragment(requireActivity().supportFragmentManager,
//                        accountInfoFragment, R.id.main_frame_layout)
//                    (activity as? MainActivity)?.toggleBottomNavigationView(false)
//                }
                FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, AccountInfoFragment(), R.id.main_frame_layout)
                (activity as? MainActivity)?.toggleBottomNavigationView(false)
            } else {
                Toast.makeText(context, "Non puoi accedere a questa sezione come ospite", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageViewCatalog.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, BookCategoriesFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(false)
        }

        binding.imageViewContacts.setOnClickListener(){
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, ContactsFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(false)
        }

        return binding.root
    }


}
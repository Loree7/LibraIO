package com.lorenzoprogramma.libraio.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.databinding.FragmentHomeBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        val isExpired = arguments?.getBoolean("exipired")
        println("isExpired: $isExpired")
        if (isExpired != null && isExpired) {
            Toast.makeText(context, "Il prestito di alcuni libri e' scaduto, la prossima volta ricorda di restituirli in anticipo", Toast.LENGTH_LONG).show()
        }

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

        binding.imageButtonEvents.setOnClickListener {
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, EventsFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(false)
        }

        binding.imageViewContacts.setOnClickListener(){
            FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, ContactsFragment(), R.id.main_frame_layout)
            (activity as? MainActivity)?.toggleBottomNavigationView(false)
        }
        binding.imageViewLoans.setOnClickListener(){
            val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val isLogged = sharedPreferences.getBoolean("isLogged", false)
            if (isLogged) {
                FragmentUtils.replaceFragment(requireActivity().supportFragmentManager, LoansFragment(), R.id.main_frame_layout)
                (activity as? MainActivity)?.toggleBottomNavigationView(false)
            } else {
                Toast.makeText(context, "Non puoi accedere a questa sezione come ospite", Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }


}
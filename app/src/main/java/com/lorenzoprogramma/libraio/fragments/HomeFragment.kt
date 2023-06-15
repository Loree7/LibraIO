package com.lorenzoprogramma.libraio.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lorenzoprogramma.libraio.MainActivity
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.data.User
import com.lorenzoprogramma.libraio.databinding.FragmentCatalogBinding
import com.lorenzoprogramma.libraio.databinding.FragmentHomeBinding
import com.lorenzoprogramma.libraio.utils.FragmentUtils

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.imageViewAccountInfo.setOnClickListener {
            val user = arguments?.getParcelable<User>("user")
            println("Ciao, $user")
            if (user != null) {
                val accountInfoFragment = AccountInfoFragment()
                val bundle = Bundle()
                bundle.putParcelable("user", user)
                accountInfoFragment.arguments = bundle
                openAccountInfo(accountInfoFragment)
            }

        }


        return binding.root
    }

    private fun openAccountInfo(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction().add(R.id.main_frame_layout, fragment).commit()
        (activity as? MainActivity)?.toggleBottomNavigationView(false)
    }
}
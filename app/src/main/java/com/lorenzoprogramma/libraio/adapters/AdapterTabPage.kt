package com.lorenzoprogramma.libraio.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lorenzoprogramma.libraio.fragments.ActiveLoansFragment
import com.lorenzoprogramma.libraio.fragments.PastLoansFragment

class AdapterTabPage (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 2 //sono solo 2 pagine
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0){ //crea il Fragment con i prestiti in vigore
                ActiveLoansFragment()
            }else{      //crea il Fragment con i prestiti passati
                PastLoansFragment()
             }
    }
}
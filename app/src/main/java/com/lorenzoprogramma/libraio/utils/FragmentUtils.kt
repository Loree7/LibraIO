package com.lorenzoprogramma.libraio.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentUtils {
    companion object {
        fun addFragment(manager: FragmentManager, fragment: Fragment, container: Int) {
            val transaction = manager.beginTransaction()
            transaction.add(container, fragment)
            transaction.commit()
        }

        fun replaceFragment(manager: FragmentManager, fragment: Fragment, container: Int) {
            val transaction = manager.beginTransaction()
            transaction.replace(container, fragment)
            transaction.commit()
        }
    }
}
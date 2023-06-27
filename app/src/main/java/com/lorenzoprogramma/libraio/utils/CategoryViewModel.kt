package com.lorenzoprogramma.libraio.utils

import android.view.View
import androidx.lifecycle.ViewModel
import com.lorenzoprogramma.libraio.data.Categories

class CategoryViewModel: ViewModel() {
    var categoryVm: Categories? = null
    var categoryNameVM: String? = null
}
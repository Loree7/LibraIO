package com.lorenzoprogramma.libraio.data

import android.widget.EditText
import android.widget.TextView
import java.util.zip.DataFormatException

data class Events(
    val authorEditText: String,
    val titleEditText: String,
    val dateEditText: String,
    val categoriesEditText: String
)

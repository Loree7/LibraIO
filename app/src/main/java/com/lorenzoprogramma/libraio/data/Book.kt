package com.lorenzoprogramma.libraio.data

import android.graphics.Bitmap

data class Book(
    val isbn: String?,
    val title: String?,
    val author: String?,
    val cover: Bitmap?
)

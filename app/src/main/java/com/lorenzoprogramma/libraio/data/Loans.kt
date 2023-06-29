package com.lorenzoprogramma.libraio.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class Loans(
    val isbnBook: String?,
    val userUsername: String?,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val status: Boolean
)

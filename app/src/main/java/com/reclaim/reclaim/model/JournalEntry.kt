package com.reclaim.reclaim.model

import java.time.LocalDate

data class JournalEntry(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val date: String,
    val time: String
)
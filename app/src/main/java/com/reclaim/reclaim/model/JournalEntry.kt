package com.reclaim.reclaim.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Represents a single journal entry to be stored in Firestore.
 * This is the single source of truth for the JournalEntry data model.
 */
data class JournalEntry(
    val id: String = "",
    val text: String = "",
    val date: String = "",
    val time: String = "",
    @ServerTimestamp val createdAt: Date? = null // For sorting
)

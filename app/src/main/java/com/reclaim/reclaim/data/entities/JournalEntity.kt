package com.reclaim.reclaim.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ServerTimestamp


/**
 * JournalEntity
 * -------------
 * Room entity representing a journal entry.
 * - Stored in reclaim_database.
 * - Linked to JournalDao for CRUD operations.
 *
 * TODO:
 * - Add fields for mood rating, tags, or linked strategies.
 * - Consider indexing date for faster queries.
 * - Add optional encryption for sensitive content.
 */

@Entity(tableName = "journals")
data class  JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val date: String,
    val time: String,

    @ServerTimestamp
    val createdAt: Long
)
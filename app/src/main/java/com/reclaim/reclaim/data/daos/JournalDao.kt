package com.reclaim.reclaim.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.reclaim.reclaim.data.entities.JournalEntity
import kotlinx.coroutines.flow.Flow

/**
 * JournalDao
 * ----------
 * Data Access Object for JournalEntity.
 * - Defines database operations for journal entries.
 * - Injected into ViewModels via Hilt.
 *
 * TODO:
 * - Add queries for filtering by date, mood, or tags.
 * - Add ordering (e.g., newest first).
 * - Consider pagination for large datasets.
 */

@Dao
interface JournalDao {
    @Query("SELECT * FROM journals ORDER BY id DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Insert
    suspend fun  insertJournal(journal: JournalEntity)


    @Query("DELETE FROM journals WHERE id = :id")
    suspend fun deleteJournal(id: Int)

}
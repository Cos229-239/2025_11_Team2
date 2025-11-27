package com.reclaim.reclaim.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.reclaim.reclaim.data.entities.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journals ORDER BY id DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Insert
    suspend fun  insertJournal(journal: JournalEntity)


    @Query("DELETE FROM journals WHERE id = :id")
    suspend fun deleteJournal(id: Int)

}
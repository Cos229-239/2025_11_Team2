package com.reclaim.reclaim.data.daos

import androidx.room.*
import com.reclaim.data.entities.MoodEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MoodDao{
    @Query("SELECT * FROM mood_entries ORDER BY date DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE date = :date LIMIT 1")
    suspend fun getMoodByDate(date: LocalDate): MoodEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntry)


}
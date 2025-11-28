package com.reclaim.reclaim.data.daos

import androidx.room.*
import com.reclaim.reclaim.data.entities.TriggerEntity
import kotlinx.coroutines.flow.Flow

/**
 * TriggerDao
 * ----------
 * Data Access Object for TriggerEntity.
 * - Defines database operations for triggers.
 * - Used by ViewModels via Hilt injection.
 *
 * TODO:
 * - Add update/delete methods for triggers.
 * - Add queries for filtering triggers by category or date.
 */

@Dao
interface TriggerDao {

    // --- Core queries ---
    @Query("SELECT * FROM triggers ORDER BY id DESC")
    fun getAllTriggers(): Flow<List<TriggerEntity>>

    @Query("SELECT * FROM triggers WHERE id = :id")
    suspend fun getTriggerById(id: Int): TriggerEntity?

    // Because LocalDate is stored as a String via Converters
    @Query("SELECT * FROM triggers WHERE date = :date")
    fun getTriggersForDate(date: String): Flow<List<TriggerEntity>>

    // --- Insert / Update / Delete ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trigger: TriggerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(triggers: List<TriggerEntity>)

    @Update
    suspend fun update(trigger: TriggerEntity)

    @Delete
    suspend fun delete(trigger: TriggerEntity)

    @Query("DELETE FROM triggers WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM triggers")
    suspend fun clearAll()
}

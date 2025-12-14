package com.reclaim.reclaim.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.reclaim.reclaim.data.entities.StrategyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StrategyDao {
    @Query("SELECT * FROM StrategyEntity")
    fun getAllStrategies(): Flow<List<StrategyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(strategies: List<StrategyEntity>)

    @Update
    suspend fun update(strategy: StrategyEntity)

    @Delete
    suspend fun delete(strategy: StrategyEntity)  // NEW: For deleting customs
}
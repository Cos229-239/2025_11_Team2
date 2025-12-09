package com.reclaim.reclaim.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reclaim.reclaim.data.entities.StrategyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StrategyDao {
    @Query("SELECT * FROM strategies")
    fun getAllStrategies(): Flow<List<StrategyEntity>>

    @Insert
    suspend fun insert(strategy: StrategyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(strategies: List<StrategyEntity>)

    @Update
    suspend fun update(strategy: StrategyEntity)
}
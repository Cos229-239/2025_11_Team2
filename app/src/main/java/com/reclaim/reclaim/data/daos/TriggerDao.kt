package com.reclaim.reclaim.data.daos

import androidx.room.*
import com.reclaim.reclaim.data.entities.TriggerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TriggerDao {
    @Query("SELECT * FROM triggers")
    fun getAllTriggers(): Flow<List<TriggerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trigger: TriggerEntity)

    @Update
    suspend fun update(trigger: TriggerEntity)

    @Delete
    suspend fun delete(trigger: TriggerEntity)
}

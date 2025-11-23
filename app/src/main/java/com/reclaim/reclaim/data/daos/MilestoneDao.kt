package com.reclaim.reclaim.data.daos

import androidx.room.*
import com.reclaim.reclaim.data.entities.MilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM milestones ORDER BY dayCount ASC")
    fun getAllMilestones(): Flow<List<MilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMilestone(milestone: MilestoneEntity)

    @Query("SELECT * FROM milestones WHERE dayCount = :day LIMIT 1")
    suspend fun getMilestoneByDay(day: Long): MilestoneEntity?
}

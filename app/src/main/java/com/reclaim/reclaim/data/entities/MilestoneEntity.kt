package com.reclaim.reclaim.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "milestones")
data class MilestoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayCount: Long,
    val dateReached: LocalDate
)

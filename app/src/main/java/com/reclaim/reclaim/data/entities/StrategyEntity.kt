package com.reclaim.reclaim.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strategies")
data class StrategyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val triggerName: String,
    val strategy: String,
    val isFavorite: Boolean = false
)

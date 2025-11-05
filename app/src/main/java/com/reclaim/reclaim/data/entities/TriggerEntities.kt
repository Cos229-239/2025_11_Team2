package com.reclaim.reclaim.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TriggerEntity(
    @PrimaryKey val name: String,
    val color: Int,
    val iconName: String
)

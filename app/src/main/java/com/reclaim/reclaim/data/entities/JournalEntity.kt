package com.reclaim.reclaim.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class  JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val date: String,
    val time: String

)
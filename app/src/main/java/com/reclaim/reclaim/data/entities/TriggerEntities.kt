package com.reclaim.reclaim.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reclaim.reclaim.ui.trigger.TriggerType
import java.time.LocalDate

/**
 * TriggerEntity
 * -------------
 * Room entity representing a trigger.
 * - Stored in the reclaim_database.
 *
 * TODO:
 * - Add fields for severity, notes, or linked strategies.
 * - Consider indexing frequently queried fields.
 */

@Entity(tableName = "triggers")
data class TriggerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val type: TriggerType,
    val description: String? = null,
    val linkedStrategyIds: List<Int> = emptyList()
)



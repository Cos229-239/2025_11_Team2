package com.reclaim.reclaim.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class TriggerWithStrategies(
    @Embedded val trigger: TriggerEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "triggerName"
    )
    val strategies: List<StrategyEntity>
)

package com.reclaim.reclaim.ui.trigger

import androidx.compose.ui.graphics.Color
import com.reclaim.reclaim.ui.theme.*

enum class TriggerType {
    STRESS,
    LONELINESS,
    DEPRESSION,
    BOREDOM,
    SOCIAL_EVENTS,
    HEALTH,
    LIFE_CHANGES
}

fun TriggerType.prettyName(): String = when (this) {
    TriggerType.STRESS -> "Stress"
    TriggerType.LONELINESS -> "Loneliness"
    TriggerType.DEPRESSION -> "Depression"
    TriggerType.BOREDOM -> "Boredom"
    TriggerType.SOCIAL_EVENTS -> "Social events"
    TriggerType.HEALTH -> "Health"
    TriggerType.LIFE_CHANGES -> "Life changes"
}

fun triggerColor(trigger: TriggerType): Color = when (trigger) {
    TriggerType.STRESS -> Red
    TriggerType.LONELINESS -> Blue
    TriggerType.DEPRESSION -> Violet
    TriggerType.BOREDOM -> Orange
    TriggerType.SOCIAL_EVENTS -> Turquoise
    TriggerType.HEALTH -> Green
    TriggerType.LIFE_CHANGES -> PurpleAccent // or another theme color
}

fun java.time.LocalDate.toPrettyString(): String {
    val monthName = month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$dayOfMonth $monthName $year"
}
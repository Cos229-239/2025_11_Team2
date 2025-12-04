package com.reclaim.reclaim.ui.trigger

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.reclaim.reclaim.data.model.TriggerType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Nicely formatted names for each trigger type
fun TriggerType.prettyName(): String = when (this) {
    TriggerType.STRESS       -> "Stress"
    TriggerType.LONELINESS   -> "Loneliness"
    TriggerType.DEPRESSION   -> "Depression"
    TriggerType.BOREDOM      -> "Boredom"
    TriggerType.SOCIAL_EVENTS-> "Social events"
    TriggerType.HEALTH       -> "Health"
    TriggerType.LIFE_CHANGES -> "Life changes"
    TriggerType.EMOTIONAL -> TODO()
    TriggerType.PHYSICAL -> TODO()
    TriggerType.ENVIRONMENTAL -> TODO()
    TriggerType.SOCIAL -> TODO()
    TriggerType.COGNITIVE -> TODO()
}

/**
 * Color used for the little dots under each day in the calendar.
 * Now uses your Material theme instead of hard-coded Red/Blue/etc.
 */
@Composable
fun triggerColor(trigger: TriggerType): Color {
    val scheme = MaterialTheme.colorScheme

    return when (trigger) {
        TriggerType.STRESS        -> scheme.primary
        TriggerType.LONELINESS    -> scheme.secondary
        TriggerType.DEPRESSION    -> scheme.primary.copy(alpha = 0.7f)
        TriggerType.BOREDOM       -> scheme.surfaceVariant
        TriggerType.SOCIAL_EVENTS -> scheme.outline
        TriggerType.HEALTH        -> scheme.secondary.copy(alpha = 0.8f)
        TriggerType.LIFE_CHANGES  -> scheme.surfaceVariant
        TriggerType.EMOTIONAL -> TODO()
        TriggerType.PHYSICAL -> TODO()
        TriggerType.ENVIRONMENTAL -> TODO()
        TriggerType.SOCIAL -> TODO()
        TriggerType.COGNITIVE -> TODO()
    }
}

/**
 * Formats a LocalDate like: "November 30, 2025"
 */
fun LocalDate.toPrettyString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
    return this.format(formatter)
}

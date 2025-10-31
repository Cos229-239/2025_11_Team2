package com.reclaim.reclaim.ui.trigger

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class Trigger(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun TriggerGrid() {
    val triggers = listOf(
        Trigger("Stress", Icons.Default.Warning, Color(0xFFE57373)),
        Trigger("Loneliness", Icons.Default.Person, Color(0xFF64B5F6)),
        Trigger("Boredom", Icons.Default.HourglassEmpty, Color(0xFFFFB74D)),
        Trigger("Celebration", Icons.Default.EmojiEvents, Color(0xFF81C784))
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        triggers.forEach { trigger ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: Expand or link strategy */ },
                colors = CardDefaults.cardColors(containerColor = trigger.color),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(trigger.icon, contentDescription = trigger.name)
                    Text(trigger.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

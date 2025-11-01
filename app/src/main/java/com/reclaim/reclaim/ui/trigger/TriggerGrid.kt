package com.reclaim.reclaim.ui.trigger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateContentSize


data class Trigger(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val strategies: List<String> = emptyList()
)

@Composable
fun TriggerGrid(draggedStrategy: String?) {
    val triggers = remember {
        mutableStateListOf(
            Trigger("Stress", Icons.Default.Warning, Color(0xFFE57373), listOf("Deep breathing", "Go for a walk")),
            Trigger("Loneliness", Icons.Default.Person, Color(0xFF64B5F6), listOf("Call a friend")),
            Trigger("Boredom", Icons.Default.HourglassEmpty, Color(0xFFFFB74D), listOf("Read a book")),
            Trigger("Celebration", Icons.Default.EmojiEvents, Color(0xFF81C784), listOf("Reflect on progress"))
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        triggers.forEachIndexed { index, trigger ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(trigger.name) {
                        detectDragGestures(
                            onDragStart = {},
                            onDrag = { _, _ -> },
                            onDragEnd = {
                                draggedStrategy?.let { strategy ->
                                    if (!trigger.strategies.contains(strategy)) {
                                        triggers[index] = trigger.copy(
                                            strategies = trigger.strategies + strategy
                                        )
                                    }
                                }
                            },
                            onDragCancel = {}
                        )
                    }
                    .clickable { expanded = !expanded },
                colors = CardDefaults.cardColors(containerColor = trigger.color),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(trigger.icon, contentDescription = trigger.name)
                        Text(trigger.name, style = MaterialTheme.typography.bodyLarge)
                    }

                    AnimatedVisibility(visible = expanded) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .animateContentSize()
                        ) {
                            trigger.strategies.forEach { strategy ->
                                AssistChip(
                                    onClick = { /* Optional unlink */ },
                                    label = { Text(strategy) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

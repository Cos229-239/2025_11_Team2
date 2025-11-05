package com.reclaim.reclaim.ui.trigger

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun StrategyBar(onDragStart: (String) -> Unit) {
    val strategies = listOf("Meditation", "Journaling", "Talk to sponsor", "Cold shower")

    Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
        strategies.forEach { strategy ->
            AssistChip(
                onClick = { /* Optional tap */ },
                label = { Text(strategy) },
                modifier = Modifier
                    .padding(4.dp)
                    .pointerInput(strategy) {
                        detectDragGestures(
                            onDragStart = { offset -> onDragStart(strategy) },
                            onDrag = { change, dragAmount -> },
                            onDragEnd = {}
                        )
                    }

            )
        }
    }
}

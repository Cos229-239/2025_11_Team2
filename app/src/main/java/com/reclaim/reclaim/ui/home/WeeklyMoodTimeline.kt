package com.reclaim.reclaim.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reclaim.reclaim.data.entities.MoodEntry
import java.time.LocalDate

@Composable
fun WeeklyMoodTimeline(moods: List<MoodEntry>) {
    val days = (0..6).map { LocalDate.now().minusDays((6 - it).toLong()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Timeline
        LazyRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(days) { day ->
                val entry = moods.find { it.date == day }
                val isToday = day == LocalDate.now()

                val moodColor = when (entry?.mood) {
                    "😊", "❤️" -> Color(0xFF81C784) // green
                    "😐", "😴" -> Color(0xFFB0BEC5) // gray
                    "😢", "😠", "😨", "🤯" -> Color(0xFFE57373) // red
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isToday) moodColor.copy(alpha = 0.9f) else moodColor
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(entry?.mood ?: "❓", style = MaterialTheme.typography.headlineMedium)
                        Text(day.dayOfWeek.name.take(3), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(color = Color(0xFF81C784), label = "Positive")
            LegendItem(color = Color(0xFFB0BEC5), label = "Neutral")
            LegendItem(color = Color(0xFFE57373), label = "Negative")
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = MaterialTheme.shapes.small)
        )
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

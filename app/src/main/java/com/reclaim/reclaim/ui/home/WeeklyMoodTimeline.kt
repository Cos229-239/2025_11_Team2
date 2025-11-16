package com.reclaim.reclaim.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reclaim.reclaim.data.entities.MoodEntry
import java.time.LocalDate

@Composable
fun WeeklyMoodTimeline(moods: List<MoodEntry>) {
    val days = (0..6).map { LocalDate.now().minusDays((6 - it).toLong()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            val entry = moods.find { it.date == day }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = entry?.mood ?: "❓", // fallback if no entry
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = day.dayOfWeek.name.take(3), // Mon, Tue, etc.
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

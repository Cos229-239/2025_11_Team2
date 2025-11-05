package com.reclaim.reclaim.ui.components

import com.reclaim.reclaim.model.SoberTime

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment



@Composable
fun SoberTimeTracker(soberTime: SoberTime) {
    val animatedYears by animateIntAsState(
        targetValue = soberTime.years,
        animationSpec = tween(durationMillis = 800)
    )
    val animatedMonths by animateIntAsState(
        targetValue = soberTime.months,
        animationSpec = tween(durationMillis = 800)
    )
    val animatedDays by animateIntAsState(
        targetValue = soberTime.days,
        animationSpec = tween(durationMillis = 800)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Sober Time", style = MaterialTheme.typography.titleMedium)

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$animatedYears Years")
                Text("$animatedMonths Months")
                Text("$animatedDays Days")
            }

            // 🎉 Celebration Text for Total Days Sober
            Text(
                text = "🎉 ${soberTime.totalDays} total days sober",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }
}


package com.reclaim.reclaim.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reclaim.reclaim.ui.home.SoberTime
import androidx.compose.material3.CardDefaults

@Composable
fun SoberTimeTracker(soberTime: SoberTime) {
    Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Sober Time", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Text("${soberTime.years} Years")
                Text("${soberTime.months} Months")
                Text("${soberTime.days} Days")
            }
        }
    }
}

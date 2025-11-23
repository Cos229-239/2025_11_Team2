package com.reclaim.reclaim.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.components.BottomNavBar
import com.reclaim.reclaim.ui.components.NavigationGrid
import com.reclaim.reclaim.ui.components.SoberTimeTracker
import com.reclaim.reclaim.ui.viewmodels.HomeViewModel
import java.time.LocalTime
import com.reclaim.reclaim.ui.components.MilestoneCelebrationCard




@Composable
fun HomeScreen(
    name: String,
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val soberTime by viewModel.soberTime.collectAsState()
    val affirmation by viewModel.affirmation.collectAsState()
    val mood by viewModel.mood.collectAsState()
    val weeklyMoods by viewModel.weeklyMoodHistory.collectAsState(initial = emptyList())
    val milestoneReached by viewModel.milestoneReached.collectAsState()
    var dismissed by remember { mutableStateOf(false) }
    val greeting = remember {
        val hour = LocalTime.now().hour
        when {
            hour < 12 -> "Good morning"
            hour < 18 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item { Text("$greeting, $name 👋", style = MaterialTheme.typography.headlineMedium) }
            item {
                Text("Your affirmation", style = MaterialTheme.typography.headlineSmall)
                Text(affirmation, style = MaterialTheme.typography.bodyLarge)
            }
            if (milestoneReached != null && !dismissed) {
                item {
                    MilestoneCelebrationCard(
                        milestoneDays = milestoneReached!!,
                        onDismiss = { dismissed = true }
                    )
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Mood Check-In",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val moods = listOf("😊", "😐", "😢", "😠", "😨", "😴", "❤️", "🤯")
                            moods.forEach { emoji ->
                                Text(
                                    text = emoji,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.clickable { viewModel.setMood(emoji) }
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Today's mood: ${mood ?: "Not logged"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            item { WeeklyMoodTimeline(weeklyMoods) }
            item { SoberTimeTracker(soberTime) }
            item { NavigationGrid(navController) }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Today’s Reflection", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "What emotion surprised you today?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Debug button(Delete when not needed)
            // Also delete debug code in HomeViewModel
            item {
                Button(
                    onClick = { viewModel.triggerMilestone(30)},
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text("Trigger 30-day milestone")
                }
            }
        }
    }
}
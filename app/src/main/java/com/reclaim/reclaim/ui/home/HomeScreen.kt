package com.reclaim.reclaim.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.components.BottomNavBar
import com.reclaim.reclaim.ui.components.NavigationGrid
import com.reclaim.reclaim.ui.components.SoberTimeTracker
import com.reclaim.reclaim.ui.viewmodels.HomeViewModel
import com.reclaim.reclaim.model.SoberTime
import java.time.LocalTime
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(
    name: String,
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val soberTime by viewModel.soberTime.collectAsState()
    val affirmation by viewModel.affirmation.collectAsState()
    val mood by viewModel.mood.collectAsState()

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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "$greeting, $name 👋",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Your affirmation",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = affirmation,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // ✅ Mood Check-In Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Mood Check-In",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

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

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Today's mood: ${mood ?: "Not logged"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                SoberTimeTracker(soberTime)

                NavigationGrid(navController)

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
        }
    }
}
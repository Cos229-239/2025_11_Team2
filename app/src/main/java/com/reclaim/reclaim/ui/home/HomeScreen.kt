package com.reclaim.reclaim.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.components.BottomNavBar
import com.reclaim.reclaim.ui.components.NavigationGrid
import com.reclaim.reclaim.ui.components.SoberTimeTracker
import java.time.LocalTime


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val soberTime by viewModel.soberTime.collectAsState()
    val affirmation by viewModel.affirmation.collectAsState()

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
                    text = "$greeting 👋",
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

                SoberTimeTracker(soberTime)

                NavigationGrid(navController)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Today’s Reflection", style = MaterialTheme.typography.titleMedium)
                        Text("What emotion surprised you today?", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

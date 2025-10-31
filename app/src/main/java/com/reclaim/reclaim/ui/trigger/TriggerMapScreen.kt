package com.reclaim.reclaim.ui.trigger

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TriggerMapScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Trigger Map") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Explore your triggers and link strategies", style = MaterialTheme.typography.titleMedium)

            // Placeholder for trigger grid
            TriggerGrid()
        }
    }
}

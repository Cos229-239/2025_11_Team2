package com.reclaim.reclaim.ui.trigger

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.trigger.StrategyBar
import com.reclaim.reclaim.ui.components.BottomNavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggerMapScreen(navController: NavController) {
    var draggedStrategy by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        topBar = {
            TopAppBar(title = { Text("Trigger Map") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Explore your triggers and link strategies", style = MaterialTheme.typography.titleMedium)

            StrategyBar { strategy -> draggedStrategy = strategy }

            TriggerGrid(draggedStrategy)
        }
    }
}
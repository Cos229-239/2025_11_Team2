package com.reclaim.reclaim.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.components.BottomNavBar
import com.reclaim.reclaim.ui.components.NavigationGrid
import com.reclaim.reclaim.ui.components.SoberTimeTracker
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val soberTime by viewModel.soberTime.collectAsState()
    val affirmation by viewModel.affirmation.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Your affirmation",
                style = MaterialTheme.typography.headlineSmall // instead of h6 or subtitle1
            )

            SoberTimeTracker(soberTime)
            NavigationGrid(navController)
        }
    }
}

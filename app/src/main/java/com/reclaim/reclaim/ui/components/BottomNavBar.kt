package com.reclaim.reclaim.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.reclaim.reclaim.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val screens = listOf(
        Screen.Home,
        Screen.Map,
        Screen.Strategies,
        Screen.Journal,
        Screen.Profile
    )

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = false, // Replace with actual selection logic
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

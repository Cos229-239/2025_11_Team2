package com.reclaim.reclaim.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.reclaim.reclaim.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(Screen.Home, Screen.Map, Screen.Strategies, Screen.Journal, Screen.Profile)
    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = false,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}

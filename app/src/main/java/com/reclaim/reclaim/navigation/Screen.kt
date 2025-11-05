package com.reclaim.reclaim.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Map : Screen("map", "Map", Icons.Default.Map)
    object Strategies : Screen("strategies", "Strategies", Icons.Default.Lightbulb)
    object Journal : Screen("journal", "Journal", Icons.Default.Edit)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}
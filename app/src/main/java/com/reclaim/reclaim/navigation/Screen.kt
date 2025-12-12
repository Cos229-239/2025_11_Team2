package com.reclaim.reclaim.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.List

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {

    object Login: Screen(route = "login", label = "Login", icon = Icons.Default.Login)
    object SignUp: Screen(route = "signup", label = "Sign Up", icon = Icons.Default.PersonAdd)
    object Splash : Screen(route = "splash", label = "Splash", icon = Icons.Default.Home
    )
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Map : Screen("map", "Map", Icons.Default.Map)
    object Strategies : Screen("strategies", "Strategies", Icons.Default.SelfImprovement)
    object Journal : Screen("journal", "Journal", Icons.Default.Edit)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)

    object SavedJournals : Screen(
        route = "saved_journals",
        label = "Saved",
        icon = Icons.Default.List
    )
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

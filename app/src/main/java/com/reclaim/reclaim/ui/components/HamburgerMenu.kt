package com.reclaim.reclaim.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

/**
 * HamburgerMenu
 * -------------
 * Drawer content for navigation.
 * - Displays navigation options.
 * - Closes drawer when an option is tapped.
 *
 * TODO:
 * - Add navigation items for all your screens (Home, Journal, Strategies, Profile, etc.)
 * - Highlight the current route.
 */
@Composable
fun HamburgerMenu(
    navController: NavHostController,
    currentRoute: String,
    onClose: () -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "Navigation",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        NavigationDrawerItem(
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home")
                onClose()
            }
        )

        NavigationDrawerItem(
            label = { Text("Coping Strategies") },
            selected = currentRoute == "coping",
            onClick = {
                navController.navigate("coping")
                onClose()
            }
        )

        NavigationDrawerItem(
            label = { Text("Journal") },
            selected = currentRoute == "journal",
            onClick = {
                navController.navigate("journal")
                onClose()
            }
        )

        NavigationDrawerItem(
            label = { Text("Profile") },
            selected = currentRoute == "Profile",
            onClick = {
                navController.navigate("Profile")
                onClose()
            }
        )
    }
}
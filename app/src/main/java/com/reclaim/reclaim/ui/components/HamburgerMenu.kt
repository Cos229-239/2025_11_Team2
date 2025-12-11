package com.reclaim.reclaim.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HamburgerMenu(
    navController: NavHostController,
    currentRoute: String,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        // App title / header
        Text(
            text = "Reclaim",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        DrawerItem(
            label = "Home",
            route = "home",
            currentRoute = currentRoute,
            navController = navController,
            onClose = onClose
        )

        DrawerItem(
            label = "Journal",
            route = "journal",
            currentRoute = currentRoute,
            navController = navController,
            onClose = onClose
        )

        DrawerItem(
            label = "Coping Strategies",
            route = "coping",
            currentRoute = currentRoute,
            navController = navController,
            onClose = onClose
        )

        DrawerItem(
            label = "Trigger Map",
            route = "trigger_map",
            currentRoute = currentRoute,
            navController = navController,
            onClose = onClose
        )
    }
}

@Composable
private fun DrawerItem(
    label: String,
    route: String,
    currentRoute: String,
    navController: NavHostController,
    onClose: () -> Unit
) {
    val selected = currentRoute == route

    NavigationDrawerItem(
        label = { Text(text = label) },
        selected = selected,
        onClick = {
            if (!selected) {
                navController.navigate(route) {
                    launchSingleTop = true
                }
            }
            onClose()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        )
    )
}

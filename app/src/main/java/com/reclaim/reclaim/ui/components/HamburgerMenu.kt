package com.reclaim.reclaim.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HamburgerMenu(
    navController: NavHostController,
    currentRoute: String,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        Text(
            text = "Reclaim",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))

        Spacer(Modifier.height(8.dp))


        val itemColors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.background,
            unselectedContainerColor = Color.Transparent,
            selectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
        )

        NavigationDrawerItem(
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home")
                onClose()
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            colors = itemColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Journal") },
            selected = currentRoute == "journal",
            onClick = {
                navController.navigate("journal")
                onClose()
            },
            icon = { Icon(Icons.Filled.Edit, contentDescription = "Journal") },
            colors = itemColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Trigger Map") },
            selected = currentRoute == "map",
            onClick = {
                navController.navigate("map")
                onClose()
            },
            icon = { Icon(Icons.Filled.Map, contentDescription = "Trigger Map") },
            colors = itemColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Coping Strategies") },
            selected = currentRoute == "strategies",
            onClick = {
                navController.navigate("strategies")
                onClose()
            },
            icon = { Icon(Icons.Filled.SelfImprovement, contentDescription = "Coping Strategies") },
            colors = itemColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Profile") },
            selected = currentRoute == "Profile",
            onClick = {
                navController.navigate("Profile")
                onClose()
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            colors = itemColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Spacer(Modifier.height(12.dp))
    }
}

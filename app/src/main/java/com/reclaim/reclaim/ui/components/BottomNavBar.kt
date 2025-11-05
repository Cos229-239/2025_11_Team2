package com.reclaim.reclaim.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.reclaim.reclaim.navigation.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    BottomNavigation {
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
            )
        }
    }
}

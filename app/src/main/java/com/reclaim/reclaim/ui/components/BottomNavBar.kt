package com.reclaim.reclaim.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.reclaim.reclaim.navigation.Screen
import androidx.compose.ui.unit.sp

private val PillPurple = Color(0xFF7C3AED)

@Composable
fun BottomNavBar(navController: NavController) {

    val screens = listOf(
        Screen.Home,
        Screen.Map,
        Screen.Strategies,
        Screen.Journal,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .padding(bottom = 18.dp, start = 24.dp, end = 24.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(26.dp),
                clip = false
            )
            .clip(RoundedCornerShape(26.dp))
            .background(PillPurple)
            .padding(horizontal = 20.dp, vertical = 6.dp) // << THIN HEIGHT
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                val isSelected = currentRoute == screen.route

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label,
                        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = screen.label,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 2.dp),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}


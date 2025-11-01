package com.reclaim.reclaim.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.reclaim.reclaim.ui.home.HomeScreen
import com.reclaim.reclaim.ui.trigger.TriggerMapScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Map.route) {
            TriggerMapScreen(navController)

        }
    }
}

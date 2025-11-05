package com.reclaim.reclaim.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.reclaim.reclaim.ui.home.HomeScreen
import com.reclaim.reclaim.ui.trigger.TriggerMapScreen
import com.reclaim.reclaim.ui.coping.CopingStrategiesScreen
import com.reclaim.reclaim.ui.journal.JournalScreen


@Composable
fun NavGraph(navController: NavHostController, name: String) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)

    }
        composable(Screen.Map.route) {
            TriggerMapScreen(navController = navController)
        }
        composable(Screen.Strategies.route) {
            CopingStrategiesScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Journal.route) {
            JournalScreen(name = name, navController = navController)
        }
    }
}

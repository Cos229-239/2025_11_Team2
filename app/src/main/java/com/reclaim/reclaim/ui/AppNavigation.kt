package com.reclaim.reclaim.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reclaim.reclaim.ui.coping.CopingStrategiesScreen
import com.reclaim.reclaim.ui.home.HomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("coping_strategies") {
            CopingStrategiesScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
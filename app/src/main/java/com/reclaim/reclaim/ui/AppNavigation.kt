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
    name: String, navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(name = name, navController = navController)
        }
        composable("coping") {
            CopingStrategiesScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
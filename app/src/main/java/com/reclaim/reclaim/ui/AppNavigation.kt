package com.reclaim.reclaim.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.reclaim.reclaim.ui.coping.CopingStrategiesScreen
import com.reclaim.reclaim.ui.home.HomeScreen
import com.reclaim.reclaim.ui.journal.JournalScreen
import com.reclaim.reclaim.ui.viewmodels.SavedJournals
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavHost(
    name: String, navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("coping") {
            CopingStrategiesScreen(
                onBackClick = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(
            route = "journal?initialText={initialText}",
            arguments = listOf(navArgument("initialText") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val initialText = backStackEntry.arguments?.getString("initialText") ?: ""
            val viewModel: SavedJournals = viewModel()
            JournalScreen(
                name = name,
                navController = navController,
                viewModel = viewModel,
                initialText = initialText
            )
        }
    }
}
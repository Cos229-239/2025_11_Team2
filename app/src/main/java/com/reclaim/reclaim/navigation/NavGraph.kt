package com.reclaim.reclaim.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.reclaim.reclaim.ui.home.HomeScreen
import com.reclaim.reclaim.ui.trigger.TriggerMapScreen
import com.reclaim.reclaim.ui.coping.CopingStrategiesScreen
import com.reclaim.reclaim.ui.journal.JournalScreen
import com.reclaim.reclaim.ui.journal.SavedJournalsScreen
import com.reclaim.reclaim.ui.profile.ProfileScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaim.reclaim.ui.viewmodels.SavedJournals


@Composable
fun NavGraph(navController: NavHostController, name: String) {

    val savedJournalsViewModel: SavedJournals = viewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(name = name, navController = navController)

        }
        composable(Screen.Map.route) {
            TriggerMapScreen(navController = navController)
        }
        composable("Strategies") {
            CopingStrategiesScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Journal.route) {
            JournalScreen(
                name = name,
                navController = navController,
                viewModel = savedJournalsViewModel
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(name = name, navController = navController)
        }
        composable(Screen.SavedJournals.route) {
            SavedJournalsScreen(
                navController = navController,
                viewModel = savedJournalsViewModel
            )
        }
    }

}
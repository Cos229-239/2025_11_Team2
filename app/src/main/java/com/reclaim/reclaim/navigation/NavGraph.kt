package com.reclaim.reclaim.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.reclaim.reclaim.ui.components.LoginScreen
import com.reclaim.reclaim.ui.components.SplashScreen
import com.reclaim.reclaim.ui.coping.CopingStrategiesScreen
import com.reclaim.reclaim.ui.home.HomeScreen
import com.reclaim.reclaim.ui.journal.JournalScreen
import com.reclaim.reclaim.ui.journal.SavedJournalsScreen
import com.reclaim.reclaim.ui.profile.ProfileScreen
import com.reclaim.reclaim.ui.settings.SettingsScreen
import com.reclaim.reclaim.ui.trigger.TriggerMapScreen
import com.reclaim.reclaim.ui.viewmodels.HomeViewModel
import com.reclaim.reclaim.ui.viewmodels.SavedJournals
import com.reclaim.reclaim.ui.components.SignUpScreen // <-- Import the new screen


@Composable
fun NavGraph(
    navController: NavHostController,
    name: String,
    viewModel: HomeViewModel = viewModel()
) {
    val savedJournalsViewModel: SavedJournals = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // ---------- Splash ----------
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                name = name,
                navController = navController
            )
        }

        composable(Screen.Map.route) {
            TriggerMapScreen(
                navController = navController
            )
        }

        composable(Screen.Strategies.route) {
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
            ProfileScreen(
                name = name,
                navController = navController
            )
        }

        composable(Screen.SavedJournals.route) {
            SavedJournalsScreen(
                navController = navController,
                viewModel = savedJournalsViewModel
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
    }
}
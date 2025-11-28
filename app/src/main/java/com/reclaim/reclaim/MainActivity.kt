package com.reclaim.reclaim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.reclaim.reclaim.navigation.NavGraph
import com.reclaim.reclaim.ui.theme.ReclaimTheme
import dagger.hilt.android.AndroidEntryPoint   // 👈 import this

/**
 * MainActivity
 * ------------
 * Root activity hosting the Compose UI.
 * - Annotated with @AndroidEntryPoint so Hilt can inject ViewModels.
 * - Sets up NavController and passes it into NavGraph.
 */


@AndroidEntryPoint   // 👈 required for Hilt injection
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val userName = "Brandy" // Replace with dynamic value later if needed

            ReclaimTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph(navController = navController, name = userName)
                }
            }
        }
    }
}

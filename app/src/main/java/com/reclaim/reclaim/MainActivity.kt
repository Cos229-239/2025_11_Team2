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
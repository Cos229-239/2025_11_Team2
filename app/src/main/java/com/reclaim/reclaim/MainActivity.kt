package com.reclaim.reclaim

import android.R.attr.name
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.reclaim.reclaim.navigation.NavGraph
import com.reclaim.reclaim.ui.theme.ReclaimTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            ReclaimTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavGraph(navController = navController,
                        name= "Brandy"
                    )
                }
            }
        }
    }
}
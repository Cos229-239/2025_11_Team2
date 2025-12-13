package com.reclaim.reclaim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.reclaim.reclaim.navigation.NavGraph
import com.reclaim.reclaim.ui.theme.ReclaimTheme
import com.reclaim.reclaim.ui.widget.WidgetRefreshWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val request = PeriodicWorkRequestBuilder<WidgetRefreshWorker>(1, TimeUnit.SECONDS).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("widget_refresh", ExistingPeriodicWorkPolicy.UPDATE, request)


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
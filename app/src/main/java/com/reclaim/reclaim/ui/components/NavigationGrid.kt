package com.reclaim.reclaim.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.navigation.Screen

@Composable
fun NavigationGrid(navController: NavController) {
    val items = listOf(Screen.Map, Screen.Strategies, Screen.Journal, Screen.Profile)
    LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp)) {
        items(items.size) { index ->
            val item = items[index]
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate(item.route) },
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(item.icon, contentDescription = item.label)
                    Text(item.label)
                }
            }
        }
    }
}

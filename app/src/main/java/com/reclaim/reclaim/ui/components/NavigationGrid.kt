package com.reclaim.reclaim.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.navigation.Screen
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun NavigationGrid(navController: NavController) {
    val items = listOf(Screen.Map, Screen.Strategies, Screen.Journal, Screen.Profile)

    FlowRow(
        mainAxisSpacing = 16.dp,
        crossAxisSpacing = 16.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            Card(
                modifier = Modifier
                    .width(150.dp) // fixed width so two fit per row
                    .clickable { navController.navigate(item.route) },
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon, // or painter depending on your Screen definition
                        contentDescription = item.label
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(item.label)
                }
            }
        }
    }
}

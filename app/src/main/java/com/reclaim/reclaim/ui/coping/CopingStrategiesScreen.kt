@file:OptIn(ExperimentalMaterial3Api::class)

package com.reclaim.reclaim.ui.coping

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaim.reclaim.data.entities.StrategyEntity
import com.reclaim.reclaim.ui.viewmodels.CopingStrategiesViewModel

@Composable
fun CopingStrategiesScreen(
    onBackClick: () -> Unit,
    viewModel: CopingStrategiesViewModel = viewModel()
) {
    val strategies by viewModel.strategies.collectAsState(initial = emptyList())
    val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsState(initial = false)
    var searchQuery by remember { mutableStateOf("") } // New for search
    val filteredStrategies = strategies.filter {
        (if (showFavoritesOnly) it.isFavorite else true) &&
                (it.triggerName.contains(searchQuery, ignoreCase = true) ||
                        it.strategy.contains(searchQuery, ignoreCase = true))
    }
    val backgroundColor = Color(0xFFA5D6A7)
    val cardColor = Color(0xFFE8F5E9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coping Strategies") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor // Set overall background color
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // New Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search strategies...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            // New Favorites Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Show Favorites Only")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = showFavoritesOnly,
                    onCheckedChange = { viewModel.toggleShowFavoritesOnly() }
                )
            }

            if (filteredStrategies.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredStrategies) { strategyItem ->
                        var expanded by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp), // Rounded corners
                            colors = CardDefaults.cardColors(
                                containerColor = cardColor // Lighter green for cards
                            ),
                            onClick = { expanded = !expanded }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp)) // Space after icon
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = strategyItem.triggerName,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    if (expanded) {
                                        Text(
                                            text = strategyItem.strategy,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                // New Favorite Toggle
                                IconButton(onClick = { viewModel.toggleFavorite(strategyItem) }) {
                                    Icon(
                                        imageVector = if (strategyItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Toggle Favorite",
                                        tint = if (strategyItem.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
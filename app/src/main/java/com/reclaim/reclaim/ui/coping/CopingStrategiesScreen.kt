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
import com.reclaim.reclaim.ui.components.BottomNavBar
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.theme.WhiteTextFieldColors


/**
 * StrategiesScreen
 * ----------------
 * Displays coping strategies and allows linking them to triggers.
 * - Intended for recovery support and journaling context.
 * - Will integrate with StrategyEntity + StrategyDao via Hilt.
 *
 * TODO:
 * - Show list of strategies from database.
 * - Add ability to create, edit, and delete strategies.
 * - Link strategies to triggers (many-to-many relationship).
 * - Add color-coded categories for clarity.
 * - Provide quick actions (favorite, mark as effective).
 */

@Composable
fun CopingStrategiesScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    viewModel: CopingStrategiesViewModel = viewModel()
) {
    val strategies by viewModel.strategies.collectAsState(initial = emptyList())
    val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsState(initial = false)
    var searchQuery by remember { mutableStateOf("") }

    val filteredStrategies = strategies.filter {
        (if (showFavoritesOnly) it.isFavorite else true) &&
                (it.triggerName.contains(searchQuery, ignoreCase = true) ||
                        it.strategy.contains(searchQuery, ignoreCase = true))
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    val cardColor = MaterialTheme.colorScheme.surface

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
        bottomBar = { BottomNavBar(navController = navController) },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search strategies...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                colors = WhiteTextFieldColors()

            )

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

            if (strategies.isEmpty()) {
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
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = cardColor
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
                                Spacer(modifier = Modifier.width(12.dp))
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

@file:OptIn(ExperimentalMaterial3Api::class)

package com.reclaim.reclaim.ui.coping

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaim.reclaim.ui.viewmodels.CopingStrategiesViewModel
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.components.BottomNavBar


private val SoftBeige = Color(0xFFF8EFE7)
private val Violet = Color(0xFF7C3AED)
private val Turquoise = Color(0xFF00B4A0)

@Composable
fun CopingStrategiesScreen(
    onBackClick: () -> Unit,
    navController: NavController,
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

    val backgroundColor = Turquoise

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Coping Strategies",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor,
        bottomBar = {
            BottomNavBar(navController = navController)
        }
            ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {

            // 🔍 Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search strategies...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = SoftBeige,
                    unfocusedContainerColor = SoftBeige,
                    focusedLabelColor = Violet,
                    unfocusedLabelColor = Violet.copy(alpha = 0.7f),
                    cursorColor = Violet,
                    focusedTextColor = Violet,
                    unfocusedTextColor = Violet
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show Favorites Only",
                    color = Violet,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = showFavoritesOnly,
                    onCheckedChange = { viewModel.toggleShowFavoritesOnly() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Violet,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = SoftBeige.copy(alpha = 0.6f)
                    )
                )
            }

            if (strategies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Violet)
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
                                .shadow(
                                    elevation = 6.dp,
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SoftBeige
                            ),
                            onClick = { expanded = !expanded }
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = Turquoise,
                                    modifier = Modifier.size(24.dp)
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = strategyItem.triggerName,
                                        color = Violet,
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    if (expanded) {
                                        Text(
                                            text = strategyItem.strategy,
                                            color = Violet.copy(alpha = 0.9f),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                IconButton(onClick = {
                                    viewModel.toggleFavorite(strategyItem)
                                }) {
                                    Icon(
                                        imageVector = if (strategyItem.isFavorite)
                                            Icons.Default.Favorite
                                        else
                                            Icons.Default.FavoriteBorder,
                                        contentDescription = "Toggle Favorite",
                                        tint = if (strategyItem.isFavorite) Color.Red else Violet
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

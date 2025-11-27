@file:OptIn(ExperimentalMaterial3Api::class)

package com.reclaim.reclaim.ui.coping

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.icons.filled.VolumeUp // Added icon for voice read aloud button
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaim.reclaim.data.entities.StrategyEntity
import com.reclaim.reclaim.ui.viewmodels.CopingStrategiesViewModel
import android.speech.tts.TextToSpeech // Android tool to read text out loud
import androidx.compose.ui.platform.LocalContext // Gets the app context for voice features
import java.util.Locale // Sets language for voice readout

@Composable
fun CopingStrategiesScreen(
    onBackClick: () -> Unit,
    viewModel: CopingStrategiesViewModel = viewModel()
) {
    val strategies by viewModel.strategies.collectAsState(initial = emptyList())
    val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsState(initial = false)
    var searchQuery by remember { mutableStateOf("") }
    val filteredStrategies by remember { derivedStateOf {
        strategies.filter {
            (if (showFavoritesOnly) it.isFavorite else true) &&
                    (it.triggerName.contains(searchQuery, ignoreCase = true) ||
                            it.strategy.contains(searchQuery, ignoreCase = true))
        }
    } }
    val backgroundColor = Color(0xFF4CAF50) // More vibrant green background
    val cardColor = Color(0xFFC8E6C9) // Lighter more vibrant green for cards
    var randomStrategy by remember { mutableStateOf<StrategyEntity?>(null) } // Updated: Quick tip refresh on each button click
    val context = LocalContext.current // Get the app's context to use for voice readout
    val tts = remember { TextToSpeech(context, null) } //  Sets up the voice reader tool
    tts.language = Locale.US // Sets the voice to English (change if needed for other languages)
    var showQuickTipDialog by remember { mutableStateOf(false) } // Tracks if the quick tip dialog is open

    // Dialog to show the random tip when the button is clicked, keeps it simple and pops up instantly
    if (showQuickTipDialog && randomStrategy != null) {
        AlertDialog(
            onDismissRequest = { showQuickTipDialog = false },
            title = { Text("Quick Tip") },
            text = {
                Column {
                    Text(randomStrategy!!.triggerName, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(randomStrategy!!.strategy, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = { showQuickTipDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

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
        containerColor = Color.Transparent // For gradient
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(backgroundColor, Color(0xFF81C784)) // Vibrant green fade
                    )
                )
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search strategies...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp)), // Rounded for better look
                    singleLine = true
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

                // Button for quick random tip - users tap for an instant strategy when they need fast help
                Button(
                    onClick = {
                        randomStrategy = viewModel.getRandomStrategy(filteredStrategies) // Updated: Recomputes a new random tip each click
                        showQuickTipDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Text("Get Quick Tip")
                }

                if (filteredStrategies.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White) // White for contrast on green
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredStrategies) { strategyItem ->
                            val expanded = remember { mutableStateOf(false) }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(6.dp, RoundedCornerShape(20.dp)), // Increased shadow to stand out more
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = cardColor),
                                onClick = { expanded.value = !expanded.value }
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
                                        tint = backgroundColor, // Match vibrant green
                                        modifier = Modifier.size(28.dp) // Slightly larger might look better
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = strategyItem.triggerName,
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), // Bolder for titles
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        AnimatedVisibility( // New Fade animation on expand
                                            visible = expanded.value,
                                            enter = fadeIn() + scaleIn(),
                                            exit = fadeOut() + scaleOut()
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = strategyItem.strategy,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                IconButton(onClick = { tts.speak(strategyItem.strategy, TextToSpeech.QUEUE_FLUSH, null, null) }) { // Button to read the strategy out loud for hands-free help
                                                    Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud")
                                                }
                                            }
                                        }
                                    }
                                    IconButton(onClick = { viewModel.toggleFavorite(strategyItem) }) {
                                        Icon(
                                            imageVector = if (strategyItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Toggle Favorite",
                                            tint = if (strategyItem.isFavorite) Color(0xFFE57373) else MaterialTheme.colorScheme.onSurface // Softer red for favorites
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
}
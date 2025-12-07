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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.VolumeUp
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
import android.speech.tts.TextToSpeech
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import kotlinx.coroutines.flow.collectLatest



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
    var randomStrategy by remember { mutableStateOf<StrategyEntity?>(null) }  // Now mutable, starts as null; updates on button click for fresh random each time
    val context = LocalContext.current // Get app's context to use for voice readout
    val tts = remember { TextToSpeech(context, null) } //  Sets up the voice reader tool
    tts.language = Locale.US // Sets the voice to English (change if needed for other languages)
    var showQuickTipDialog by remember { mutableStateOf(false) } // Tracks if the quick tip dialog is open
    var showAddDialog by remember { mutableStateOf(false) } // New: Tracks if the add custom strategy dialog is open
    var newTriggerName by remember { mutableStateOf("") } // New: Holds user input for new trigger name
    var newStrategy by remember { mutableStateOf("") } // New: Holds user input for new strategy description
    var stats by remember { mutableStateOf(Pair(0, 0)) }  // New: for total and favorites
    LaunchedEffect(Unit) {
        viewModel.stats.collectLatest { stats = it }
    }

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

    // NEW: Dialog for adding custom strategies. Users can enter their own trigger and tip to save to the list
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Custom Strategy") },
            text = {
                Column {
                    TextField(
                        value = newTriggerName,
                        onValueChange = { newTriggerName = it },
                        label = { Text("Trigger Name (e.g., Stress)") }
                    )
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = newStrategy,
                        onValueChange = { newStrategy = it },
                        label = { Text("Strategy Description") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTriggerName.isNotBlank() && newStrategy.isNotBlank()) {
                            viewModel.addCustomStrategy(newTriggerName, newStrategy)  // Pass strings directly and ViewModel creates the entity
                            showAddDialog = false
                            newTriggerName = ""
                            newStrategy = ""
                        }
                    }
                ) { Text("Add") }
            },
            dismissButton = { Button(onClick = { showAddDialog = false }) { Text("Cancel") } }
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
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Custom Strategy")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(backgroundColor, Color(0xFF81C784))
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
                        .clip(RoundedCornerShape(12.dp)),
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

                Button(
                    onClick = {
                        randomStrategy = viewModel.getRandomStrategy(filteredStrategies)
                        showQuickTipDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Text("Get Quick Tip")
                }

                Text(
                    text = "${stats.first} Strategies | ${stats.second} Favorites",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )

                if (filteredStrategies.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
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
                                    .shadow(6.dp, RoundedCornerShape(20.dp)),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = cardColor),
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
                                        tint = backgroundColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = strategyItem.triggerName,
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        AnimatedVisibility(
                                            visible = expanded,
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
                                                IconButton(onClick = { tts.speak(strategyItem.strategy, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                                                    Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud")
                                                }
                                            }
                                        }
                                    }
                                    IconButton(onClick = { viewModel.toggleFavorite(strategyItem) }) {
                                        Icon(
                                            imageVector = if (strategyItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Toggle Favorite",
                                            tint = if (strategyItem.isFavorite) Color(0xFFE57373) else MaterialTheme.colorScheme.onSurface
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
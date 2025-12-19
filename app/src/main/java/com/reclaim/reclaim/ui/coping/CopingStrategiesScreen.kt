@file:OptIn(ExperimentalMaterial3Api::class)

package com.reclaim.reclaim.ui.coping
import androidx.compose.ui.graphics.Color
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Edit  // NEW: for edit custom strategies
import androidx.compose.material.icons.filled.Delete  // NEW: for deleting custom strategies
import androidx.compose.material.icons.filled.NoteAdd  // NEW: Icon for Log This
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaim.reclaim.data.entities.StrategyEntity
import com.reclaim.reclaim.ui.viewmodels.CopingStrategiesViewModel
import android.speech.tts.TextToSpeech
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import kotlinx.coroutines.flow.collectLatest
import androidx.navigation.NavHostController  // NEW: For navigation
import java.net.URLEncoder  // NEW: For encoding prefill text
@Composable
fun CopingStrategiesScreen(
    onBackClick: () -> Unit,
    navController: NavHostController,  // NEW: Param for navigating to journal
    viewModel: CopingStrategiesViewModel = viewModel()
) {
    val strategies by viewModel.strategies.collectAsState(initial = emptyList())
    val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsState(initial = false)
    var searchQuery by remember { mutableStateOf("") }
    val filteredStrategies by remember {
        derivedStateOf {
            strategies.filter {
                (if (showFavoritesOnly) it.isFavorite else true) &&
                        (it.triggerName.contains(searchQuery, ignoreCase = true) ||
                                it.strategy.contains(searchQuery, ignoreCase = true))
            }
        }
    }
    var randomStrategy by remember { mutableStateOf<StrategyEntity?>(null) }
    val context = LocalContext.current
    val tts = remember { TextToSpeech(context, null) }
    tts.language = Locale.US
    var showQuickTipDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newTriggerName by remember { mutableStateOf("") }
    var newStrategy by remember { mutableStateOf("") }
    var strategyToEdit by remember { mutableStateOf<StrategyEntity?>(null) }  // NEW
    var strategyToDelete by remember { mutableStateOf<StrategyEntity?>(null) }  // NEW
    var stats by remember { mutableStateOf<Pair<Int, Int>>(Pair(0, 0)) }
    LaunchedEffect(Unit) {
        viewModel.stats.collectLatest { stats = it }
    }

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
                            viewModel.addCustomStrategy(newTriggerName, newStrategy)
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

    if (strategyToEdit != null) {
        AlertDialog(
            onDismissRequest = { strategyToEdit = null },
            title = { Text("Edit Strategy") },
            text = {
                Column {
                    TextField(
                        value = newTriggerName,
                        onValueChange = { newTriggerName = it },
                        label = { Text("Trigger Name") }
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
                            val updated = strategyToEdit!!.copy(
                                triggerName = newTriggerName,
                                strategy = newStrategy
                            )
                            viewModel.updateStrategy(updated)
                            strategyToEdit = null
                            newTriggerName = ""
                            newStrategy = ""
                        }
                    }
                ) { Text("Save") }
            },
            dismissButton = { Button(onClick = { strategyToEdit = null }) { Text("Cancel") } }
        )
    }
    LaunchedEffect(strategyToEdit) {
        if (strategyToEdit != null) {
            newTriggerName = strategyToEdit!!.triggerName
            newStrategy = strategyToEdit!!.strategy
        }
    }

    if (strategyToDelete != null) {
        AlertDialog(
            onDismissRequest = { strategyToDelete = null },
            title = { Text("Delete Strategy?") },
            text = { Text("Remove '${strategyToDelete!!.triggerName}'? This can't be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteStrategy(strategyToDelete!!)
                        strategyToDelete = null
                    }
                ) { Text("Delete") }
            },
            dismissButton = { Button(onClick = { strategyToDelete = null }) { Text("Cancel") } }
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
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Custom Strategy")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
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
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                onClick = { expanded = !expanded }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Lightbulb,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = strategyItem.triggerName,
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        AnimatedVisibility(
                                            visible = expanded,
                                            enter = fadeIn() + scaleIn(),
                                            exit = fadeOut() + scaleOut()
                                        ) {
                                            Column {  // NEW: Wrap in Column to avoid horizontal overflow shift
                                                Text(
                                                    text = strategyItem.strategy,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.End  // NEW: Align icons to right
                                                ) {
                                                    IconButton(onClick = { tts.speak(strategyItem.strategy, TextToSpeech.QUEUE_FLUSH, null, null) }) {
                                                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Read Aloud")
                                                    }
                                                    IconButton(onClick = {
                                                        val prefill = "Used ${strategyItem.strategy} for ${strategyItem.triggerName} today—how did it go?"
                                                        navController.navigate("journal?initialText=${java.net.URLEncoder.encode(prefill, "UTF-8")}")
                                                    }) {
                                                        Icon(Icons.Filled.NoteAdd, contentDescription = "Log This", tint = MaterialTheme.colorScheme.secondary)
                                                    }
                                                    if (strategyItem.isCustom) {
                                                        IconButton(onClick = { strategyToEdit = strategyItem }) {
                                                            Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                                                        }
                                                        IconButton(onClick = { strategyToDelete = strategyItem }) {
                                                            Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    IconButton(onClick = { viewModel.toggleFavorite(strategyItem) }) {
                                        Icon(
                                            imageVector = if (strategyItem.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                            contentDescription = "Toggle Favorite",
                                            tint = if (strategyItem.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
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
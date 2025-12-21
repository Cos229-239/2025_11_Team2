package com.reclaim.reclaim.ui.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
//import androidx.glance.appwidget.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reclaim.reclaim.data.JournalEntry
import com.reclaim.reclaim.ui.theme.AppTypography
import com.reclaim.reclaim.ui.viewmodels.SavedJournals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedJournalsScreen(
    navController: NavController,
    // Ensure you are using the Hilt-injected ViewModel
    viewModel: SavedJournals = hiltViewModel()
) {
    // This now collects a List<JournalEntry> from the cloud
    val entries by viewModel.entries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    "Saved Journals",
                    style = AppTypography.headlineLarge,
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
    ) { innerPadding ->
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No journal entries yet.\nWrite your first one 💜",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }   else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CORRECTED: The named parameter 'items' is removed.
                // The 'entries' list is passed directly.
                items(items = entries, key = { entry -> entry.id }) { entry ->
                    JournalListItem(
                        entry = entry,
                        onClick = { /* ... */ },
                        onDelete = { viewModel.deleteEntry(entry.id) }    )
                }
            }
        }
    }
}

@Composable
private fun JournalListItem(
    // FIX: This composable now expects the `JournalEntry` data class
    entry: JournalEntry,
    onClick: () -> Unit = {},
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(), // Make Row fill width
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = entry.date,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = entry.time,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.background
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete entry", tint = MaterialTheme.colorScheme.background)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = entry.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

package com.reclaim.reclaim.ui.journal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaim.reclaim.R
import com.reclaim.reclaim.ui.components.ProfileHeader
import com.reclaim.reclaim.ui.theme.WhiteTextFieldColors
import com.reclaim.reclaim.ui.viewmodels.SavedJournals
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.reclaim.reclaim.ui.components.HamburgerMenu
import kotlinx.coroutines.launch
import java.net.URLDecoder  // NEW: For decoding '+' appearing in pre-fill

/**
 * JournalScreen
 * -------------
 * Displays the user’s journal entries.
 * - Intended for daily reflections, mood tracking, and recovery notes.
 * - Will integrate with JournalEntity + JournalDao via Hilt.
 *
 * TODO:
 * - Fetch journal entries from Room using Hilt-injected ViewModel.
 * - Add UI for creating new entries (text field, save button).
 * - Display list of past entries with timestamps.
 * - Support editing/deleting entries.
 * - Add filtering (by date, mood, or tags).
 * - Consider adding AI-assisted suggestions for reflection prompts.
 * - Ensure accessibility (large text scaling, voice input).
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    name: String,
    navController: NavHostController,
    viewModel: SavedJournals = viewModel(),
    initialText: String = ""  // New (fixed): New param for pre-filling the entry field (defaults to empty)
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var entry by remember { mutableStateOf(java.net.URLDecoder.decode(initialText, "UTF-8")) }  // Decodes to remove '+'
    val date = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEEE, MMM d")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerMenu(
                navController = navController,
                currentRoute = "journal",
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                if (entry.isNotBlank()) {
                                    viewModel.addEntry(entry, date)
                                    navController.navigate("saved_journals")
                                }
                            },
                            enabled = entry.isNotBlank(),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Save")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFB59E7D),
                        navigationIconContentColor = Color.Black
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileHeader(
                    name = name,
                    photoRes = R.drawable.user
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Journal Entry",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = entry,
                    onValueChange = { entry = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = { Text("Release your thoughts here") },
                    colors = WhiteTextFieldColors(),
                    maxLines = Int.MAX_VALUE
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("saved_journals") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("View saved journals")
                }
            }
        }
    }
}


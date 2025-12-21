package com.reclaim.reclaim.ui.journal

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.reclaim.reclaim.R
import com.reclaim.reclaim.ui.components.HamburgerMenu
import com.reclaim.reclaim.ui.theme.CormorantGaramond
import com.reclaim.reclaim.ui.viewmodels.SavedJournals
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    name: String,
    navController: NavHostController,
    // This is the most critical change.
    // It uses Hilt to get the ViewModel that is connected to AuthRepository and the cloud.
    viewModel: SavedJournals = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val context = LocalContext.current

    var entryText by remember { mutableStateOf("") }
    val currentDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerMenu(
                navController = navController,
                currentRoute = "journal", // Assuming "journal" is the route for this screen
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
                        // This "Save" button is now correctly wired to the cloud.
                        TextButton(
                            onClick = {
                                if (entryText.isNotBlank()) {
                                    // This calls the ViewModel function that saves to Firestore.
                                    viewModel.addEntry(entryText, currentDate)

                                    // Give user feedback and navigate away.
                                    Toast.makeText(context, "Journal entry saved!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("saved_journals") {
                                        // Optional: Clear the back stack so the user can't go "back" to the entry they just saved.
                                        popUpTo("journal") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "Please write something before saving.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = entryText.isNotBlank() // Button is disabled until the user types something.
                        ) {
                            Text("Save")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = Color.Black
                    )
                )
            }
        ) { innerPadding ->
            // --- Your original UI layout is preserved below ---
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = R.drawable.reclaimcurrentpicture),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Journal Entry",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = currentDate,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = entryText,
                    onValueChange = { entryText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(top = 16.dp),
                    placeholder = {
                        Text(
                            text = "Release your thoughts here",
                            color = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    minLines = 6,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                        disabledIndicatorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("saved_journals") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "View saved journals",
                        fontFamily = CormorantGaramond,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

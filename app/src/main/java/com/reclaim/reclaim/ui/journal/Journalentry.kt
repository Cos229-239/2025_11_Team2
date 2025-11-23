package com.reclaim.reclaim.ui.journal

import com.reclaim.reclaim.R
import com.reclaim.reclaim.ui.components.ProfileHeader
import com.reclaim.reclaim.ui.components.BottomNavBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.viewmodels.SavedJournals
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reclaim.reclaim.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(name: String, navController: NavController, viewModel: SavedJournals) {

    var entry by remember { mutableStateOf("") }
    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d"))

    Scaffold(
        topBar =
            {
                TopAppBar(
                    title =
                        {
                            Text(
                                text = "Journal Entry",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                    navigationIcon =
                        {
                            IconButton(onClick = { navController.navigateUp() })
                            {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                    actions =
                        {
                            TextButton(onClick = {
                                viewModel.addEntry(entry, date)
                                navController.navigateUp()
                            },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            {
                                Text("Save")
                            }

                        },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
                )
            },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ProfileHeader(name = name, photoRes = R.drawable.user)

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Journal Entry",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = entry,
                onValueChange = { entry = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Release your thoughts here",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                minLines = 6
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.SavedJournals.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(text = "View saved journals")
            }
        }
    }
}



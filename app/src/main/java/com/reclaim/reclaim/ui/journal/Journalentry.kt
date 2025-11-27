package com.reclaim.reclaim.ui.journal

import androidx.compose.foundation.layout.Column
import com.reclaim.reclaim.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.components.BottomNavBar
import com.reclaim.reclaim.ui.components.ProfileHeader
import com.reclaim.reclaim.ui.viewmodels.SavedJournals
import com.reclaim.reclaim.data.entities.JournalEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    name: String,
    navController: NavController,
    viewModel: SavedJournals = viewModel() // <-- now backed by Room
) {
    var entry by remember { mutableStateOf("") }
    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Journal Entry", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (entry.isNotBlank()) {
                                viewModel.addEntry(entry, date) // <-- persist to Room
                                navController.navigateUp()
                            }
                        },
                        enabled = entry.isNotBlank()
                    ) {
                        Text("Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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

            Text("Journal Entry", style = MaterialTheme.typography.headlineSmall)
            Text(date, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = entry,
                onValueChange = { entry = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text("Release your thoughts here")
                },
                minLines = 6
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("saved_journals") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View saved journals")
            }
        }
    }
}

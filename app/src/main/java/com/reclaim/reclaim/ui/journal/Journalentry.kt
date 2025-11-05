package com.reclaim.reclaim.ui.journal

import com.reclaim.reclaim.R
import com.reclaim.reclaim.ui.components.ProfileHeader
import com.reclaim.reclaim.ui.components.BottomNavBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun JournalScreen(name: String) {
    val navController = rememberNavController()
    var entry by remember { mutableStateOf("") }
    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d"))

    Scaffold(
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

            Text("Journal Entry",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold)
            Text(date, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = entry,
                onValueChange = { entry = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Release your thought here", color = Color.Gray) },
                minLines = 6
            )
        }
    }
}
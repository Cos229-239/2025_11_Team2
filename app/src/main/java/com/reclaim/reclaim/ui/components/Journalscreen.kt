package com.reclaim.reclaim.ui.components

import com.reclaim.reclaim.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun JournalScreen(userName: String, modifier: Modifier = Modifier) {
    var entry: String by remember { mutableStateOf("") }
    val date = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ProfileHeader(name = userName, photoRes = R.drawable.user)

        Spacer(Modifier.height(16.dp))

        Text("Journal Entry", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Text(date, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = entry,
            onValueChange = { entry = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = { Text("Release your thought here", color = Color.Gray) },
            minLines = 6,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
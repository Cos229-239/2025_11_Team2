package com.reclaim.reclaim.ui.profile


import com.reclaim.reclaim.R
import com.reclaim.reclaim.ui.components.ProfileHeader
import com.reclaim.reclaim.ui.components.BottomNavBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

@Composable
fun ProfileScreen (
    name: String,
    navController: NavController)
{
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            ProfileHeader(
                name = name,
                photoRes = R.drawable.user,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                )

            Spacer(Modifier.height(16.dp))

            Text(
                "About me",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                thickness = 1.dp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactInformation(modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally))
        }
    }
}

@Composable
fun ContactInformation(modifier: Modifier = Modifier) {
    var phoneNumber by remember { mutableStateOf("867-5309") }
    var email by remember { mutableStateOf("william.henry.moody@my-own-personal-domain.com") }
    var hasSponsor by remember { mutableStateOf(true) }
    var sponsorName by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        Text(
            text = "Phone number: $phoneNumber",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        Text(
            text = "Email: $email",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sponsored",
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = hasSponsor,
                onCheckedChange = { hasSponsor = it },
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        if (hasSponsor) {
            OutlinedTextField(
                value = sponsorName,
                onValueChange = { sponsorName = it },
                label = { Text("Sponsor's Name") },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        } else {
            Text(
                text = "No sponsor",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Divider(
            thickness = 1.dp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Emergency Contacts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Suicide Hotline: 1-800-273-8255",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        Text(
            text = "Mental Health Helpline: 1-800-273-8255",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )


        Text(
            text = "Substance Abuse Helpline: 1-800-273-8255",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
    }
}

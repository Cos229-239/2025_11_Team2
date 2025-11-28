package com.reclaim.reclaim.ui.profile


import androidx.compose.foundation.Image
import com.reclaim.reclaim.R
import com.reclaim.reclaim.ui.components.BottomNavBar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.ui.theme.WhiteTextFieldColors

class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 10) text.text.substring(0..9) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 2 || i == 5) {
                out += "-"
            }
        }

        val phoneNumberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 10) return offset + 2
                return 12
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset - 1
                if (offset <= 12) return offset - 2
                return 10
            }
        }

        return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen (
    name: String,
    navController: NavController)
{
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Image(
                painter = painterResource(id = R.drawable.reclaimcurrentpicture),
                contentDescription = "Profile picture",
                modifier = Modifier.size(128.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                 text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )


            //Spacer(Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))
            Divider(
                thickness = 1.dp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactInformation()
        }
    }
}

@Composable
fun ContactInformation(modifier: Modifier = Modifier) {
    var phoneNumber by remember { mutableStateOf("867-5309") }
    var email by remember { mutableStateOf("william.henry.moody@my-own-personal-domain.com") }
    var hasSponsor by remember { mutableStateOf(true) }
    var sponsorName by remember { mutableStateOf("") }
    var sponsorPhoneNumber by remember { mutableStateOf("") }
    var sponsorEmail by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = WhiteTextFieldColors()

            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = sponsorPhoneNumber,
                onValueChange = { 
                    if (it.length <= 10) {
                        sponsorPhoneNumber = it.filter { char -> char.isDigit() } 
                    }
                },
                label = { Text("Sponsor's Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = PhoneNumberVisualTransformation(),
                colors = WhiteTextFieldColors()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = sponsorEmail,
                onValueChange = { sponsorEmail = it },
                label = { Text("Sponsor's Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = WhiteTextFieldColors()
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
            text = "Sobriety Improvement",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Before",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Image(
                    painter = painterResource(R.drawable.reclaimbeforepicture),
                    contentDescription = "Before photo placeholder",
                    modifier = Modifier
                        .size(120.dp)
                        .border(1.dp, Color.Gray)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Current",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.reclaimcurrentpicture),
                    contentDescription = "Current photo placeholder",
                    modifier = Modifier
                        .size(120.dp)
                        .border(1.dp, Color.Gray)

                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

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

package com.reclaim.reclaim.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.reclaim.reclaim.R
import com.reclaim.reclaim.navigation.Screen
import com.reclaim.reclaim.ui.components.HamburgerMenu
import com.reclaim.reclaim.ui.theme.WhiteTextFieldColors
import com.reclaim.reclaim.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Settings


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
    navController: NavController,
    name: String,
    viewModel: ProfileViewModel = hiltViewModel()
)
{
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) } // State for the dropdown menu
    var notificationsEnabled by remember { mutableStateOf(true)}
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            viewModel.onProfilePictureChanged(uri)
            showDialog = false
        }
    )

    val cameraImageUri: Uri = remember {
        val file = File(context.cacheDir, "camera_photo.jpg")
        FileProvider.getUriForFile(context, "com.reclaim.reclaim.fileprovider", file)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if(success) {
                viewModel.onProfilePictureChanged(cameraImageUri)
            }
            showDialog = false
        }
    )
    if(showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Profile Picture") },
            text = { Text("Choose an option to update your profile picture") },
            confirmButton = {
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("From Gallery")
                }
            },
            dismissButton = {
                Button(onClick = { cameraLauncher.launch(cameraImageUri) }) {
                    Text("From Camera")
                }
            }
        )
    }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerMenu(
                navController = navController as NavHostController,
                currentRoute = Screen.Profile.route,
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Profile") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings menu")
                        }
                        // This is the dropdown menu itself
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) { DropdownMenuItem(
                            text = { Text("Push Notifications") },
                            // The onClick for the item itself does nothing, as the switch handles the action.
                            onClick = { notificationsEnabled = !notificationsEnabled },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Notifications Icon"
                                )
                            },
                            // The trailingIcon is where we place the Switch.
                            trailingIcon = {
                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { notificationsEnabled = it }
                                )
                            }
                        )
                            // The "Logout" menu item
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    // Close the menu
                                    showMenu = false
                                    // Navigate to the login screen and clear the app's back stack
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            inclusive = true
                                        }
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Logout,
                                        contentDescription = "Logout Icon"
                                    )
                                }

                            )
                            // You can add more DropdownMenuItems here later (e.g., for a "Settings" screen)
                        }
                    },
                    // --- END OF NEW PART ---
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
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
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = uiState.profilePictureUri ?: R.drawable.reclaimcurrentpicture
                        ),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                    )

                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon (
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.userName,
                    onValueChange = { newName -> viewModel.onNameChange(newName) },
                    label = { Text("Name") },
                    readOnly = !uiState.isEditingName,
                    modifier = Modifier.fillMaxWidth(),
                    colors = WhiteTextFieldColors(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onEditModeChange(!uiState.isEditingName) }) {
                            Icon(
                                imageVector = if (uiState.isEditingName) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = if (uiState.isEditingName) "Save Name" else "Edit Name"
                            )
                        }
                    }
                )

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
}

@Composable
fun ContactInformation(modifier: Modifier = Modifier) {
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
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Suicide Hotline: 1-800-273-8255",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Mental Health Helpline: 1-800-273-8255",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        Text(
            text = "Substance Abuse Helpline: 1-800-273-8255",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

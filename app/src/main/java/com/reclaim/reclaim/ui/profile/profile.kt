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
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.reclaim.reclaim.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File // <-- FIX: Import 'java.io.File'

// This is a helper class, and it's fine where it is.
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

// This reusable composable is good.
@Composable
fun SobrietyImprovementImage(
    label: String,
    imageUrl: String?,
    placeholderResId: Int,
    onEditClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 4.dp))
        Box(contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    error = painterResource(placeholderResId),
                    placeholder = painterResource(placeholderResId)
                ),
                contentDescription = "$label photo",
                modifier = Modifier
                    .size(120.dp)
                    .border(1.dp, Color.Gray),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Edit, "Edit $label Photo", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen (
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- State Management for the Dialog ---
    var showMenu by remember { mutableStateOf(false) }
    // This state controls the dialog's visibility
    var showDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    // This state remembers WHICH image is being updated ("profile", "before", or "current")
    var imageTypeToUpdate by remember { mutableStateOf<String?>(null) }
    // This holds the temporary URI for the camera
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }


    // --- UNIFIED LAUNCHERS ---
    // Launcher for picking an image from the gallery.
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        // When an image is picked, use the 'imageTypeToUpdate' to call the ViewModel.
        imageTypeToUpdate?.let { type ->
            viewModel.onImageChanged(uri, type)
        }
        showDialog = false // Close the dialog
    }

    // Launcher for taking a picture with the camera.
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // If the picture was taken successfully, use the temp URI.
            imageTypeToUpdate?.let { type ->
                viewModel.onImageChanged(tempCameraUri, type)
            }
        }
        showDialog = false // Close the dialog
    }

    // --- The Dialog to Choose Camera or Gallery ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Picture") },
            text = { Text("Choose an option to update your picture.") },
            confirmButton = {
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("From Gallery")
                }
            },
            dismissButton = {
                Button(onClick = {
                    // Create a temporary file and URI for the camera to save the photo to
                    val file = File(context.cacheDir, "camera_photo_${System.currentTimeMillis()}.jpg")
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                    tempCameraUri = uri
                    cameraLauncher.launch(uri)
                }) {
                    Text("From Camera")
                }
            }
        )
    }

    // --- Navigation and Scaffold Setup (This part is correct) ---
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    // ... (Your ModalNavigationDrawer and Scaffold setup goes here) ...

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
                        // This button opens the hamburger menu
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        // This button opens the settings dropdown
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.Settings, "Settings menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Push Notifications") },
                                onClick = { notificationsEnabled = !notificationsEnabled },
                                leadingIcon = { Icon(Icons.Default.Notifications, null) },
                                trailingIcon = { Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it }) }
                            )
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    showMenu = false
                                    // You should ideally call viewModel.logout() here
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                    }
                                },
                                leadingIcon = { Icon(Icons.Default.Logout, null) }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- Main Profile Picture Section ---
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = uiState.profilePictureUrl,
                            error = painterResource(R.drawable.user),
                            placeholder = painterResource(R.drawable.user)
                        ),
                        contentDescription = "Profile picture",
                        modifier = Modifier.size(120.dp).clip(CircleShape).border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    // FIX: onClick now sets the type and shows the dialog
                    IconButton(
                        onClick = {
                            imageTypeToUpdate = "profile"
                            showDialog = true
                        },
                        modifier = Modifier.size(30.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Edit, "Edit Profile Picture", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.userName,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Name") },
                    // The text field is read-only unless we are in edit mode
                    readOnly = !uiState.isEditingName,
                    modifier = Modifier.fillMaxWidth(),
                    // Add a trailing icon to toggle edit/save
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onEditModeChange(!uiState.isEditingName) }) {
                            Icon(
                                // Show a Check icon when editing, or an Edit icon when not
                                imageVector = if (uiState.isEditingName) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = if (uiState.isEditingName) "Save Name" else "Edit Name"
                            )
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Divider(thickness = 1.dp, color = Color.LightGray)
                Spacer(Modifier.height(16.dp))

                // --- Sobriety Improvement Section ---
                Text(
                    text = "Sobriety Improvement",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SobrietyImprovementImage(
                        label = "Before",
                        imageUrl = uiState.beforePictureUrl,
                        placeholderResId = R.drawable.user,
                        // FIX: onClick now sets the type and shows the dialog
                        onEditClick = {
                            imageTypeToUpdate = "before"
                            showDialog = true
                        }
                    )
                    SobrietyImprovementImage(
                        label = "Current",
                        imageUrl = uiState.currentPictureUrl,
                        placeholderResId = R.drawable.user,
                        // FIX: onClick now sets the type and shows the dialog
                        onEditClick = {
                            imageTypeToUpdate = "current"
                            showDialog = true
                        }
                    )
                }

                Spacer(Modifier.height(16.dp))
                Divider(thickness = 1.dp, color = Color.LightGray)
                Spacer(Modifier.height(16.dp))

                // --- Sponsor and Emergency Contacts Section ---
                ContactInformation()
            }
        }
    }
}


@Composable
fun ContactInformation(modifier: Modifier = Modifier) {
    // This composable now ONLY manages its own local state.
    var hasSponsor by remember { mutableStateOf(true) }
    var sponsorName by remember { mutableStateOf("") }
    var sponsorPhoneNumber by remember { mutableStateOf("") }
    var sponsorEmail by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Sponsor Section ---
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Sponsored", style = MaterialTheme.typography.bodyMedium)
            Switch(checked = hasSponsor, onCheckedChange = { hasSponsor = it }, modifier = Modifier.padding(start = 8.dp))
        }
        if (hasSponsor) {
            OutlinedTextField(
                value = sponsorName,
                onValueChange = { sponsorName = it },
                label = { Text("Sponsor's Name") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = sponsorPhoneNumber,
                onValueChange = {
                    if (it.length <= 10) sponsorPhoneNumber = it.filter { char -> char.isDigit() }
                },
                label = { Text("Sponsor's Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = PhoneNumberVisualTransformation()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = sponsorEmail,
                onValueChange = { sponsorEmail = it },
                label = { Text("Sponsor's Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        } else {
            Text(
                text = "No sponsor",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))

        // --- Emergency Contacts Section ---
        Text(
            text = "Emergency Contacts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Suicide Hotline: 1-800-273-8255", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Mental Health Helpline: 1-800-273-8255", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = "Substance Abuse Helpline: 1-800-273-8255", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
    }
}

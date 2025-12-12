package com.reclaim.reclaim.ui.components

//package com.reclaim.reclaim.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reclaim.reclaim.navigation.Screen // Make sure to import your Screen class

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Reclaim",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Email Text Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Text Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (isPasswordVisible) "Hide password" else "Show password"
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                // TODO: Add authentication logic here (e.g., check email/password)
                // For now, we'll just navigate to the Home screen on click.
                navController.navigate(Screen.Home.route) {
                    // This clears the back stack, so the user can't go back to the login screen.
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Login")
        }

        // Optional: Add a "Sign Up" button or link
        TextButton(
            onClick = { navController.navigate(Screen.SignUp.route) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Don't have an account? Sign Up")
        }
    }
}

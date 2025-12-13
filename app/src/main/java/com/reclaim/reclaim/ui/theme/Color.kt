package com.reclaim.reclaim.ui.theme

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val FieldsAfar = Color(0xFF365437)
val PeachCream = Color(0xFFEFE7DA)
val Tobacco   = Color(0xFFB59E7D)

@Composable
fun WhiteTextFieldColors(
    focusedContainerColor: Color,
    unfocusedContainerColor: Color,
    disabledContainerColor: Color,
    focusedIndicatorColor: Color,
    unfocusedIndicatorColor: Color,
    cursorColor: Color,
    focusedTextColor: Color,
    unfocusedTextColor: Color,
    focusedPlaceholderColor: Color,
    unfocusedPlaceholderColor: Color
): TextFieldColors =
    TextFieldDefaults.colors(
        focusedContainerColor = PeachCream,
        unfocusedContainerColor = PeachCream,
        disabledContainerColor = PeachCream.copy(alpha = 0.7f),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        cursorColor = FieldsAfar,
        focusedTextColor = Color(0xFF2A2218),
        unfocusedTextColor = Color(0xFF2A2218),
        focusedLabelColor = Color(0xFF6B5A40),
        unfocusedLabelColor = Color(0xFF6B5A40)
    )
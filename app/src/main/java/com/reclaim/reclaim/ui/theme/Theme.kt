package com.reclaim.reclaim.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



private val LightColorScheme = lightColorScheme(
    primary = FieldsAfar,
    onPrimary = Color(0xFF202618),

    secondary = PeachCream,
    onSecondary = Color(0xFF2A2218),

    background = Tobacco,
    onBackground = Color(0xFF22170E),

    surface = PeachCream,
    onSurface = Color(0xFF2A2218),

    surfaceVariant = PeachCream,
    onSurfaceVariant = Color(0xFF3B3022),

    outline = PeachCream
)


private val DarkColorScheme = darkColorScheme(
    primary = FieldsAfar,
    onPrimary = Color(0xFF11130C),

    secondary = Tobacco,
    onSecondary = PeachCream,

    background = Color(0xFF14110F),
    onBackground = PeachCream,

    surface = Color(0xFF1E1A17),
    onSurface = PeachCream,

    surfaceVariant = Color(0xFF2B241E),
    onSurfaceVariant = PeachCream,

    outline = Tobacco
)


@Composable
fun ReclaimTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
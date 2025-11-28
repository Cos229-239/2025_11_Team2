package com.reclaim.reclaim.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = TextPrimaryLight,
    primaryContainer = TealLight,
    onPrimaryContainer = TextPrimaryDark,

    secondary = PurpleAccent,
    onSecondary = TextPrimaryLight,
    secondaryContainer = PurpleDark,
    onSecondaryContainer = TextPrimaryLight,

    tertiary = OrangeAccent,
    onTertiary = TextPrimaryDark,
    tertiaryContainer = OrangeDark,
    onTertiaryContainer = TextPrimaryLight,

    background = BeigeBackground,
    onBackground = TextPrimaryDark,

    surface = SurfaceLight,
    onSurface = TextPrimaryDark,

    error = OrangeAccent,
    onError = TextPrimaryDark
)

private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = TealDark,
    onPrimary = TextPrimaryLight,
    primaryContainer = TealPrimary,
    onPrimaryContainer = TextPrimaryLight,

    secondary = PurpleDark,
    onSecondary = TextPrimaryLight,
    secondaryContainer = PurpleAccent,
    onSecondaryContainer = TextPrimaryLight,

    tertiary = OrangeDark,
    onTertiary = TextPrimaryLight,
    tertiaryContainer = OrangeAccent,
    onTertiaryContainer = TextPrimaryDark,

    background = SurfaceDark,
    onBackground = TextPrimaryLight,

    surface = SurfaceDark,
    onSurface = TextPrimaryLight,

    error = OrangeAccent,
    onError = TextPrimaryDark
)
@Composable
fun WhiteTextFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = MaterialTheme.colorScheme.onBackground,
    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
)




@Composable
fun ReclaimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

}

package com.example.medicinereminder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Use the colors defined in Color.kt
private val DarkColorScheme = darkColorScheme(
    primary = primaryBlue, // Use your custom blue for dark theme
    secondary = secondaryTeal,
    background = BackgroundDark,
    surface = SurfaceDark,
    // ... add more colors as needed
)

private val LightColorScheme = lightColorScheme(
    primary = primaryBlue,
    secondary = secondaryTeal,
    background = BackgroundLight,
    surface = SurfaceLight,
    // ... add more colors as needed
)

@Composable
fun MedicineReminderTheme(
    darkTheme: Boolean = false, // Use isSystemInDarkTheme() for production
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // References the 'Typography' object defined in Typography.kt
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
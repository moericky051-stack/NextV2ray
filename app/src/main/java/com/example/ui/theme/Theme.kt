package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NextV2rayDarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Color.Black,
    primaryContainer = CyberCyanGlow,
    onPrimaryContainer = CyberCyan,
    secondary = CyberEmerald,
    onSecondary = Color.Black,
    secondaryContainer = CyberEmeraldGlow,
    onSecondaryContainer = CyberEmerald,
    tertiary = CyberAmber,
    background = CyberBackground,
    onBackground = CyberTextPrimary,
    surface = CyberSurface,
    onSurface = CyberTextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = CyberTextSecondary,
    outline = CyberCardBorder,
    error = CyberRose
)

@Composable
fun NextV2rayTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NextV2rayDarkColorScheme,
        typography = Typography,
        content = content
    )
}

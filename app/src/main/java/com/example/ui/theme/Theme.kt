package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AegisDarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = OnElectricCyan,
    primaryContainer = ElectricCyanContainer,
    onPrimaryContainer = Color(0xFFB2F5FF),
    secondary = EmeraldShield,
    onSecondary = OnEmeraldShield,
    secondaryContainer = EmeraldShieldContainer,
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = AmberRoute,
    onTertiary = Color(0xFF1F1300),
    tertiaryContainer = AmberRouteContainer,
    onTertiaryContainer = Color(0xFFFDE68A),
    error = CoralAlert,
    background = ObsidianBackground,
    onBackground = TextPrimaryDark,
    surface = ObsidianSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineDark
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AegisDarkColorScheme,
        typography = Typography,
        content = content
    )
}

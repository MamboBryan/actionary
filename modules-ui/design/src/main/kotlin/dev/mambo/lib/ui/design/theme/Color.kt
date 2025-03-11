package dev.mambo.lib.ui.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightPrimary = Color(0xFF52B799)
val LightOnPrimary = Color(0xFF000000)
val LightBackground = Color(0xFFFFFFFF)
val LightOnBackground = Color(0xFF000000)
val LightSurface = Color(0xFFE2F3EE)
val LightOnSurface = Color(0xFF000000)

val DarkPrimary = Color(0xFF8CCFBB)
val DarkOnPrimary = Color(0xFF000000)
val DarkBackground = Color(0xFF060F0C)
val DarkOnBackground = Color(0xFFFFFFFF)
val DarkSurface = Color(0xFF0C1D17)
val DarkOnSurface = Color(0xFFFFFFFF)

private val DarkColorScheme =
    darkColorScheme(
        primary = DarkPrimary,
        onPrimary = DarkOnPrimary,
        background = DarkBackground,
        onBackground = DarkOnBackground,
        surface = DarkSurface,
        onSurface = DarkOnSurface,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = LightPrimary,
        onPrimary = LightOnPrimary,
        background = LightBackground,
        onBackground = LightOnBackground,
        surface = LightSurface,
        onSurface = LightOnSurface,
    )

val ColorScheme: ColorScheme
    @Composable
    get() = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme

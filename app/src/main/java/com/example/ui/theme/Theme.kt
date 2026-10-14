package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DbrTeal,
    onPrimary = DbrSurface,
    primaryContainer = DbrMintTeal,
    onPrimaryContainer = DbrDarkTeal,
    secondary = DbrOrange,
    onSecondary = DbrSurface,
    secondaryContainer = DbrOrangeLight,
    onSecondaryContainer = DbrOrangeDark,
    tertiary = DbrLightTeal,
    background = DbrBackground,
    onBackground = DbrTextDark,
    surface = DbrSurface,
    onSurface = DbrTextDark,
    surfaceVariant = DbrMintTeal,
    onSurfaceVariant = DbrTextMuted,
    outline = DbrCardBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF10B981),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF041E17),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF13382F),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFD1FAE5),
    secondary = DbrOrange,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF45220E),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFFFD8BF),
    tertiary = androidx.compose.ui.graphics.Color(0xFF34D399),
    background = androidx.compose.ui.graphics.Color(0xFF08110F),
    onBackground = androidx.compose.ui.graphics.Color(0xFFF1F5F9),
    surface = androidx.compose.ui.graphics.Color(0xFF101E1B),
    onSurface = androidx.compose.ui.graphics.Color(0xFFF1F5F9),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF182B26),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF94A3B8),
    outline = androidx.compose.ui.graphics.Color(0xFF1C362F)
)

@Composable
fun DbrCarWorkshopTheme(
    language: String = "bn",
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = if (language == "bn") BanglaTypography else EnglishTypography
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = (if (darkTheme) androidx.compose.ui.graphics.Color(0xFF061512) else DbrDarkTeal).toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

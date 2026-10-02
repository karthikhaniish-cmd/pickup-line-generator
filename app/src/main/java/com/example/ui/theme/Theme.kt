package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RizzColorScheme = darkColorScheme(
    primary = NeonViolet,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2E1065),
    onPrimaryContainer = Color(0xFFE9D5FF),
    secondary = NeonPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF500724),
    onSecondaryContainer = Color(0xFFFCE7F3),
    tertiary = NeonBlue,
    onTertiary = Color.White,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    outlineVariant = Color(0xFF1E293B)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    RizzTheme(content = content)
}

@Composable
fun RizzTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = DarkBg.toArgb()
                window.navigationBarColor = DarkBg.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = RizzColorScheme,
        typography = Typography,
        content = content
    )
}

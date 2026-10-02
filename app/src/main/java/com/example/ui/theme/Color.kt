package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// RIZZAI Palette
val DarkBg = Color(0xFF080B14)
val DarkSurface = Color(0xFF111827)
val DarkCard = Color(0xFF161F33)
val DarkCardBorder = Color(0xFF232D48)
val DarkSurfaceVariant = Color(0xFF1F293D)

val NeonViolet = Color(0xFF8B5CF6)
val NeonPink = Color(0xFFEC4899)
val NeonBlue = Color(0xFF3B82F6)
val NeonCyan = Color(0xFF06B6D4)
val NeonAmber = Color(0xFFF59E0B)
val NeonGreen = Color(0xFF10B981)

val TextPrimary = Color(0xFFF9FAFB)
val TextSecondary = Color(0xFF9CA3AF)
val TextMuted = Color(0xFF6B7280)

val ErrorRed = Color(0xFFEF4444)

// Gradient Brushes
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(NeonViolet, NeonPink)
)

val CardGlowGradient = Brush.linearGradient(
    colors = listOf(NeonViolet.copy(alpha = 0.25f), NeonPink.copy(alpha = 0.15f), Color.Transparent)
)

val HeroGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1E1038), DarkBg)
)

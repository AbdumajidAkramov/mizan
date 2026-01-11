package dev.esbi.mizan.ui.theme.colors

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.ui.theme.*

// ==========================================
// PREMIUM LIGHT COLOR SCHEME
// ==========================================

val PremiumLightColorScheme = lightColorScheme(
    // Primary Colors
    primary = PremiumPrimary,
    onPrimary = Color.White,
    primaryContainer = PremiumPrimaryLight,
    onPrimaryContainer = PremiumPrimaryDark,

    // Secondary Colors
    secondary = PremiumSecondary,
    onSecondary = Color.White,
    secondaryContainer = PremiumSecondaryLight,
    onSecondaryContainer = PremiumSecondaryDark,

    // Tertiary Colors
    tertiary = PremiumSuccess,
    onTertiary = Color.Black,
    tertiaryContainer = PremiumSuccessLight,
    onTertiaryContainer = PremiumSuccessDark,

    // Error Colors
    error = PremiumError,
    onError = Color.White,
    errorContainer = PremiumErrorLight,
    onErrorContainer = PremiumErrorDark,

    // Background
    background = PremiumBgPrimaryLight,
    onBackground = PremiumTextPrimaryLight,

    // Surface
    surface = PremiumBgSecondaryLight,
    onSurface = PremiumTextPrimaryLight,
    surfaceVariant = PremiumSurface2Light,
    onSurfaceVariant = PremiumTextSecondaryLight,

    // Surface Containers
    surfaceContainer = PremiumSurface2Light,
    surfaceContainerHigh = PremiumSurface3Light,
    surfaceContainerHighest = PremiumSurface4Light,
    surfaceContainerLow = PremiumSurface1Light,
    surfaceContainerLowest = PremiumBgSecondaryLight,

    // Outline
    outline = PremiumTextTertiaryLight,
    outlineVariant = PremiumTextMutedLight,

    // Inverse Colors
    inverseSurface = PremiumTextPrimaryLight,
    inverseOnSurface = PremiumBgPrimaryLight,
    inversePrimary = PremiumPrimaryDark,

    // Surface Tint
    surfaceTint = PremiumPrimary,

    // Scrim
    scrim = Color.Black.copy(alpha = 0.32f)
)


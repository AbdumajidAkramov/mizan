package dev.esbi.mizan.ui.theme.colors

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.ui.theme.PremiumBgPrimaryDark
import dev.esbi.mizan.ui.theme.PremiumBgSecondaryDark
import dev.esbi.mizan.ui.theme.PremiumError
import dev.esbi.mizan.ui.theme.PremiumErrorDark
import dev.esbi.mizan.ui.theme.PremiumErrorLight
import dev.esbi.mizan.ui.theme.PremiumPrimary
import dev.esbi.mizan.ui.theme.PremiumPrimaryDark
import dev.esbi.mizan.ui.theme.PremiumPrimaryLight
import dev.esbi.mizan.ui.theme.PremiumSecondary
import dev.esbi.mizan.ui.theme.PremiumSecondaryDark
import dev.esbi.mizan.ui.theme.PremiumSecondaryLight
import dev.esbi.mizan.ui.theme.PremiumSuccess
import dev.esbi.mizan.ui.theme.PremiumSuccessDark
import dev.esbi.mizan.ui.theme.PremiumSuccessLight
import dev.esbi.mizan.ui.theme.PremiumSurface1Dark
import dev.esbi.mizan.ui.theme.PremiumSurface2Dark
import dev.esbi.mizan.ui.theme.PremiumSurface3Dark
import dev.esbi.mizan.ui.theme.PremiumSurface4Dark
import dev.esbi.mizan.ui.theme.PremiumTextMutedDark
import dev.esbi.mizan.ui.theme.PremiumTextPrimaryDark
import dev.esbi.mizan.ui.theme.PremiumTextSecondaryDark
import dev.esbi.mizan.ui.theme.PremiumTextTertiaryDark

// ==========================================
// PREMIUM DARK COLOR SCHEME
// ==========================================

val PremiumDarkColorScheme = darkColorScheme(
    // Primary Colors
    primary = PremiumPrimary,
    onPrimary = Color.White,
    primaryContainer = PremiumPrimaryDark,
    onPrimaryContainer = PremiumPrimaryLight,

    // Secondary Colors
    secondary = PremiumSecondary,
    onSecondary = Color.White,
    secondaryContainer = PremiumSecondaryDark,
    onSecondaryContainer = PremiumSecondaryLight,

    // Tertiary Colors
    tertiary = PremiumSuccess,
    onTertiary = Color.Black,
    tertiaryContainer = PremiumSuccessDark,
    onTertiaryContainer = PremiumSuccessLight,

    // Error Colors
    error = PremiumError,
    onError = Color.White,
    errorContainer = PremiumErrorDark,
    onErrorContainer = PremiumErrorLight,

    // Background
    background = PremiumBgPrimaryDark,
    onBackground = PremiumTextPrimaryDark,

    // Surface
    surface = PremiumBgSecondaryDark,
    onSurface = PremiumTextPrimaryDark,
    surfaceVariant = PremiumSurface2Dark,
    onSurfaceVariant = PremiumTextSecondaryDark,

    // Surface Containers
    surfaceContainer = PremiumSurface2Dark,
    surfaceContainerHigh = PremiumSurface3Dark,
    surfaceContainerHighest = PremiumSurface4Dark,
    surfaceContainerLow = PremiumSurface1Dark,
    surfaceContainerLowest = PremiumBgSecondaryDark,

    // Outline
    outline = PremiumTextTertiaryDark,
    outlineVariant = PremiumTextMutedDark,

    // Inverse Colors
    inverseSurface = PremiumTextPrimaryDark,
    inverseOnSurface = PremiumBgPrimaryDark,
    inversePrimary = PremiumPrimaryLight,

    // Surface Tint
    surfaceTint = PremiumPrimary,

    // Scrim
    scrim = Color.Black.copy(alpha = 0.32f)
)

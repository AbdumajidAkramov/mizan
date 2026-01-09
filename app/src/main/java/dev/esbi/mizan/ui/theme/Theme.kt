package dev.esbi.mizan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Mizan Design System - Theme Configuration
 * Supports both Premium and Material3 color schemes
 * Derived from premium-theme.css and material3-theme.css
 */

// ==========================================
// PREMIUM DARK COLOR SCHEME
// ==========================================

private val PremiumDarkColorScheme = darkColorScheme(
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

// ==========================================
// PREMIUM LIGHT COLOR SCHEME
// ==========================================

private val PremiumLightColorScheme = lightColorScheme(
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

// ==========================================
// MATERIAL 3 DARK COLOR SCHEME
// ==========================================

private val Material3DarkColorScheme = darkColorScheme(
    primary = M3PrimaryDark,
    onPrimary = M3OnPrimaryDark,
    primaryContainer = M3PrimaryContainerDark,
    onPrimaryContainer = M3OnPrimaryContainerDark,
    
    secondary = M3SecondaryDark,
    onSecondary = M3OnSecondaryDark,
    secondaryContainer = M3SecondaryContainerDark,
    onSecondaryContainer = M3OnSecondaryContainerDark,
    
    tertiary = M3TertiaryDark,
    onTertiary = M3OnTertiaryDark,
    tertiaryContainer = M3TertiaryContainerDark,
    onTertiaryContainer = M3OnTertiaryContainerDark,
    
    error = M3ErrorDark,
    onError = M3OnErrorDark,
    errorContainer = M3ErrorContainerDark,
    onErrorContainer = M3OnErrorContainerDark,
    
    background = M3BackgroundDark,
    onBackground = M3OnBackgroundDark,
    
    surface = M3SurfaceDark,
    onSurface = M3OnSurfaceDark,
    surfaceVariant = M3SurfaceVariantDark,
    onSurfaceVariant = M3OnSurfaceVariantDark,
    
    surfaceContainer = M3SurfaceContainerDark,
    surfaceContainerHigh = M3SurfaceContainerHighDark,
    surfaceContainerHighest = M3SurfaceContainerHighestDark,
    surfaceContainerLow = M3SurfaceContainerLowDark,
    surfaceContainerLowest = M3SurfaceContainerLowestDark,
    
    outline = M3OutlineDark,
    outlineVariant = M3OutlineVariantDark,
    
    inverseSurface = M3OnSurfaceDark,
    inverseOnSurface = M3SurfaceDark,
    inversePrimary = M3PrimaryDark,
    
    surfaceTint = M3PrimaryDark,
    scrim = Color.Black
)

// ==========================================
// MATERIAL 3 LIGHT COLOR SCHEME
// ==========================================

private val Material3LightColorScheme = lightColorScheme(
    primary = M3PrimaryLight,
    onPrimary = M3OnPrimaryLight,
    primaryContainer = M3PrimaryContainerLight,
    onPrimaryContainer = M3OnPrimaryContainerLight,
    
    secondary = M3SecondaryLight,
    onSecondary = M3OnSecondaryLight,
    secondaryContainer = M3SecondaryContainerLight,
    onSecondaryContainer = M3OnSecondaryContainerLight,
    
    tertiary = M3TertiaryLight,
    onTertiary = M3OnTertiaryLight,
    tertiaryContainer = M3TertiaryContainerLight,
    onTertiaryContainer = M3OnTertiaryContainerLight,
    
    error = M3ErrorLight,
    onError = M3OnErrorLight,
    errorContainer = M3ErrorContainerLight,
    onErrorContainer = M3OnErrorContainerLight,
    
    background = M3BackgroundLight,
    onBackground = M3OnBackgroundLight,
    
    surface = M3SurfaceLight,
    onSurface = M3OnSurfaceLight,
    surfaceVariant = M3SurfaceVariantLight,
    onSurfaceVariant = M3OnSurfaceVariantLight,
    
    surfaceContainer = M3SurfaceContainerLight,
    surfaceContainerHigh = M3SurfaceContainerHighLight,
    surfaceContainerHighest = M3SurfaceContainerHighestLight,
    surfaceContainerLow = M3SurfaceContainerLowLight,
    surfaceContainerLowest = M3SurfaceContainerLowestLight,
    
    outline = M3OutlineLight,
    outlineVariant = M3OutlineVariantLight,
    
    inverseSurface = M3OnSurfaceLight,
    inverseOnSurface = M3SurfaceLight,
    inversePrimary = M3PrimaryLight,
    
    surfaceTint = M3PrimaryLight,
    scrim = Color.Black
)

// ==========================================
// MIZAN THEME COMPOSABLE
// ==========================================

/**
 * Main theme composable for Mizan app
 * 
 * @param darkTheme Whether to use dark theme (defaults to system preference)
 * @param dynamicColor Whether to use dynamic color (Android 12+)
 * @param usePremiumTheme Whether to use Premium theme (true) or Material3 theme (false)
 * @param content The composable content
 */
@Composable
fun MizanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    usePremiumTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        usePremiumTheme -> {
            if (darkTheme) PremiumDarkColorScheme else PremiumLightColorScheme
        }
        else -> {
            if (darkTheme) Material3DarkColorScheme else Material3LightColorScheme
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Premium-specific theme composable
 * Always uses Premium color scheme
 */
@Composable
fun PremiumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MizanTheme(
        darkTheme = darkTheme,
        dynamicColor = false,
        usePremiumTheme = true,
        content = content
    )
}
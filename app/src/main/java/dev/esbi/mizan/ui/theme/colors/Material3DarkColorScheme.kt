package dev.esbi.mizan.ui.theme.colors

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.ui.theme.M3BackgroundDark
import dev.esbi.mizan.ui.theme.M3ErrorContainerDark
import dev.esbi.mizan.ui.theme.M3ErrorDark
import dev.esbi.mizan.ui.theme.M3OnBackgroundDark
import dev.esbi.mizan.ui.theme.M3OnErrorContainerDark
import dev.esbi.mizan.ui.theme.M3OnErrorDark
import dev.esbi.mizan.ui.theme.M3OnPrimaryContainerDark
import dev.esbi.mizan.ui.theme.M3OnPrimaryDark
import dev.esbi.mizan.ui.theme.M3OnSecondaryContainerDark
import dev.esbi.mizan.ui.theme.M3OnSecondaryDark
import dev.esbi.mizan.ui.theme.M3OnSurfaceDark
import dev.esbi.mizan.ui.theme.M3OnSurfaceVariantDark
import dev.esbi.mizan.ui.theme.M3OnTertiaryContainerDark
import dev.esbi.mizan.ui.theme.M3OnTertiaryDark
import dev.esbi.mizan.ui.theme.M3OutlineDark
import dev.esbi.mizan.ui.theme.M3OutlineVariantDark
import dev.esbi.mizan.ui.theme.M3PrimaryContainerDark
import dev.esbi.mizan.ui.theme.M3PrimaryDark
import dev.esbi.mizan.ui.theme.M3SecondaryContainerDark
import dev.esbi.mizan.ui.theme.M3SecondaryDark
import dev.esbi.mizan.ui.theme.M3SurfaceContainerDark
import dev.esbi.mizan.ui.theme.M3SurfaceContainerHighDark
import dev.esbi.mizan.ui.theme.M3SurfaceContainerHighestDark
import dev.esbi.mizan.ui.theme.M3SurfaceContainerLowDark
import dev.esbi.mizan.ui.theme.M3SurfaceContainerLowestDark
import dev.esbi.mizan.ui.theme.M3SurfaceDark
import dev.esbi.mizan.ui.theme.M3SurfaceVariantDark
import dev.esbi.mizan.ui.theme.M3TertiaryContainerDark
import dev.esbi.mizan.ui.theme.M3TertiaryDark

// ==========================================
// MATERIAL 3 DARK COLOR SCHEME
// ==========================================

val Material3DarkColorScheme = darkColorScheme(
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

package dev.esbi.mizan.ui.theme.colors

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.ui.theme.M3BackgroundLight
import dev.esbi.mizan.ui.theme.M3ErrorContainerLight
import dev.esbi.mizan.ui.theme.M3ErrorLight
import dev.esbi.mizan.ui.theme.M3OnBackgroundLight
import dev.esbi.mizan.ui.theme.M3OnErrorContainerLight
import dev.esbi.mizan.ui.theme.M3OnErrorLight
import dev.esbi.mizan.ui.theme.M3OnPrimaryContainerLight
import dev.esbi.mizan.ui.theme.M3OnPrimaryLight
import dev.esbi.mizan.ui.theme.M3OnSecondaryContainerLight
import dev.esbi.mizan.ui.theme.M3OnSecondaryLight
import dev.esbi.mizan.ui.theme.M3OnSurfaceLight
import dev.esbi.mizan.ui.theme.M3OnSurfaceVariantLight
import dev.esbi.mizan.ui.theme.M3OnTertiaryContainerLight
import dev.esbi.mizan.ui.theme.M3OnTertiaryLight
import dev.esbi.mizan.ui.theme.M3OutlineLight
import dev.esbi.mizan.ui.theme.M3OutlineVariantLight
import dev.esbi.mizan.ui.theme.M3PrimaryContainerLight
import dev.esbi.mizan.ui.theme.M3PrimaryLight
import dev.esbi.mizan.ui.theme.M3SecondaryContainerLight
import dev.esbi.mizan.ui.theme.M3SecondaryLight
import dev.esbi.mizan.ui.theme.M3SurfaceContainerHighLight
import dev.esbi.mizan.ui.theme.M3SurfaceContainerHighestLight
import dev.esbi.mizan.ui.theme.M3SurfaceContainerLight
import dev.esbi.mizan.ui.theme.M3SurfaceContainerLowLight
import dev.esbi.mizan.ui.theme.M3SurfaceContainerLowestLight
import dev.esbi.mizan.ui.theme.M3SurfaceLight
import dev.esbi.mizan.ui.theme.M3SurfaceVariantLight
import dev.esbi.mizan.ui.theme.M3TertiaryContainerLight
import dev.esbi.mizan.ui.theme.M3TertiaryLight

// ==========================================
// MATERIAL 3 LIGHT COLOR SCHEME
// ==========================================

val Material3LightColorScheme = lightColorScheme(
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

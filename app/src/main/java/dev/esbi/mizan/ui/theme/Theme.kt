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

private val PremiumDarkColorScheme = darkColorScheme(
    primary = PremiumPrimary,
    onPrimary = Color.White,
    primaryContainer = PremiumPrimaryDark,
    onPrimaryContainer = PremiumPrimaryLight,
    
    secondary = PremiumSecondary,
    onSecondary = Color.White,
    secondaryContainer = PremiumSecondaryDark,
    onSecondaryContainer = PremiumSecondaryLight,
    
    tertiary = PremiumSuccess,
    onTertiary = Color.Black,
    tertiaryContainer = PremiumSuccessDark,
    onTertiaryContainer = PremiumSuccessLight,
    
    error = PremiumError,
    onError = Color.White,
    errorContainer = PremiumErrorDark,
    onErrorContainer = PremiumErrorLight,
    
    background = PremiumBgPrimaryDark,
    onBackground = PremiumTextPrimaryDark,
    
    surface = PremiumBgSecondaryDark,
    onSurface = PremiumTextPrimaryDark,
    surfaceVariant = PremiumSurface2Dark,
    onSurfaceVariant = PremiumTextSecondaryDark,
    
    outline = PremiumTextTertiaryDark,
    outlineVariant = PremiumTextMutedDark,
)

private val PremiumLightColorScheme = lightColorScheme(
    primary = PremiumPrimary,
    onPrimary = Color.White,
    primaryContainer = PremiumPrimaryLight,
    onPrimaryContainer = PremiumPrimaryDark,
    
    secondary = PremiumSecondary,
    onSecondary = Color.White,
    secondaryContainer = PremiumSecondaryLight,
    onSecondaryContainer = PremiumSecondaryDark,
    
    tertiary = PremiumSuccess,
    onTertiary = Color.Black,
    tertiaryContainer = PremiumSuccessLight,
    onTertiaryContainer = PremiumSuccessDark,
    
    error = PremiumError,
    onError = Color.White,
    errorContainer = PremiumErrorLight,
    onErrorContainer = PremiumErrorDark,
    
    background = PremiumBgPrimaryLight,
    onBackground = PremiumTextPrimaryLight,
    
    surface = PremiumBgSecondaryLight,
    onSurface = PremiumTextPrimaryLight,
    surfaceVariant = PremiumSurface2Light,
    onSurfaceVariant = PremiumTextSecondaryLight,
    
    outline = PremiumTextTertiaryLight,
    outlineVariant = PremiumTextMutedLight,
)

@Composable
fun MizanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> PremiumDarkColorScheme
        else -> PremiumLightColorScheme
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
package dev.esbi.mizan.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import dev.esbi.mizan.ui.theme.colors.Material3DarkColorScheme
import dev.esbi.mizan.ui.theme.colors.Material3LightColorScheme
import dev.esbi.mizan.ui.theme.colors.PremiumDarkColorScheme
import dev.esbi.mizan.ui.theme.colors.PremiumLightColorScheme

/**
 * Mizan Design System - Theme Configuration
 * Supports both Premium and Material3 color schemes
 * Derived from premium-theme.css and material3-theme.css
 */



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
/*
    val cornerRadius = mizanCornerRadius()
    val colors = mizanColors(
        isDarkTheme = darkTheme,
        isPremiumTheme = usePremiumTheme
    )
    LaunchedEffect(colors) {
        Log.d("MizanTheme", "colors = $colors")
    }

    val typography = mizanTypography(color = colors.functional.textPrimary)
    CompositionLocalProvider(
        LocalMizanColors provides colors,
        LocalMizanCornerRadius provides cornerRadius,
        LocalMizanTypography provides typography,
    ) {
        ProvideTextStyle(value = typography.bodyLG) {
            Box(
                modifier = Modifier
                    .background(colors.functional.bgPrimary)
                    .padding(48.dp)
            ) {
                content()
            }
        }
    }
*/

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

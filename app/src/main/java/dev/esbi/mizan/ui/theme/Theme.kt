package dev.esbi.mizan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.esbi.mizan.ui.theme.colors.DarkPremiumDesignSystem
import dev.esbi.mizan.ui.theme.colors.LightPremiumDesignSystem
import dev.esbi.mizan.ui.theme.colors.LocalPremiumSystem

// =============================================================================
// 1. PREMIUM THEME (CSS dagi ranglarni M3 ga moslash)
// =============================================================================

/**
 * LIGHT MODE: Premium ranglar
 * CSS Mapping:
 * - primary -> --premium-primary
 * - background -> --premium-bg-primary (F8F9FA)
 * - surface -> --premium-bg-secondary (White)
 * - onSurface -> --premium-text-primary
 */
val PremiumLightColorScheme = lightColorScheme(
    primary = LightPremiumDesignSystem.colors.primary,           // #667eea
    onPrimary = Color.White,                               // Primary ustidagi yozuv
    primaryContainer = LightPremiumDesignSystem.colors.primaryLight,
    onPrimaryContainer = Color.White,

    secondary = LightPremiumDesignSystem.colors.secondary,       // #f5576c
    onSecondary = Color.White,
    secondaryContainer = LightPremiumDesignSystem.colors.secondaryLight,
    onSecondaryContainer = Color.White,

    tertiary = LightPremiumDesignSystem.text.tertiary,           // Qo'shimcha elementlar uchun
    onTertiary = Color.White,

    background = LightPremiumDesignSystem.background.primary,    // #f8f9fa
    onBackground = LightPremiumDesignSystem.text.primary,        // #1a1a2e

    surface = LightPremiumDesignSystem.background.secondary,     // #ffffff (Cardlar uchun)
    onSurface = LightPremiumDesignSystem.text.primary,           // #1a1a2e

    surfaceVariant = LightPremiumDesignSystem.background.tertiary, // #f1f3f5
    onSurfaceVariant = LightPremiumDesignSystem.text.secondary,

    error = LightPremiumDesignSystem.colors.error,
    onError = Color.White,

    outline = LightPremiumDesignSystem.glass.border // Chegaralar uchun
)

/**
 * DARK MODE: Premium ranglar
 * CSS Mapping:
 * - background -> --premium-bg-primary (0F0F23)
 * - surface -> --premium-bg-secondary (1A1A2E)
 */
val PremiumDarkColorScheme = darkColorScheme(
    primary = DarkPremiumDesignSystem.colors.primary,            // #667eea
    onPrimary = Color.White,
    primaryContainer = DarkPremiumDesignSystem.colors.primaryDark,
    onPrimaryContainer = Color.White,

    secondary = DarkPremiumDesignSystem.colors.secondary,        // #f5576c
    onSecondary = Color.White,
    secondaryContainer = DarkPremiumDesignSystem.colors.secondaryDark,
    onSecondaryContainer = Color.White,

    background = DarkPremiumDesignSystem.background.primary,     // #0f0f23 (Deep Blue)
    onBackground = DarkPremiumDesignSystem.text.primary,         // White

    surface = DarkPremiumDesignSystem.background.secondary,      // #1a1a2e (Lighter Blue for Cards)
    onSurface = DarkPremiumDesignSystem.text.primary,            // White

    surfaceVariant = DarkPremiumDesignSystem.background.tertiary, // #16213e
    onSurfaceVariant = DarkPremiumDesignSystem.text.secondary,

    error = DarkPremiumDesignSystem.colors.error,
    onError = Color.White,

    outline = DarkPremiumDesignSystem.glass.border
)

// =============================================================================
// 2. STANDARD MATERIAL 3 THEME (Fallback / Free users)
// Agar premium yoqilmasa, oddiyroq ranglar ishlatiladi.
// Hozircha Premium ranglarga yaqin, lekin soddaroq qilib qo'yildi.
// =============================================================================

val Material3LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4), // M3 Default Purple
    onPrimary = Color.White,
    secondary = Color(0xFF625B71),
    onSecondary = Color.White,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimaryContainer = Color(0xFF21005D),
    onSurface = Color(0xFF1C1B1F),
)

val Material3DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF), // M3 Default Light Purple
    onPrimary = Color(0xFF381E72),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
)

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

    val premiumSystem = if (darkTheme) DarkPremiumDesignSystem else LightPremiumDesignSystem
    CompositionLocalProvider(
        LocalPremiumSystem provides premiumSystem
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

package dev.esbi.mizan.design.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Mizan Typography System
 * Based on Material 3 typography scale
 * Derived from material3-theme.css and premium-theme.css
 *
 * Font Family: Inter (system default fallback)
 */

val Typography = Typography(
    // ==========================================
    // DISPLAY STYLES
    // ==========================================
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 46.sp,
        letterSpacing = (-0.02).em
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 46.sp,
        letterSpacing = (-0.02).em
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp,
        letterSpacing = (-0.01).em
    ),
    // ==========================================
    // HEADLINE STYLES
    // ==========================================
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),

    // ==========================================
    // TITLE STYLES
    // ==========================================
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    // ==========================================
    // BODY STYLES
    // ==========================================
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp
    ),
    // ==========================================
    // LABEL STYLES
    // ==========================================
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        letterSpacing = 0.01.em
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 21.sp,
        letterSpacing = 0.01.em
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.02.em
    )
)

package dev.esbi.mizan.design.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Complete Color Palette - Mizan Design System
 * Derived from premium-theme.css and material3-theme.css
 */

// ==========================================
// PREMIUM BRAND COLORS (Theme-Independent)
// ==========================================

val PremiumPrimary = Color(0xFF667EEA)
val PremiumPrimaryLight = Color(0xFF8B9CFF)
val PremiumPrimaryDark = Color(0xFF5568D3)

val PremiumSecondary = Color(0xFFF5576C)
val PremiumSecondaryLight = Color(0xFFFF7A8A)
val PremiumSecondaryDark = Color(0xFFD63F54)

val PremiumSuccess = Color(0xFF00F2FE)
val PremiumSuccessLight = Color(0xFF4FFBFF)
val PremiumSuccessDark = Color(0xFF00D4E0)

val PremiumWarning = Color(0xFFFEE140)
val PremiumWarningLight = Color(0xFFFFF176)
val PremiumWarningDark = Color(0xFFFBC02D)

val PremiumError = Color(0xFFFF6B6B)
val PremiumErrorLight = Color(0xFFFF8A8A)
val PremiumErrorDark = Color(0xFFEE5253)

// ==========================================
// LIGHT THEME COLORS
// ==========================================

val PremiumBgPrimaryLight = Color(0xFFF8F9FA)
val PremiumBgSecondaryLight = Color(0xFFFFFFFF)
val PremiumBgTertiaryLight = Color(0xFFF1F3F5)

val PremiumTextPrimaryLight = Color(0xFF1A1A2E)
val PremiumTextSecondaryLight = Color(0xFF4A5568)
val PremiumTextTertiaryLight = Color(0xFF718096)
val PremiumTextMutedLight = Color(0xFFA0AEC0)

val PremiumSurface1Light = Color(0xFFFAFAFA)
val PremiumSurface2Light = Color(0xFFF5F5F5)
val PremiumSurface3Light = Color(0xFFEEEEEE)
val PremiumSurface4Light = Color(0xFFE0E0E0)

val PremiumGlassBgLight = Color(0xFFFFFFFF).copy(alpha = 0.7f)
val PremiumGlassBorderLight = Color(0xFF000000).copy(alpha = 0.08f)

// ==========================================
// DARK THEME COLORS
// ==========================================

val PremiumBgPrimaryDark = Color(0xFF0F0F23)
val PremiumBgSecondaryDark = Color(0xFF1A1A2E)
val PremiumBgTertiaryDark = Color(0xFF16213E)

val PremiumTextPrimaryDark = Color(0xFFFFFFFF)
val PremiumTextSecondaryDark = Color(0xFFA8B2D1)
val PremiumTextTertiaryDark = Color(0xFF7E8BA3)
val PremiumTextMutedDark = Color(0xFF5A6478)

val PremiumSurface1Dark = Color(0xFF1C1C30)
val PremiumSurface2Dark = Color(0xFF232339)
val PremiumSurface3Dark = Color(0xFF2A2A42)
val PremiumSurface4Dark = Color(0xFF31314B)

val PremiumGlassBgDark = Color(0xFFFFFFFF).copy(alpha = 0.05f)
val PremiumGlassBorderDark = Color(0xFFFFFFFF).copy(alpha = 0.1f)

// ==========================================
// CATEGORY COLORS (Premium Palette)
// ==========================================

val CategoryFood = Color(0xFFFF6B9D)
val CategoryTransport = Color(0xFF4FACFE)
val CategoryShopping = Color(0xFFFFA34D)
val CategoryBills = Color(0xFF00D2FF)
val CategoryEntertainment = Color(0xFFC471F5)
val CategoryHealth = Color(0xFFFF6B6B)
val CategoryTravel = Color(0xFF667EEA)
val CategoryTech = Color(0xFF00F2A0)
val CategoryIncome = Color(0xFF00F2FE)

// ==========================================
// MATERIAL 3 COLORS - LIGHT THEME
// ==========================================
// Primary
val M3PrimaryLight = Color(0xFF6750A4)
val M3OnPrimaryLight = Color(0xFFFFFFFF)
val M3PrimaryContainerLight = Color(0xFFEADDFF)
val M3OnPrimaryContainerLight = Color(0xFF21005D)

// Secondary
val M3SecondaryLight = Color(0xFF625B71)
val M3OnSecondaryLight = Color(0xFFFFFFFF)
val M3SecondaryContainerLight = Color(0xFFE8DEF8)
val M3OnSecondaryContainerLight = Color(0xFF1D192B)

// Tertiary
val M3TertiaryLight = Color(0xFF7D5260)
val M3OnTertiaryLight = Color(0xFFFFFFFF)
val M3TertiaryContainerLight = Color(0xFFFFD8E4)
val M3OnTertiaryContainerLight = Color(0xFF31111D)

// Error
val M3ErrorLight = Color(0xFFB3261E)
val M3OnErrorLight = Color(0xFFFFFFFF)
val M3ErrorContainerLight = Color(0xFFF9DEDC)
val M3OnErrorContainerLight = Color(0xFF410E0B)

// Background
val M3BackgroundLight = Color(0xFFFFFBFE)
val M3OnBackgroundLight = Color(0xFF1C1B1F)

// Surface
val M3SurfaceLight = Color(0xFFFFFBFE)
val M3OnSurfaceLight = Color(0xFF1C1B1F)
val M3SurfaceVariantLight = Color(0xFFE7E0EC)
val M3OnSurfaceVariantLight = Color(0xFF49454F)

// Surface Containers
val M3SurfaceContainerLowestLight = Color(0xFFFFFFFF)
val M3SurfaceContainerLowLight = Color(0xFFF7F2FA)
val M3SurfaceContainerLight = Color(0xFFF3EDF7)
val M3SurfaceContainerHighLight = Color(0xFFECE6F0)
val M3SurfaceContainerHighestLight = Color(0xFFE6E0E9)

// Outline
val M3OutlineLight = Color(0xFF79747E)
val M3OutlineVariantLight = Color(0xFFCAC4D0)

// ==========================================
// MATERIAL 3 COLORS - DARK THEME
// ==========================================

// Primary
val M3PrimaryDark = Color(0xFFD0BCFF)
val M3OnPrimaryDark = Color(0xFF381E72)
val M3PrimaryContainerDark = Color(0xFF4F378B)
val M3OnPrimaryContainerDark = Color(0xFFEADDFF)

// Secondary
val M3SecondaryDark = Color(0xFFCCC2DC)
val M3OnSecondaryDark = Color(0xFF332D41)
val M3SecondaryContainerDark = Color(0xFF4A4458)
val M3OnSecondaryContainerDark = Color(0xFFE8DEF8)

// Tertiary
val M3TertiaryDark = Color(0xFFEFB8C8)
val M3OnTertiaryDark = Color(0xFF492532)
val M3TertiaryContainerDark = Color(0xFF633B48)
val M3OnTertiaryContainerDark = Color(0xFFFFD8E4)

// Error
val M3ErrorDark = Color(0xFFF2B8B5)
val M3OnErrorDark = Color(0xFF601410)
val M3ErrorContainerDark = Color(0xFF8C1D18)
val M3OnErrorContainerDark = Color(0xFFF9DEDC)

// Background
val M3BackgroundDark = Color(0xFF1C1B1F)
val M3OnBackgroundDark = Color(0xFFE6E1E5)

// Surface
val M3SurfaceDark = Color(0xFF1C1B1F)
val M3OnSurfaceDark = Color(0xFFE6E1E5)
val M3SurfaceVariantDark = Color(0xFF49454F)
val M3OnSurfaceVariantDark = Color(0xFFCAC4D0)

// Surface Containers
val M3SurfaceContainerLowestDark = Color(0xFF0F0D13)
val M3SurfaceContainerLowDark = Color(0xFF1D1B20)
val M3SurfaceContainerDark = Color(0xFF211F26)
val M3SurfaceContainerHighDark = Color(0xFF2B2930)
val M3SurfaceContainerHighestDark = Color(0xFF36343B)

// Outline
val M3OutlineDark = Color(0xFF938F99)
val M3OutlineVariantDark = Color(0xFF49454F)

// ==========================================
// PREMIUM GRADIENTS
// ==========================================

val GradientPrimary = Brush.linearGradient(
    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
)

val GradientSecondary = Brush.linearGradient(
    colors = listOf(Color(0xFFF093FB), Color(0xFFF5576C))
)

val GradientSuccess = Brush.linearGradient(
    colors = listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
)

val GradientWarm = Brush.linearGradient(
    colors = listOf(Color(0xFFFA709A), Color(0xFFFEE140))
)

val GradientCool = Brush.linearGradient(
    colors = listOf(Color(0xFF30CFD0), Color(0xFF330867))
)

val GradientAvatar = Brush.linearGradient(
    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
)

// ==========================================
// PREMIUM COLORS OBJECT
// ==========================================

object PremiumColors {
    // Brand Colors
    val Primary = PremiumPrimary
    val PrimaryLight = PremiumPrimaryLight
    val PrimaryDark = PremiumPrimaryDark

    val Secondary = PremiumSecondary
    val SecondaryLight = PremiumSecondaryLight
    val SecondaryDark = PremiumSecondaryDark

    val Success = PremiumSuccess
    val SuccessLight = PremiumSuccessLight
    val SuccessDark = PremiumSuccessDark

    val Warning = PremiumWarning
    val WarningLight = PremiumWarningLight
    val WarningDark = PremiumWarningDark

    val Error = PremiumError
    val ErrorLight = PremiumErrorLight
    val ErrorDark = PremiumErrorDark

    // Background (defaults to dark)
    val BgPrimary = PremiumBgPrimaryDark
    val BgSecondary = PremiumBgSecondaryDark
    val BgTertiary = PremiumBgTertiaryDark

    // Text (defaults to dark)
    val TextPrimary = PremiumTextPrimaryDark
    val TextSecondary = PremiumTextSecondaryDark
    val TextTertiary = PremiumTextTertiaryDark
    val TextMuted = PremiumTextMutedDark

    // Surface (defaults to dark)
    val Surface1 = PremiumSurface1Dark
    val Surface2 = PremiumSurface2Dark
    val Surface3 = PremiumSurface3Dark
    val Surface4 = PremiumSurface4Dark

    // Glass Effect
    val GlassBg = PremiumGlassBgDark
    val GlassBorder = PremiumGlassBorderDark
}


val DarkBg = Color(0xFF0D0D1A)
val CardBg = Color(0xFF1A1A2E)
val CardBorderColor = Color.White.copy(alpha = 0.08f)
val Purple = Color(0xFF667EEA)
val Purple2 = Color(0xFF764BA2)
val Pink = Color(0xFFF5576C)
val Cyan = Color(0xFF00F2FE)
val Orange = Color(0xFFFFA34D)
val Red = Color(0xFFFF6B6B)
val TextWhite = Color.White
val TextGray = Color(0xFFB8B8D1)
val TextMuted = Color(0xFF718096)

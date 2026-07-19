package dev.esbi.mizan.design.theme.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// ==========================================
// 1. DATA CLASSES (STRUKTURA)
// ==========================================

@Immutable
data class PremiumDesignSystem(
    val colors: PremiumColors,
    val gradients: PremiumGradients,
    val categories: PremiumCategoryColors,
    val text: PremiumTextColors,
    val background: PremiumBackgroundColors,
    val glass: PremiumGlassEffects,
    val shadows: PremiumShadows,
    val spacing: PremiumSpacing,
    val radius: PremiumRadius,
    val typography: PremiumTypography
)

@Immutable
data class PremiumColors(
    val primary: Color,
    val primaryLight: Color,
    val primaryDark: Color,
    val secondary: Color,
    val secondaryLight: Color,
    val secondaryDark: Color,
    val success: Color,
    val successLight: Color,
    val successDark: Color,
    val warning: Color,
    val warningLight: Color,
    val warningDark: Color,
    val error: Color,
    val errorLight: Color,
    val errorDark: Color,
    val surface1: Color,
    val surface2: Color,
    val surface3: Color,
    val surface4: Color,
    val emerald: Color,
    val white: Color,
    val black: Color,
)

@Immutable
data class PremiumGradients(
    val primary: Brush,
    val secondary: Brush,
    val success: Brush,
    val warm: Brush,
    val cool: Brush
)

@Immutable
data class PremiumCategoryColors(
    val food: Color,
    val transport: Color,
    val shopping: Color,
    val bills: Color,
    val entertainment: Color,
    val health: Color,
    val travel: Color,
    val tech: Color,
    val income: Color
)

@Immutable
data class PremiumTextColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val muted: Color
)

@Immutable
data class PremiumBackgroundColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color
)

@Immutable
data class PremiumGlassEffects(
    val bg: Color,
    val border: Color,
    val blur: Dp // Blur effektini Modifier orqali beramiz
)

@Immutable
data class PremiumShadows(
    val sm: ShadowInfo,
    val md: ShadowInfo,
    val lg: ShadowInfo,
    val xl: ShadowInfo,
    val glowPrimary: ShadowInfo,
    val glowSecondary: ShadowInfo,
    val glowSuccess: ShadowInfo
)

// Soya ma'lumotlarini saqlash uchun yordamchi class
data class ShadowInfo(
    val color: Color,
    val blurRadius: Dp,
    val spread: Dp = 0.dp,
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp
)

@Immutable
data class PremiumSpacing(
    val xs: Dp, val sm: Dp, val md: Dp, val lg: Dp,
    val xl: Dp, val xxl: Dp, val xxxl: Dp, val xxxxl: Dp
)

@Immutable
data class PremiumRadius(
    val xs: Dp, val sm: Dp, val md: Dp, val lg: Dp,
    val xl: Dp, val xxl: Dp, val full: Dp
)

@Immutable
data class PremiumTypography(
    val displayXl: TextStyle,
    val displayLg: TextStyle,
    val displayMd: TextStyle,
    val displaySm: TextStyle,
    val headingXl: TextStyle,
    val headingLg: TextStyle,
    val headingMd: TextStyle,
    val headingSm: TextStyle,
    val bodyLg: TextStyle,
    val bodyMd: TextStyle,
    val bodySm: TextStyle,
    val bodyXs: TextStyle,
    val labelLg: TextStyle,
    val labelMd: TextStyle,
    val labelSm: TextStyle
)

// ==========================================
// 2. HELPER FUNCTIONS
// ==========================================

// 135deg gradient simulyatsiyasi (TopStart -> BottomEnd)
private fun gradient135(color1: Long, color2: Long): Brush {
    return Brush.linearGradient(
        colors = listOf(Color(color1), Color(color2))
        // Default: Top-Left to Bottom-Right, bu taxminan 135deg ga to'g'ri keladi
    )
}

// ==========================================
// 3. VALUES (QIYMATLAR)
// ==========================================

// --- SHARED VALUES (Light/Dark bir xil) ---
private val SharedGradients = PremiumGradients(
    primary = gradient135(0xFF667EEA, 0xFF764BA2),
    secondary = gradient135(0xFFF093FB, 0xFFF5576C),
    success = gradient135(0xFF4FACFE, 0xFF00F2FE),
    warm = gradient135(0xFFFA709A, 0xFFFEE140),
    cool = gradient135(0xFF30CFD0, 0xFF330867)
)

private val SharedCategories = PremiumCategoryColors(
    food = Color(0xFFFF6B9D), transport = Color(0xFF4FACFE),
    shopping = Color(0xFFFFA34D), bills = Color(0xFF00D2FF),
    entertainment = Color(0xFFC471F5), health = Color(0xFFFF6B6B),
    travel = Color(0xFF667EEA), tech = Color(0xFF00F2A0),
    income = Color(0xFF00F2A0)
)

private val SharedSpacing = PremiumSpacing(
    xs = 4.dp, sm = 8.dp, md = 16.dp, lg = 24.dp,
    xl = 32.dp, xxl = 40.dp, xxxl = 48.dp, xxxxl = 64.dp
)

private val SharedRadius = PremiumRadius(
    xs = 8.dp, sm = 12.dp, md = 16.dp, lg = 20.dp,
    xl = 24.dp, xxl = 32.dp, full = 9999.dp
)

// Font Family: Default System Font ishlatildi (Inter o'rniga),
// "Inter" qo'shmoqchi bo'lsangiz FontFamily(Font(R.font.inter)) qilasiz.
private val SharedTypography = PremiumTypography(
    displayXl = TextStyle(
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 70.sp,
        letterSpacing = (-0.02).em
    ),
    displayLg = TextStyle(
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 56.sp,
        letterSpacing = (-0.02).em
    ),
    displayMd = TextStyle(
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 48.sp,
        letterSpacing = (-0.01).em
    ),
    displaySm = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
        letterSpacing = (-0.01).em
    ),

    headingXl = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold, lineHeight = 36.sp),
    headingLg = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold, lineHeight = 32.sp),
    headingMd = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, lineHeight = 28.sp),
    headingSm = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp),

    bodyLg = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp),
    bodyMd = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 21.sp),
    bodySm = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal, lineHeight = 18.sp),
    bodyXs = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Normal, lineHeight = 16.sp),

    labelLg = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        letterSpacing = 0.01.em
    ),
    labelMd = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 21.sp,
        letterSpacing = 0.01.em
    ),
    labelSm = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        letterSpacing = 0.02.em
    )
)

// --- LIGHT THEME ---
private val LightColors = PremiumColors(
    primary = Color(0xFF667EEA),
    primaryLight = Color(0xFF8B9CFF),
    primaryDark = Color(0xFF5568D3),
    secondary = Color(0xFFF5576C),
    secondaryLight = Color(0xFFFF7A8A),
    secondaryDark = Color(0xFFD63F54),
    success = Color(0xFF00F2FE),
    successLight = Color(0xFF4FFBFF),
    successDark = Color(0xFF00D4E0),
    warning = Color(0xFFFEE140),
    warningLight = Color(0xFFFFF176),
    warningDark = Color(0xFFFBC02D),
    error = Color(0xFFFF6B6B),
    errorLight = Color(0xFFFF8A8A),
    errorDark = Color(0xFFEE5253),
    surface1 = Color.Black.copy(alpha = 0.02f),
    surface2 = Color.Black.copy(alpha = 0.04f),
    surface3 = Color.Black.copy(alpha = 0.08f),
    surface4 = Color.Black.copy(alpha = 0.12f),
    emerald = Color(16, 185, 129),
    white = Color.White,
    black = Color.Black
)

val LightPremiumDesignSystem = PremiumDesignSystem(
    colors = LightColors,
    gradients = SharedGradients,
    categories = SharedCategories,
    text = PremiumTextColors(
        primary = Color(0xFF1A1A2E),
        secondary = Color(0xFF4A5568),
        tertiary = Color(0xFF718096),
        muted = Color(0xFFA0AEC0)
    ),
    background = PremiumBackgroundColors(
        primary = Color(0xFFF8F9FA),
        secondary = Color(0xFFFFFFFF),
        tertiary = Color(0xFFF1F3F5)
    ),
    glass = PremiumGlassEffects(
        bg = Color.White.copy(alpha = 0.7f),
        border = Color.Black.copy(alpha = 0.08f),
        blur = 20.dp
    ),
    shadows = PremiumShadows(
        sm = ShadowInfo(Color.Black.copy(0.06f), blurRadius = 8.dp, offsetY = 2.dp),
        md = ShadowInfo(Color.Black.copy(0.08f), blurRadius = 16.dp, offsetY = 4.dp),
        lg = ShadowInfo(Color.Black.copy(0.12f), blurRadius = 32.dp, offsetY = 8.dp),
        xl = ShadowInfo(Color.Black.copy(0.16f), blurRadius = 48.dp, offsetY = 12.dp),
        glowPrimary = ShadowInfo(Color(0xFF667EEA).copy(0.3f), blurRadius = 20.dp),
        glowSecondary = ShadowInfo(Color(0xFFF5576C).copy(0.3f), blurRadius = 20.dp),
        glowSuccess = ShadowInfo(Color(0xFF00F2FE).copy(0.3f), blurRadius = 20.dp)
    ),
    spacing = SharedSpacing,
    radius = SharedRadius,
    typography = SharedTypography
)

// --- DARK THEME ---
val DarkPremiumDesignSystem = LightPremiumDesignSystem.copy(
    background = PremiumBackgroundColors(
        primary = Color(0xFF0F0F23),
        secondary = Color(0xFF1A1A2E),
        tertiary = Color(0xFF16213E)
    ),
    text = PremiumTextColors(
        primary = Color(0xFFFFFFFF),
        secondary = Color(0xFFA8B2D1),
        tertiary = Color(0xFF7E8BA3),
        muted = Color(0xFF5A6478)
    ),
    colors = LightColors.copy(
        surface1 = Color.White.copy(alpha = 0.02f), surface2 = Color.White.copy(alpha = 0.05f),
        surface3 = Color.White.copy(alpha = 0.08f), surface4 = Color.White.copy(alpha = 0.12f)
    ),
    glass = PremiumGlassEffects(
        bg = Color.White.copy(alpha = 0.05f),
        border = Color.White.copy(alpha = 0.1f),
        blur = 20.dp
    ),
    shadows = LightPremiumDesignSystem.shadows.copy(
        sm = ShadowInfo(Color.Black.copy(0.15f), blurRadius = 8.dp, offsetY = 2.dp),
        md = ShadowInfo(Color.Black.copy(0.25f), blurRadius = 16.dp, offsetY = 4.dp),
        lg = ShadowInfo(Color.Black.copy(0.35f), blurRadius = 32.dp, offsetY = 8.dp),
        xl = ShadowInfo(Color.Black.copy(0.45f), blurRadius = 48.dp, offsetY = 12.dp),
        glowPrimary = ShadowInfo(Color(0xFF667EEA).copy(0.5f), blurRadius = 20.dp),
        glowSecondary = ShadowInfo(Color(0xFFF5576C).copy(0.5f), blurRadius = 20.dp),
        glowSuccess = ShadowInfo(Color(0xFF00F2FE).copy(0.5f), blurRadius = 20.dp)
    )
)

// ==========================================
// 4. THEME PROVIDER & ACCESSOR
// ==========================================

val LocalPremiumSystem = staticCompositionLocalOf { LightPremiumDesignSystem }

// Qulay foydalanish uchun Object
object MizanTheme {
    val premium: PremiumDesignSystem
        @Composable
        @ReadOnlyComposable
        get() = LocalPremiumSystem.current

    // Typography ga oson kirish
    val typography: PremiumTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalPremiumSystem.current.typography
}

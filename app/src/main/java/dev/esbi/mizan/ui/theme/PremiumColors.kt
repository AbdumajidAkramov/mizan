package dev.esbi.mizan.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Premium Color Palette - Object-based access
 * Extracted from design/src/styles/premium-theme.css
 * References existing Color.kt constants for consistency
 * Supports both light and dark themes with vibrant gradients
 */
object PremiumColors {
    
    // ==========================================
    // BRAND COLORS - Reference existing constants
    // ==========================================
    
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
    
    // ==========================================
    // BACKGROUND COLORS
    // ==========================================
    
    val BgPrimary = PremiumBgPrimaryLight
    val BgSecondary = PremiumBgSecondaryLight
    val BgTertiary = PremiumBgTertiaryLight
    
    val BgPrimaryDark = dev.esbi.mizan.ui.theme.PremiumBgPrimaryDark
    val BgSecondaryDark = dev.esbi.mizan.ui.theme.PremiumBgSecondaryDark
    val BgTertiaryDark = dev.esbi.mizan.ui.theme.PremiumBgTertiaryDark
    
    // ==========================================
    // TEXT COLORS
    // ==========================================
    
    val TextPrimary = PremiumTextPrimaryLight
    val TextSecondary = PremiumTextSecondaryLight
    val TextTertiary = PremiumTextTertiaryLight
    val TextMuted = PremiumTextMutedLight
    
    val TextPrimaryDark = dev.esbi.mizan.ui.theme.PremiumTextPrimaryDark
    val TextSecondaryDark = dev.esbi.mizan.ui.theme.PremiumTextSecondaryDark
    val TextTertiaryDark = dev.esbi.mizan.ui.theme.PremiumTextTertiaryDark
    val TextMutedDark = dev.esbi.mizan.ui.theme.PremiumTextMutedDark
    
    // ==========================================
    // SURFACE COLORS
    // ==========================================
    
    val Surface1 = PremiumSurface1Light
    val Surface2 = PremiumSurface2Light
    val Surface3 = PremiumSurface3Light
    val Surface4 = PremiumSurface4Light
    
    val Surface1Dark = dev.esbi.mizan.ui.theme.PremiumSurface1Dark
    val Surface2Dark = dev.esbi.mizan.ui.theme.PremiumSurface2Dark
    val Surface3Dark = dev.esbi.mizan.ui.theme.PremiumSurface3Dark
    val Surface4Dark = dev.esbi.mizan.ui.theme.PremiumSurface4Dark
    
    // ==========================================
    // CATEGORY COLORS
    // ==========================================
    
    val CategoryFood = dev.esbi.mizan.ui.theme.CategoryFood
    val CategoryTransport = dev.esbi.mizan.ui.theme.CategoryTransport
    val CategoryShopping = dev.esbi.mizan.ui.theme.CategoryShopping
    val CategoryBills = dev.esbi.mizan.ui.theme.CategoryBills
    val CategoryEntertainment = dev.esbi.mizan.ui.theme.CategoryEntertainment
    val CategoryHealth = dev.esbi.mizan.ui.theme.CategoryHealth
    val CategoryTravel = dev.esbi.mizan.ui.theme.CategoryTravel
    val CategoryTech = dev.esbi.mizan.ui.theme.CategoryTech
    val CategoryIncome = dev.esbi.mizan.ui.theme.CategoryIncome
    
    // ==========================================
    // GRADIENT COLORS - For Filter Chips
    // ==========================================
    
    // All Filter Gradient: #667eea → #764ba2
    val GradientAllStart = Color(0xFF667EEA)
    val GradientAllEnd = Color(0xFF764BA2)
    
    // Expense Filter Gradient: #f093fb → #f5576c
    val GradientExpenseStart = Color(0xFFF093FB)
    val GradientExpenseEnd = Color(0xFFF5576C)
    
    // Income Filter Gradient: #4facfe → #00f2fe
    val GradientIncomeStart = Color(0xFF4FACFE)
    val GradientIncomeEnd = Color(0xFF00F2FE)
    
    // Additional Gradients
    val GradientWarmStart = Color(0xFFFA709A)
    val GradientWarmEnd = Color(0xFFFEE140)
    
    val GradientCoolStart = Color(0xFF30CFD0)
    val GradientCoolEnd = Color(0xFF330867)
    
    // ==========================================
    // GLASS EFFECT COLORS
    // ==========================================
    
    val GlassBg = Color(0xFFFFFFFF).copy(alpha = 0.7f)
    val GlassBorder = Color(0xFF000000).copy(alpha = 0.08f)
    val GlassBgDark = Color(0xFFFFFFFF).copy(alpha = 0.05f)
    val GlassBorderDark = Color(0xFFFFFFFF).copy(alpha = 0.1f)
    
    // ==========================================
    // HELPER FUNCTIONS
    // ==========================================
    
    /**
     * Get category color by ID
     */
    fun getCategoryColor(categoryId: String): Color {
        return when (categoryId) {
            "food" -> CategoryFood
            "transport" -> CategoryTransport
            "shopping" -> CategoryShopping
            "bills" -> CategoryBills
            "entertainment" -> CategoryEntertainment
            "health" -> CategoryHealth
            "travel" -> CategoryTravel
            "tech" -> CategoryTech
            "income" -> CategoryIncome
            else -> Primary
        }
    }
}

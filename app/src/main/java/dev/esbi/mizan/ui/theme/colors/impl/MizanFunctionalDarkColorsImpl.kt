@file:Suppress("ALL")

package dev.esbi.mizan.ui.theme.colors.impl

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.ui.theme.colors.MizanFunctionalColors
import dev.esbi.mizan.ui.theme.colors.MizanPrimitiveColors

/**
 * It's not possible to use properties as constructor arguments.
 * Data class isn't used so that it is not possible to create a new instance through copying.
 *
 * workaround {@link https://youtrack.jetbrains.com/issue/KT-65756}
 */
@Immutable
internal class MizanFunctionalDarkColorsImpl(
    primitiveColors: MizanPrimitiveColors
) : MizanFunctionalColors {
    override val bgPrimary = Color(0xFF0F0F23)
    override val bgSecondary = Color(0xFF1A1A2E)
    override val bgTertiary = Color(0xFF16213E)
    override val glassBackground = primitiveColors.white100.copy(alpha = 0.8f)
    override val glassBorder = primitiveColors.white100.copy(alpha = 0.10f)
    override val textPrimary = primitiveColors.white100
    override val textSecondary = Color(0xFFA8B2D1)
    override val textTertiary = Color(0xFF7E8BA3)
    override val textMuted = Color(0xFF5A6478)
    override val surface1 = primitiveColors.white100.copy(alpha = 0.02f)
    override val surface2 = primitiveColors.white100.copy(alpha = 0.05f)
    override val surface3 = primitiveColors.white100.copy(alpha = 0.08f)
    override val surface4 = primitiveColors.white100.copy(alpha = 0.12f)
    override val shadowSM = primitiveColors.black100.copy(alpha = 0.15f)
    override val shadowMD = primitiveColors.black100.copy(alpha = 0.25f)
    override val shadowLG = primitiveColors.black100.copy(alpha = 0.35f)
    override val shadowXL = primitiveColors.black100.copy(alpha = 0.45f)
    override val glowPrimary = Color(0xFF667EEA).copy(alpha = 0.50f)
    override val glowSecondary = Color(0xFFF5576C).copy(alpha = 0.50f)
    override val glowSuccess = Color(0xFF00F2FE).copy(alpha = 0.50f)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MizanFunctionalDarkColorsImpl

        if (bgPrimary != other.bgPrimary) return false
        if (bgSecondary != other.bgSecondary) return false
        if (bgTertiary != other.bgTertiary) return false
        if (glassBackground != other.glassBackground) return false
        if (glassBorder != other.glassBorder) return false
        if (textPrimary != other.textPrimary) return false
        if (textSecondary != other.textSecondary) return false
        if (textTertiary != other.textTertiary) return false
        if (textMuted != other.textMuted) return false
        if (surface1 != other.surface1) return false
        if (surface2 != other.surface2) return false
        if (surface3 != other.surface3) return false
        if (surface4 != other.surface4) return false
        if (shadowSM != other.shadowSM) return false
        if (shadowMD != other.shadowMD) return false
        if (shadowLG != other.shadowLG) return false
        if (shadowXL != other.shadowXL) return false
        if (glowPrimary != other.glowPrimary) return false
        if (glowSecondary != other.glowSecondary) return false
        if (glowSuccess != other.glowSuccess) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bgPrimary.hashCode()
        result = 31 * result + bgSecondary.hashCode()
        result = 31 * result + bgTertiary.hashCode()
        result = 31 * result + glassBackground.hashCode()
        result = 31 * result + glassBorder.hashCode()
        result = 31 * result + textPrimary.hashCode()
        result = 31 * result + textSecondary.hashCode()
        result = 31 * result + textTertiary.hashCode()
        result = 31 * result + textMuted.hashCode()
        result = 31 * result + surface1.hashCode()
        result = 31 * result + surface2.hashCode()
        result = 31 * result + surface3.hashCode()
        result = 31 * result + surface4.hashCode()
        result = 31 * result + shadowSM.hashCode()
        result = 31 * result + shadowMD.hashCode()
        result = 31 * result + shadowLG.hashCode()
        result = 31 * result + shadowXL.hashCode()
        result = 31 * result + glowPrimary.hashCode()
        result = 31 * result + glowSecondary.hashCode()
        result = 31 * result + glowSuccess.hashCode()
        return result
    }

    override fun toString(): String {
        return "MizanFunctionalColorsDarkImpl(" +
                "bgPrimary=$bgPrimary, " +
                "bgSecondary=$bgSecondary, " +
                "bgTertiary=$bgTertiary, " +
                "glassBackground=$glassBackground, " +
                "glassBorder=$glassBorder, " +
                "textPrimary=$textPrimary, " +
                "textSecondary=$textSecondary, " +
                "textTertiary=$textTertiary, " +
                "textMuted=$textMuted, " +
                "surface1=$surface1, " +
                "surface2=$surface2, " +
                "surface3=$surface3, " +
                "surface4=$surface4, " +
                "shadowSM=$shadowSM, " +
                "shadowMD=$shadowMD, " +
                "shadowLG=$shadowLG, " +
                "shadowXL=$shadowXL, " +
                "glowPrimary=$glowPrimary, " +
                "glowSecondary=$glowSecondary, " +
                "glowSuccess=$glowSuccess, " +
                ")"
    }
}

@file:Suppress("ALL")

package dev.esbi.mizan.ui.theme.colors.impl

import androidx.compose.runtime.Immutable
import dev.esbi.mizan.ui.theme.colors.MizanFunctionalColors
import dev.esbi.mizan.ui.theme.colors.MizanPrimitiveColors
import dev.esbi.mizan.ui.theme.colors.MizanSemanticColors

/**
 * It's not possible to use properties as constructor arguments.
 * Data class isn't used so that it is not possible to create a new instance through copying.
 *
 * workaround {@link https://youtrack.jetbrains.com/issue/KT-65756}
 */
@Immutable
internal class MizanSemanticPrimaryColorsImpl(
    functionalColors: MizanFunctionalColors,
    primitiveColors: MizanPrimitiveColors,
) : MizanSemanticColors {
    override val systemBg = functionalColors.textPrimary
//    override val sectionHintText = functionalColors.textSecondary

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MizanSemanticPrimaryColorsImpl

        if (systemBg != other.systemBg) return false
//        if (sectionHintText != other.sectionHintText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = systemBg.hashCode()
//        result = 31 * result + sectionHintText.hashCode()
        return result
    }

    override fun toString(): String {
        return "MizanSemanticPrimaryColorsImpl(" +
                "systemBg=$systemBg, " +
//                "sectionHintText=$sectionHintText, " +
                ")"
    }
}

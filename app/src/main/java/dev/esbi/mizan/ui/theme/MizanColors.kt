package dev.esbi.mizan.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import dev.esbi.mizan.ui.theme.colors.MizanFunctionalColors
import dev.esbi.mizan.ui.theme.colors.MizanPrimitiveColors
import dev.esbi.mizan.ui.theme.colors.MizanSemanticColors
import dev.esbi.mizan.ui.theme.colors.impl.MizanFunctionalDarkColorsImpl
import dev.esbi.mizan.ui.theme.colors.impl.MizanFunctionalLightColorsImpl
import dev.esbi.mizan.ui.theme.colors.impl.MizanPrimitiveColorsImpl
import dev.esbi.mizan.ui.theme.colors.impl.MizanSemanticOverlayColorsImpl
import dev.esbi.mizan.ui.theme.colors.impl.MizanSemanticPrimaryColorsImpl

/*
val LocalMizanColors = staticCompositionLocalOf<MizanColors> {
    error("Colors not defined")
}

@Immutable
class MizanColors internal constructor(
    val functional: MizanFunctionalColors,
    val primitive: MizanPrimitiveColors,
    val semantic: MizanSemanticColors,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MizanColors

        if (functional != other.functional) return false
        if (primitive != other.primitive) return false
        if (semantic != other.semantic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = functional.hashCode()
        result = 31 * result + primitive.hashCode()
        result = 31 * result + semantic.hashCode()
        return result
    }

    override fun toString(): String {
        return "MizanColors(functional=$functional, primitive=$primitive, semantic=$semantic)"
    }
}

internal fun mizanColors(
    isDarkTheme: Boolean,
    isPremiumTheme: Boolean
): MizanColors {
    val primitive = MizanPrimitiveColorsImpl
    val functional = mizanFunctionalColors(primitive, isDarkTheme)
    val semantic = mizanSemanticColors(functional, primitive, isPremiumTheme)
    return MizanColors(functional, primitive, semantic)
}

private fun mizanFunctionalColors(
    primitiveColors: MizanPrimitiveColors,
    isDarkTheme: Boolean
): MizanFunctionalColors {
    return if (isDarkTheme) {
        MizanFunctionalDarkColorsImpl(primitiveColors)
    } else {
        MizanFunctionalLightColorsImpl(primitiveColors)
    }
}

private fun mizanSemanticColors(
    functionalColors: MizanFunctionalColors,
    primitiveColors: MizanPrimitiveColors,
    isPrimaryTheme: Boolean
): MizanSemanticColors {
    return if (isPrimaryTheme) {
        MizanSemanticPrimaryColorsImpl(functionalColors, primitiveColors)
    } else {
        MizanSemanticOverlayColorsImpl(functionalColors, primitiveColors)
    }
}
*/

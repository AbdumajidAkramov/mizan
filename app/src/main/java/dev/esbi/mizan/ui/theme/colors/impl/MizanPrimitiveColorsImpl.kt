@file:Suppress("ALL")

package dev.esbi.mizan.ui.theme.colors.impl

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.ui.theme.colors.MizanPrimitiveColors

/**
 * It's not possible to use properties as constructor arguments.
 * Data class isn't used so that it is not possible to create a new instance through copying.
 *
 * workaround {@link https://youtrack.jetbrains.com/issue/KT-65756}
 */
@Immutable
internal object MizanPrimitiveColorsImpl : MizanPrimitiveColors {
    override val white100 = Color(color = 0xFFFFFFFF)
    override val white90 = Color(color = 0xE6F7F7F7)
    override val white80 = Color(color = 0xCCF2F2F2)
    override val white70 = Color(color = 0xB3EBEBEB)
    override val white60 = Color(color = 0x99E4E4E4)
    override val white50 = Color(color = 0x80CCCCCC)
    override val white40 = Color(color = 0x66B4B4B4)
    override val white30 = Color(color = 0x4D7D7D7D)
    override val white20 = Color(color = 0x33525252)
    override val white10 = Color(color = 0x1A292929)
    override val white5 = Color(color = 0x0D141414)
    override val black100 = Color(color = 0xFF000000)
    override val black10 = Color(color = 0xFF1A1A1A)
    override val black20 = Color(color = 0xFF333333)
    override val black30 = Color(color = 0xFF4D4D4D)
    override val black40 = Color(color = 0xFF666666)
    override val black50 = Color(color = 0xFF808080)
    override val black60 = Color(color = 0xFF999999)
    override val black70 = Color(color = 0xFFB2B2B2)
    override val black80 = Color(color = 0xFFCCCCCC)
    override val black90 = Color(color = 0xFFE5E5E5)
    override val black5 = Color(color = 0x0D0D0D0D)
}

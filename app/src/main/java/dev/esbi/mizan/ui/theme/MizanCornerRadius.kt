package dev.esbi.mizan.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val LocalMizanCornerRadius = staticCompositionLocalOf<MizanCornerRadius> {
    error("Colors not defined")
}

@Immutable
class MizanCornerRadius internal constructor(
    val xs: CornerBasedShape,
    val s: CornerBasedShape,
    val m: CornerBasedShape,
    val l: CornerBasedShape,
    val xl: CornerBasedShape,
    val full: CornerBasedShape
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MizanCornerRadius

        if (xs != other.xs) return false
        if (s != other.s) return false
        if (m != other.m) return false
        if (l != other.l) return false
        if (xl != other.xl) return false
        if (full != other.full) return false

        return true
    }

    override fun hashCode(): Int {
        var result = xs.hashCode()
        result = 31 * result + s.hashCode()
        result = 31 * result + m.hashCode()
        result = 31 * result + l.hashCode()
        result = 31 * result + xl.hashCode()
        result = 31 * result + full.hashCode()
        return result
    }

    override fun toString(): String {
        return "MizanCornerRadius(xs=$xs, s=$s, m=$m, l=$l, xl=$xl, xxl=$full)"
    }
}

internal fun mizanCornerRadius() = MizanCornerRadius(
    xs = RoundedCornerShape(4.dp),
    s = RoundedCornerShape(8.dp),
    m = RoundedCornerShape(12.dp),
    l = RoundedCornerShape(16.dp),
    xl = RoundedCornerShape(28.dp),
    full = RoundedCornerShape(128.dp),
)

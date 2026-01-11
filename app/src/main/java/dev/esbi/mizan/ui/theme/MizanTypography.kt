package dev.esbi.mizan.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

val LocalMizanTypography = staticCompositionLocalOf<MizanTypography> {
    error("Typography not defined")
}

@Suppress("LongParameterList") // used instead of data class to restrict ambiguous actions
@Immutable
class MizanTypography internal constructor(

    val displayXL: TextStyle,
    val displayLG: TextStyle,
    val displayMD: TextStyle,
    val displaySM: TextStyle,
    val headingXL: TextStyle,
    val headingLG: TextStyle,
    val headingMD: TextStyle,
    val headingSM: TextStyle,
    val bodyLG: TextStyle,
    val bodyMD: TextStyle,
    val bodySM: TextStyle,
    val bodyXS: TextStyle,
    val labelLG: TextStyle,
    val labelMD: TextStyle,
    val labelSM: TextStyle,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MizanTypography

        if (displayXL != other.displayXL) return false
        if (displayLG != other.displayLG) return false
        if (displayMD != other.displayMD) return false
        if (displaySM != other.displaySM) return false
        if (headingXL != other.headingXL) return false
        if (headingLG != other.headingLG) return false
        if (headingMD != other.headingMD) return false
        if (headingSM != other.headingSM) return false
        if (bodyLG != other.bodyLG) return false
        if (bodyMD != other.bodyMD) return false
        if (bodySM != other.bodySM) return false
        if (bodyXS != other.bodyXS) return false
        if (labelLG != other.labelLG) return false
        if (labelMD != other.labelMD) return false
        if (labelSM != other.labelSM) return false

        return true
    }

    override fun hashCode(): Int {
        var result = displayXL.hashCode()
        result = 31 * result + displayLG.hashCode()
        result = 31 * result + displayMD.hashCode()
        result = 31 * result + displaySM.hashCode()
        result = 31 * result + headingXL.hashCode()
        result = 31 * result + headingLG.hashCode()
        result = 31 * result + headingMD.hashCode()
        result = 31 * result + headingSM.hashCode()
        result = 31 * result + bodyLG.hashCode()
        result = 31 * result + bodyMD.hashCode()
        result = 31 * result + bodySM.hashCode()
        result = 31 * result + bodyXS.hashCode()
        result = 31 * result + labelLG.hashCode()
        result = 31 * result + labelMD.hashCode()
        result = 31 * result + labelSM.hashCode()
        return result
    }

    override fun toString(): String {
        return "MizanTypography(" +
                "displayXL=$displayXL, " +
                "displayLG=$displayLG, " +
                "displayMD=$displayMD, " +
                "displaySM=$displaySM, " +
                "headingXL=$headingXL, " +
                "headingLG=$headingLG, " +
                "headingMD=$headingMD, " +
                "headingSM=$headingSM, " +
                "bodyLG=$bodyLG, " +
                "bodyMD=$bodyMD, " +
                "bodySM=$bodySM, " +
                "bodyXS=$bodyXS, " +
                "labelLG=$labelLG, " +
                "labelMD=$labelMD, " +
                "labelSM=$labelSM, " +
                ")"
    }
}

@Suppress("LongMethod") // factory function
@Composable
internal fun mizanTypography(
    color: Color
) = MizanTypography(
    displayXL = defaultTextStyle(
        color = color,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 52.sp,
        letterSpacing = (-0.02).em
    ),
    displayLG = defaultTextStyle(
        color = color,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 46.sp,
        letterSpacing = (-0.02).em
    ),
    displayMD = TextStyle(
        color = color,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp,
        letterSpacing = (-0.01).em
    ),
    displaySM = TextStyle(
        color = color,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 35.sp,
        letterSpacing = (-0.01).em
    ),
    headingXL = TextStyle(
        color = color,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 31.sp
    ),
    headingLG = TextStyle(
        color = color,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 30.sp
    ),
    headingMD = TextStyle(
        color = color,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 28.sp
    ),
    headingSM = TextStyle(
        color = color,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 26.sp
    ),
    bodyLG = TextStyle(
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    bodyMD = TextStyle(
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp
    ),
    bodySM = TextStyle(
        color = color,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    bodyXS = TextStyle(
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelLG = TextStyle(
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        letterSpacing = 0.01.em
    ), labelMD = TextStyle(
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 21.sp,
        letterSpacing = 0.01.em
    ),
    labelSM = TextStyle(
        color = color,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.02.em
    )

)

@Composable
private fun defaultTextStyle(
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    lineHeight: TextUnit,
    letterSpacing: TextUnit = TextUnit.Unspecified
) = TextStyle(
    color = color,
    fontFamily = robotoFlexFontFamily(fontWeight = fontWeight),
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing
)

@Composable
private fun robotoFlexFontFamily(
    fontWeight: FontWeight
): FontFamily {
    val robotoFlexFont = Font(
        path = "font/roboto-flex/roboto-flex.ttf",
        assetManager = LocalContext.current.assets,
        weight = fontWeight,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(weight = fontWeight, style = FontStyle.Normal)
    )
    return FontFamily(robotoFlexFont)
}

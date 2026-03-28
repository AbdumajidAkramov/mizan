package dev.esbi.mizan.ui.theme.shadows

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.colors.ShadowInfo

fun Modifier.premiumShadow(
    shadowInfo: ShadowInfo,
    borderRadius: Dp = 0.dp
) = this.drawBehind {
    val shadowColor = shadowInfo.color.toArgb()
    val transparentColor = shadowInfo.color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor

        frameworkPaint.setShadowLayer(
            shadowInfo.blurRadius.toPx(),
            shadowInfo.offsetX.toPx(),
            shadowInfo.offsetY.toPx(),
            shadowColor
        )

        it.drawRoundRect(
            0f, 0f, size.width, size.height,
            borderRadius.toPx(), borderRadius.toPx(),
            paint
        )
    }
}

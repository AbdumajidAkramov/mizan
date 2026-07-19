package dev.esbi.mizan.design.components.tooltip

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.colors.MizanTheme

// O'zingizning dizayn sistemangizni import qiling
// import dev.esbi.mizan.design.theme.MizanTheme

@Composable
fun PremiumTooltipContent(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    // Figma dizayn tizimidagi ranglar va o'lchamlar
    val backgroundColor = Color(0xFF1E1E1E).copy(alpha = 0.9f) // Glassmorphism effekti
    val borderColor = Color(0xFF333333)
    val accentColor = Color(0xFF667EEA) // Gradient boshlanishi
    Box(
        modifier = modifier
            .background(
                color = MizanTheme.premium.colors.surface3,
                shape = RoundedCornerShape(MizanTheme.premium.radius.sm)
            )
            .border(
                width = 1.dp,
                color = MizanTheme.premium.glass.border,
                shape = RoundedCornerShape(MizanTheme.premium.radius.sm)
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                color = MizanTheme.premium.text.secondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                color = MizanTheme.premium.text.tertiary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// Strelka uchun maxsus shakl
private val TooltipTriangleShape: Shape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width / 2f, size.height)
    close()
}

/**
 * Canvas ichida chiziladigan universal Tooltip Drawer
 */
fun DrawScope.drawChartTooltip(
    point: Offset,
    title: String,
    value: String,
    chartWidth: Float,
    chartHeight: Float,
    config: MizanTooltipConfig // Dizayn sozlamalari
) {
    val tooltipWidth = 80.dp.toPx()
    val tooltipHeight = 50.dp.toPx()

    // 1. Tooltip Pozitsiyasini hisoblash (Ekrandan chiqib ketmasligi uchun)
    var tooltipX = point.x + 20f
    if (tooltipX + tooltipWidth > chartWidth) {
        tooltipX = point.x - tooltipWidth - 20f
    }

    val tooltipY = if (point.y - tooltipHeight - 20f < 0) {
        point.y + 60f
    } else {
        point.y - tooltipHeight - 60f
    }

    // 2. Fonni chizish (Background)
    drawRoundRect(
        color = config.backgroundColor,
        topLeft = Offset(tooltipX, tooltipY),
        size = Size(tooltipWidth, tooltipHeight),
        cornerRadius = CornerRadius(config.cornerRadius)
    )

    // 3. Hoshiyani chizish (Border)
    drawRoundRect(
        color = config.borderColor,
        topLeft = Offset(tooltipX, tooltipY),
        size = Size(tooltipWidth, tooltipHeight),
        cornerRadius = CornerRadius(config.cornerRadius),
        style = Stroke(width = 1.dp.toPx())
    )

    // 4. Matnlarni chizish
    drawIntoCanvas { canvas ->
        val titlePaint = Paint().apply {
            color = config.titleColor.toArgb()
            textSize = 12.sp.toPx()
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
        }

        val valuePaint = Paint().apply {
            color = config.valueColor.toArgb()
            textSize = 12.sp.toPx()
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        canvas.nativeCanvas.drawText(
            title,
            tooltipX + 16.dp.toPx(),
            tooltipY + 24.dp.toPx(),
            titlePaint
        )
        canvas.nativeCanvas.drawText(
            value,
            tooltipX + 16.dp.toPx(),
            tooltipY + 44.dp.toPx(),
            valuePaint
        )
    }
}

// Sozlamalar uchun model
data class MizanTooltipConfig(
    val backgroundColor: Color,
    val borderColor: Color,
    val titleColor: Color,
    val valueColor: Color,
    val cornerRadius: Float
)

@Preview
@Composable
fun PremiumTooltipContentPreview() {
    MizanTheme() {
        PremiumTooltipContent(
            title = "Wri",
            value = "$700",
            modifier = Modifier
        )
    }
}

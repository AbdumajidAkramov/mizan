package dev.esbi.mizan.feature.dashboard.presentation.widgets.premium

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R.drawable.ic_arrow_right
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import kotlin.math.roundToInt

data class SpendingPoint(
    val dayLabel: String,
    val totalAmount: Float
)

@Composable
fun PremiumSpendingChart(
    data: List<SpendingPoint>,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit = {}
) {
    // Rasmga mos ranglar
    val chartLineColor = Color(0xFF667EEA) // Primary Blue
    val axisTextColor = Color(0xFF8F9BB3)  // Text Tertiary (Greyish)
    Column() {
        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth(), // Chartdan uzoqroq
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Spending Overview",
                style = MizanTheme.typography.headingMd.copy(fontSize = 18.sp),
                color = MizanTheme.premium.text.primary
            )

            Row(
                modifier = Modifier.clickable(onClick = onDetailsClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Details",
                    style = MizanTheme.typography.bodySm,
                    color = chartLineColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    icon = IconValue(ic_arrow_right),
                    contentDescription = null,
                    tint = chartLineColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )
        PremiumCard(
            variant = CardVariant.Glass, // Yoki Solid (Rasmda orqa fon qoramtir)
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // --- CHART ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp) // Rasmda balandroq ko'rinyapti
                ) {
                    AdvancedSpendingChart(
                        data = data,
                        lineColor = chartLineColor,
                        textColor = axisTextColor
                    )
                }
            }
        }

    }
}

@Composable
fun AdvancedSpendingChart(
    data: List<SpendingPoint>,
    lineColor: Color,
    textColor: Color
) {
    if (data.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = 12.sp,
        color = textColor
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // O'lchamlar va Paddinglar
        val leftPadding = 35.dp.toPx() // Y-Axis raqamlari uchun joy
        val bottomPadding = 24.dp.toPx() // X-Axis matnlari uchun joy

        val chartWidth = size.width - leftPadding
        val chartHeight = size.height - bottomPadding

        // 1. DATA CALCULATIONS (Max value topish va round qilish)
        val rawMax = data.maxOf { it.totalAmount }
        // Rasmda 260 eng tepasi. Biz ham 5 ga bo'linadigan chiroyli raqam qilamiz.
        // Masalan: Agar max 230 bo'lsa -> 260 ga yaxlitlash.
        val steps = 4 // 0, 1, 2, 3, 4 (5 ta chiziq)
        val stepValue =
            (rawMax / steps * 1.2f).roundToInt() / 10 * 10 + 10 // Taxminiy chiroyli scaling
        val maxY = (stepValue * steps).toFloat()

        // 2. DRAW Y-AXIS (Left Side Numbers)
        // Rasmda: 260, 195, 130, 65, 0
        for (i in 0..steps) {
            val value = (maxY / steps) * i
            val y = chartHeight - (chartHeight / steps * i) // Pastdan tepaga

            val textLayout = textMeasurer.measure(
                text = value.toInt().toString(),
                style = textStyle
            )

            // Raqamlarni o'ng tarafga (chartga yaqin) tekislash
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    x = leftPadding - textLayout.size.width - 8.dp.toPx(),
                    y = y - (textLayout.size.height / 2)
                )
            )
        }

        // 3. DRAW X-AXIS (Bottom Labels)
        val xStep = chartWidth / (data.size - 1)

        // Koordinatalarni saqlab turamiz
        val points = mutableListOf<Offset>()

        data.forEachIndexed { index, point ->
            val x = leftPadding + (index * xStep)
            val y = chartHeight - ((point.totalAmount / maxY) * chartHeight)
            points.add(Offset(x, y))

            // Text
            val textLayout = textMeasurer.measure(
                text = point.dayLabel,
                style = textStyle
            )
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    x = x - (textLayout.size.width / 2),
                    y = size.height - textLayout.size.height
                )
            )
        }

        // 4. DRAW SMOOTH CURVE (Bezier Path)
        val path = Path()
        path.moveTo(points.first().x, points.first().y)

        for (i in 0 until points.size - 1) {
            val p0 = points[i]
            val p1 = points[i + 1]

            // Control points calculation for smooth "S" curve
            // Bu formula chiziqni keskin sinishidan saqlaydi
            val controlPoint1 = Offset(p0.x + (p1.x - p0.x) / 2, p0.y)
            val controlPoint2 = Offset(p0.x + (p1.x - p0.x) / 2, p1.y)

            path.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                p1.x, p1.y
            )
        }

        // 5. DRAW GRADIENT FILL (Pastki qismi)
        val fillPath = Path()
        fillPath.addPath(path)
        fillPath.lineTo(points.last().x, chartHeight) // O'ng past
        fillPath.lineTo(points.first().x, chartHeight) // Chap past
        fillPath.close()

        val gradient = Brush.verticalGradient(
            colors = listOf(
                lineColor.copy(alpha = 0.3f), // Tepasi ko'rinadigan
                lineColor.copy(alpha = 0.0f)  // Pasti shaffof
            ),
            startY = 0f,
            endY = chartHeight
        )

        drawPath(
            path = fillPath,
            brush = gradient
        )

        // 6. DRAW STROKE (Asosiy chiziq)
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx()) // Qalinroq chiziq
        )
    }
}

// --- PREVIEW ---
@Preview(showBackground = true, backgroundColor = 0xFF181829) // Rasmga o'xshash to'q fon
@Composable
fun FixedChartPreview() {
    // Rasmga o'xshash ma'lumotlar
    val mockData = listOf(
        SpendingPoint(
            "Mon",
            185f
        ),
        SpendingPoint(
            "Tue",
            120f
        ),
        SpendingPoint(
            "Wed",
            120f
        ), // Flat qism
        SpendingPoint(
            "Thu",
            250f
        ), // Cho'qqi
        SpendingPoint(
            "Fri",
            20f
        ),  // Past
        SpendingPoint(
            "Sat",
            60f
        ),
        SpendingPoint(
            "Sun",
            170f
        )
    )

    Box(Modifier.padding(16.dp)) {
        PremiumSpendingChart(data = mockData)
    }
}
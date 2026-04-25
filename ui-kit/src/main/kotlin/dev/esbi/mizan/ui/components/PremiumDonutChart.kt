package dev.esbi.mizan.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

class PremiumDonutChartComponent(
    val data: List<Pie>,
) {
    val total: Double get() = data.sumOf { it.amount }
    val colors: List<Color> get() = data.map { it.color }
    val sw = 24.dp

    data class Pie(
        val amount: Double,
        val color: Color
    )
}

@Composable
fun PremiumDonutChart(
    component: PremiumDonutChartComponent,
    modifier: Modifier = Modifier,
    style: Stroke = Stroke(56f, cap = StrokeCap.Round)
) {
    val total = component.total
    val anim = remember { Animatable(0f) }
    LaunchedEffect(component) { anim.animateTo(1f, tween(1200, easing = FastOutSlowInEasing)) }
    Canvas(modifier) {
        val sw = component.sw.toPx()
        var start = -90f
        component.data.forEachIndexed { i, c ->
            val sweep = ((c.amount / total) * 360 * anim.value).toFloat()
            drawArc(
                color = c.color,
                startAngle = start,
                sweepAngle = sweep - 4,
                useCenter = false,
                topLeft = Offset(sw / 2, sw / 2),
                size = Size(size.width - sw, size.height - sw),
                style = style
            )
            start += sweep
        }
    }
}
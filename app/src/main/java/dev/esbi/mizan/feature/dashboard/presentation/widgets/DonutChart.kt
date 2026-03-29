package dev.esbi.mizan.feature.dashboard.presentation.widgets

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
import dev.esbi.mizan.presentation.feature.dashboard.domain.model.CategorySpending
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Orange
import dev.esbi.mizan.ui.theme.Purple

@Composable
fun DonutChart(cats: List<CategorySpending>, modifier: Modifier) {
    val total = cats.sumOf { it.totalAmount }
    val colors = listOf(Cyan, Orange, Color(0xFFFF6B9D), Purple)
    val anim = remember { Animatable(0f) }
    LaunchedEffect(cats) { anim.animateTo(1f, tween(1200, easing = FastOutSlowInEasing)) }
    Canvas(modifier) {
        val sw = 24.dp.toPx()
        var start = -90f
        cats.forEachIndexed { i, c ->
            val sweep = ((c.totalAmount / total) * 360 * anim.value).toFloat()
            drawArc(
                colors.getOrElse(i) { Purple },
                start,
                sweep - 4,
                false,
                Offset(sw / 2, sw / 2),
                Size(size.width - sw, size.height - sw),
                style = Stroke(sw, cap = StrokeCap.Round)
            )
            start += sweep
        }
    }
}

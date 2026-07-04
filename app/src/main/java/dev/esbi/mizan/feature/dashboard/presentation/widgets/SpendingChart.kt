package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.dashboard.WeeklySpendingPoint
import dev.esbi.mizan.design.theme.CardBorderColor
import dev.esbi.mizan.design.theme.Purple
import dev.esbi.mizan.design.theme.TextMuted

@Composable
fun SpendingChart(data: List<WeeklySpendingPoint>, modifier: Modifier) {
    val anim = remember { Animatable(0f) }
    LaunchedEffect(data) { anim.animateTo(1f, tween(1200, easing = FastOutSlowInEasing)) }
    val display = data.ifEmpty {
        listOf(
            WeeklySpendingPoint("Mon", 120.0, 1),
            WeeklySpendingPoint("Tue", 95.0, 2),
            WeeklySpendingPoint("Wed", 140.0, 3),
            WeeklySpendingPoint("Thu", 260.0, 4),
            WeeklySpendingPoint("Fri", 180.0, 5),
            WeeklySpendingPoint("Sat", 220.0, 6),
            WeeklySpendingPoint("Sun", 150.0, 7)
        )
    }
    Column(modifier) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val pad = 40.dp.toPx()
                val max = display.maxOfOrNull { it.totalAmount } ?: 1.0
                listOf(0, 65, 130, 195, 260).forEach { lbl ->
                    val y = size.height - (lbl / 260f * (size.height - pad))
                    drawLine(CardBorderColor, Offset(pad, y), Offset(size.width, y), 1.dp.toPx())
                }
                if (display.isNotEmpty()) {
                    val path = Path()
                    val cw = size.width - pad;
                    val ch = size.height - pad
                    display.forEachIndexed { i, pt ->
                        val x = pad + (cw * i / (display.size - 1))
                        val y = ch - ((pt.totalAmount / max) * ch * anim.value).toFloat()
                        if (i == 0) path.moveTo(x, y) else {
                            val px = pad + (cw * (i - 1) / (display.size - 1))
                            val py =
                                ch - ((display[i - 1].totalAmount / max) * ch * anim.value).toFloat()
                            path.cubicTo(px + (x - px) / 3, py, px + 2 * (x - px) / 3, y, x, y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = Purple,
                        style = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            display.forEach {
                Text(
                    it.dayLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
    }
}


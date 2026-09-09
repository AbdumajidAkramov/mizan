package dev.esbi.mizan.features.dashboard.presentation.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.Purple2

@Composable
fun Sparkline(modifier: Modifier) {
    val pts = listOf(40f, 45f, 42f, 48f, 44f, 50f, 47f, 52f, 55f, 53f, 58f, 60f)
    Canvas(modifier) {
        val path = Path()
        val max = pts.maxOrNull() ?: 1f;
        val min = pts.minOrNull() ?: 0f;
        val range = max - min
        pts.forEachIndexed { i, v ->
            val x = size.width * i / (pts.size - 1)
            val y = size.height - ((v - min) / range * size.height)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, Purple2, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))
    }
}

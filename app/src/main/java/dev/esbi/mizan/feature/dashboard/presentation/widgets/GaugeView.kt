package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Orange
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.Red
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.TextWhite
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun GaugeView(score: Int) {
    val anim = remember { Animatable(0f) }
    LaunchedEffect(score) {
        anim.animateTo(
            score.toFloat(),
            tween(1500, easing = FastOutSlowInEasing)
        )
    }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(180.dp), Alignment.Center) {
            Canvas(Modifier.size(180.dp)) {
                val sw = 16.dp.toPx()
                val colors = listOf(Red, Orange, Color(0xFFFFEB3B), Color(0xFF8BC34A), Cyan)
                drawArc(
                    Brush.sweepGradient(colors),
                    135f,
                    270f,
                    false,
                    Offset(sw / 2, sw / 2),
                    Size(size.width - sw, size.height - sw),
                    style = Stroke(sw, cap = StrokeCap.Round)
                )
                val angle = 135f + (270f * anim.value / 100f)
                val rad = Math.toRadians(angle.toDouble())
                val len = (size.minDimension - sw) / 2 - 20.dp.toPx()
                val cx = size.width / 2;
                val cy = size.height / 2
                drawCircle(Color.White, 8.dp.toPx(), Offset(cx, cy))
                drawLine(
                    Color.White,
                    Offset(cx, cy),
                    Offset(cx + (len * cos(rad)).toFloat(), cy + (len * sin(rad)).toFloat()),
                    4.dp.toPx(),
                    StrokeCap.Round
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                "${anim.value.toInt()}",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                "/100",
                fontSize = 20.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Box(
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Purple.copy(0.2f))
                .padding(16.dp, 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👍", fontSize = 14.sp); Spacer(Modifier.width(6.dp))
                Text("Good", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextWhite)
            }
        }
    }
}

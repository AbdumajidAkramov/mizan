package dev.esbi.mizan.features.dashboard.presentation.widgets

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.Cyan
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.Orange
import dev.esbi.mizan.design.theme.Purple
import dev.esbi.mizan.design.theme.Red
import dev.esbi.mizan.design.theme.TextMuted
import dev.esbi.mizan.design.theme.colors.MizanTheme
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(180.dp), Alignment.Center) {
            Canvas(Modifier.size(180.dp)) {
                val sw = 16.dp.toPx()
                val colors = listOf(Red, Orange, Color(0xFFFFEB3B), Color(0xFF8BC34A), Cyan)
                val arrowColor = when {
                    score <= 25 -> colors[0]
                    score <= 50 -> colors[1]
                    score <= 75 -> colors[2]
                    else -> colors[3]
                }
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
                drawCircle(arrowColor, 8.dp.toPx(), Offset(cx, cy))
                drawLine(
                    arrowColor,
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
                style = MizanTheme.typography.displayMd,
                color = MizanTheme.premium.text.primary
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
                Text(
                    "Good", fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(79, 172, 254)
                )
            }
        }
    }
}

private const val SCORE_FOR_PREVIEW = 88

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GaugeViewPreviewDark() {
    MizanTheme {
        GaugeView(score = SCORE_FOR_PREVIEW)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun GaugeViewPreviewLight() {
    MizanTheme() {
        GaugeView(score = SCORE_FOR_PREVIEW)
    }
}

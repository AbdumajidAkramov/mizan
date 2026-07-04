package dev.esbi.mizan.design.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.animation.animateRotationAsState
import dev.esbi.mizan.design.utils.Strings

@Composable
fun PremiumHealthScore(
    score: Int,
    trend: String,
    modifier: Modifier = Modifier
) {
    val animatedRotation by animateRotationAsState(
        targetRotation = (score / 100f) * 180f - 90f
    )

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Strings.financial_mirror_health_score),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = size.minDimension / 2 - 20.dp.toPx()

                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE0E0E0),
                                Color(0xFFF5F5F5)
                            )
                        ),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round),
                        topLeft = Offset(centerX - radius, centerY - radius),
                        size = Size(radius * 2, radius * 2)
                    )

                    val sweepAngle = (score / 100f) * 180f
                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF667EEA),
                                Color(0xFF764BA2)
                            )
                        ),
                        startAngle = 180f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round),
                        topLeft = Offset(centerX - radius, centerY - radius),
                        size = Size(radius * 2, radius * 2)
                    )

                    rotate(animatedRotation, pivot = Offset(centerX, centerY)) {
                        val needleLength = radius - 10.dp.toPx()
                        val needleEndX = centerX
                        val needleEndY = centerY - needleLength

                        drawLine(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF667EEA),
                                    Color(0xFF764BA2)
                                )
                            ),
                            start = Offset(centerX, centerY),
                            end = Offset(needleEndX, needleEndY),
                            strokeWidth = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF667EEA),
                                Color(0xFF764BA2)
                            )
                        ),
                        radius = 8.dp.toPx(),
                        center = Offset(centerX, centerY)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 60.dp)
                ) {
                    Text(
                        text = score.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(Strings.financial_mirror_out_of_100),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = trend,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF00F2FE)
            )
        }
    }
}

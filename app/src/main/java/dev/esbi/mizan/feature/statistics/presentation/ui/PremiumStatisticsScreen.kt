package dev.esbi.mizan.feature.statistics.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import androidx.core.graphics.toColorInt

data class ChartDataPoint(
    val label: String,
    val value: Float
)

data class CategoryData(
    val name: String,
    val amount: Float,
    val percentage: Float,
    val color: Color
)

enum class TimePeriod {
    WEEK, MONTH, YEAR
}

@Composable
fun PremiumStatisticsScreen() {
    var selectedPeriod by remember { mutableStateOf(TimePeriod.MONTH) }
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
    }

    val monthlyData = listOf(
        ChartDataPoint("Jan", 1450f),
        ChartDataPoint("Feb", 1820f),
        ChartDataPoint("Mar", 1650f),
        ChartDataPoint("Apr", 2100f),
        ChartDataPoint("May", 1880f),
        ChartDataPoint("Jun", 2350f)
    )

    val categoryData = listOf(
        CategoryData("Bills & Utilities", 205f, 30f, Color(0xFF00D2FF)),
        CategoryData("Shopping", 300f, 26f, Color(0xFFFFA34D)),
        CategoryData("Food & Dining", 186f, 20f, Color(0xFFFF6B9D)),
        CategoryData("Transport", 98f, 12f, Color(0xFF4FACFE)),
        CategoryData("Health & Fitness", 107f, 12f, Color(0xFFFF6B6B))
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D1A))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Statistics",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "January 2026",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }

        item {
            PeriodSelector(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ComparisonCard(
                    modifier = Modifier.weight(1f),
                    label = "Income",
                    amount = "$3,850",
                    change = "+12.5%",
                    isPositive = true,
                    gradientColors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE)),
                    icon = android.R.drawable.arrow_up_float
                )
                ComparisonCard(
                    modifier = Modifier.weight(1f),
                    label = "Expenses",
                    amount = "$1,750",
                    change = "-8.3%",
                    isPositive = true,
                    gradientColors = listOf(Color(0xFFFF6B6B), Color(0xFFF5576C)),
                    icon = android.R.drawable.arrow_down_float
                )
            }
        }

        item {
            TrendChartCard(
                data = monthlyData,
                animationProgress = animationProgress.value
            )
        }

        item {
            CategoryBreakdownCard(categoryData = categoryData)
        }

        item {
            TopCategoriesCard(categoryData = categoryData)
        }

        item {
            InsightsCard()
        }
    }
}

@Composable
private fun PeriodSelector(
    selectedPeriod: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val periods = listOf(TimePeriod.WEEK, TimePeriod.MONTH, TimePeriod.YEAR)
    val selectedIndex = periods.indexOf(selectedPeriod)
    
    val offsetX by animateFloatAsState(
        targetValue = selectedIndex * 120f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "period_offset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1A1A2E))
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX.dp)
                .width(120.dp)
                .height(48.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF2A2A3E))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            periods.forEach { period ->
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(48.dp)
                        .clickable { onPeriodSelected(period) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = period.name.lowercase().capitalize(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (period == selectedPeriod) Color.White else Color(0xFF6B6B7F)
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonCard(
    label: String,
    amount: String,
    change: String,
    isPositive: Boolean,
    gradientColors: List<Color>,
    icon: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color(0xFF8E8E9E)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = amount,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$change vs last month",
                fontSize = 11.sp,
                color = if (isPositive) Color(0xFF00F2A0) else Color(0xFFFF6B6B)
            )
        }
    }
}

@Composable
private fun TrendChartCard(
    data: List<ChartDataPoint>,
    animationProgress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A2E))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Monthly Trend",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Last 6 months spending pattern",
                fontSize = 12.sp,
                color = Color(0xFF6B6B7F)
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            AnimatedLineChart(
                data = data,
                animationProgress = animationProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
private fun AnimatedLineChart(
    data: List<ChartDataPoint>,
    animationProgress: Float,
    modifier: Modifier = Modifier
) {
    var selectedPoint by remember { mutableStateOf<Int?>(null) }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val chartWidth = width - 2 * padding
        val chartHeight = height - 2 * padding

        val maxValue = data.maxOfOrNull { it.value } ?: 1f
        val minValue = data.minOfOrNull { it.value } ?: 0f
        val valueRange = maxValue - minValue

        val stepX = chartWidth / (data.size - 1)

        val points = data.mapIndexed { index, point ->
            val x = padding + index * stepX
            val normalizedValue = (point.value - minValue) / valueRange
            val y = height - padding - (normalizedValue * chartHeight)
            Offset(x, y)
        }

        if (animationProgress > 0f) {
            val visiblePoints = (points.size * animationProgress).toInt().coerceAtLeast(2)
            val animatedPoints = points.take(visiblePoints)

            val gradientPath = Path().apply {
                if (animatedPoints.isNotEmpty()) {
                    moveTo(animatedPoints.first().x, height - padding)
                    lineTo(animatedPoints.first().x, animatedPoints.first().y)

                    for (i in 0 until animatedPoints.size - 1) {
                        val current = animatedPoints[i]
                        val next = animatedPoints[i + 1]
                        val controlX1 = current.x + (next.x - current.x) / 3
                        val controlY1 = current.y
                        val controlX2 = current.x + 2 * (next.x - current.x) / 3
                        val controlY2 = next.y
                        cubicTo(controlX1, controlY1, controlX2, controlY2, next.x, next.y)
                    }

                    lineTo(animatedPoints.last().x, height - padding)
                    close()
                }
            }

            drawPath(
                path = gradientPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667EEA).copy(alpha = 0.3f),
                        Color(0xFF764BA2).copy(alpha = 0.05f)
                    ),
                    startY = 0f,
                    endY = height
                )
            )

            val linePath = Path().apply {
                if (animatedPoints.isNotEmpty()) {
                    moveTo(animatedPoints.first().x, animatedPoints.first().y)
                    for (i in 0 until animatedPoints.size - 1) {
                        val current = animatedPoints[i]
                        val next = animatedPoints[i + 1]
                        val controlX1 = current.x + (next.x - current.x) / 3
                        val controlY1 = current.y
                        val controlX2 = current.x + 2 * (next.x - current.x) / 3
                        val controlY2 = next.y
                        cubicTo(controlX1, controlY1, controlX2, controlY2, next.x, next.y)
                    }
                }
            }

            drawPath(
                path = linePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                ),
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            animatedPoints.forEachIndexed { index, point ->
                drawCircle(
                    color = Color(0xFF667EEA),
                    radius = 6f,
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = 3f,
                    center = point
                )
            }
        }

        data.forEachIndexed { index, point ->
            val x = padding + index * stepX
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    point.label,
                    x,
                    height - 10f,
                    android.graphics.Paint().apply {
                        color = "#6B6B7F".toColorInt()
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryBreakdownCard(
    categoryData: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A2E))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Category Breakdown",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Spending by category",
                fontSize = 12.sp,
                color = Color(0xFF6B6B7F)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                DonutChart(
                    data = categoryData,
                    modifier = Modifier.size(140.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categoryData.forEach { category ->
                        CategoryItem(category = category)
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    data: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2 * 0.8f
        val innerRadius = radius * 0.6f

        var startAngle = -90f
        data.forEach { category ->
            val sweepAngle = (category.percentage / 100f) * 360f
            
            drawArc(
                color = category.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(centerX - radius, centerY - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = radius - innerRadius)
            )
            
            startAngle += sweepAngle
        }
    }
}

@Composable
private fun CategoryItem(
    category: CategoryData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(category.color)
            )
            Text(
                text = category.name,
                fontSize = 13.sp,
                color = Color(0xFFB0B0C0)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${category.amount.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = "${category.percentage.toInt()}%",
                fontSize = 11.sp,
                color = Color(0xFF6B6B7F)
            )
        }
    }
}

@Composable
private fun TopCategoriesCard(
    categoryData: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A2E))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Top Categories",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Highest spending categories",
                fontSize = 12.sp,
                color = Color(0xFF6B6B7F)
            )
            Spacer(modifier = Modifier.height(24.dp))

            BarChart(
                data = categoryData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
private fun BarChart(
    data: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val chartHeight = height - 2 * padding
        val barWidth = (width - 2 * padding) / (data.size * 2)
        val spacing = barWidth

        val maxValue = data.maxOfOrNull { it.amount } ?: 1f

        data.forEachIndexed { index, category ->
            val barHeight = (category.amount / maxValue) * chartHeight
            val x = padding + index * (barWidth + spacing)
            val y = height - padding - barHeight

            drawRoundRect(
                color = category.color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    category.name.split(" ").first(),
                    x + barWidth / 2,
                    height - 10f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.parseColor("#6B6B7F")
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

@Composable
private fun InsightsCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A2E))
            .padding(20.dp)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💡",
                    fontSize = 20.sp
                )
                Text(
                    text = "Insights",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            InsightItem(
                title = "Your spending decreased by 8.3% this month",
                description = "Great job managing your expenses!",
                barColor = Brush.verticalGradient(
                    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            InsightItem(
                title = "Bills category is 30% of total spending",
                description = "Consider reviewing your subscriptions",
                barColor = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF5576C), Color(0xFFFFA34D))
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            InsightItem(
                title = "You saved $1,245 this month",
                description = "You're on track to meet your savings goal",
                barColor = Brush.verticalGradient(
                    colors = listOf(Color(0xFF00F2FE), Color(0xFF4FACFE))
                )
            )
        }
    }
}

@Composable
private fun InsightItem(
    title: String,
    description: String,
    barColor: Brush,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(barColor)
        )
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color(0xFF6B6B7F)
            )
        }
    }
}

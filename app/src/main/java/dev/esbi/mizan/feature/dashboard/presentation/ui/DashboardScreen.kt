package dev.esbi.mizan.feature.dashboard.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.feature.dashboard.domain.model.CategorySpending
import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import dev.esbi.mizan.feature.dashboard.domain.model.Transaction
import dev.esbi.mizan.feature.dashboard.domain.model.TransactionType
import dev.esbi.mizan.feature.dashboard.domain.model.WeeklySpendingPoint
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModel
import dev.esbi.mizan.feature.dashboard.presentation.DashboardViewModelFactory
import dev.esbi.mizan.feature.dashboard.presentation.store.DashboardStore
import dev.esbi.mizan.ui.components.ErrorState
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

private val DarkBg = Color(0xFF0D0D1A)
private val CardBg = Color(0xFF1A1A2E)
private val CardBorderColor = Color.White.copy(alpha = 0.08f)
private val Purple = Color(0xFF667EEA)
private val Purple2 = Color(0xFF764BA2)
private val Pink = Color(0xFFF5576C)
private val Cyan = Color(0xFF00F2FE)
private val Orange = Color(0xFFFFA34D)
private val Red = Color(0xFFFF6B6B)
private val TextWhite = Color.White
private val TextGray = Color(0xFFB8B8D1)
private val TextMuted = Color(0xFF718096)
private const val SLIDE_UP_DELAY_MS = 20

@Composable
fun DashboardScreen(
    viewModelFactory: DashboardViewModelFactory,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is DashboardStore.Label.NavigateToCategory -> onNavigateToCategory(label.categoryId)
                is DashboardStore.Label.ShowError -> {}
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        when {
            state.isLoading && state.dashboardData == null -> LoadingContent()
            state.error != null && state.dashboardData == null -> ErrorState(
                message = state.error!!,
                onRetry = { viewModel.onIntent(DashboardStore.Intent.Retry) },
                modifier = Modifier.fillMaxSize()
            )

            state.dashboardData != null -> DashboardScrollContent(
                data = state.dashboardData!!,
                onCategoryClick = { viewModel.onIntent(DashboardStore.Intent.CategoryClicked(it)) }
            )
        }
    }
}

@Composable
private fun DashboardScrollContent(data: DashboardSummary, onCategoryClick: (String) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { AnimSection(visible, 0) { HeaderSection() } }
        item {
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                BalanceCard(
                    data.totalBalance,
                    data.totalBalance - data.monthlyExpenses + data.monthlySavings,
                    data.monthlyExpenses
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { HealthCard(78, 5) } }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { NetWorthCard(22450.0, 1245.0, 5.9) } }
        item {
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                CashFlowCard(
                    3850.0,
                    data.monthlyExpenses
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { EmergencyCard(8500.0, 12000.0, 6) } }
        item {
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                StatsRow(
                    data.budgetPercentageUsed,
                    data.monthlyExpenses,
                    data.budgetLimit,
                    data.monthlySavings
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { ChartCard(data.weeklySpending) } }
        item {
            AnimSection(visible, SLIDE_UP_DELAY_MS) {
                CategoriesSection(
                    data.topCategories,
                    onCategoryClick
                )
            }
        }
        item { AnimSection(visible, SLIDE_UP_DELAY_MS) { InsightsSection() } }
        item {
            AnimSection(
                visible,
                SLIDE_UP_DELAY_MS
            ) { TransactionsSection(data.recentTransactions) }
        }
    }
}

@Composable
private fun AnimSection(visible: Boolean, delayMs: Int, content: @Composable () -> Unit) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (visible) {
            delay(delayMs.toLong()); show = true
        }
    }
    AnimatedVisibility(
        show,
        enter = fadeIn(tween(100)) + slideInVertically(tween(100)) { it / 4 }) { content() }
}

@Composable
private fun PressCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, tween(150), label = "scale")
    Box(
        modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                    onTap = { onClick?.invoke() })
            }) { content() }
}

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    PressCard(modifier, onClick) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(CardBg.copy(0.95f))
                .border(1.dp, CardBorderColor, RoundedCornerShape(24.dp))
        ) { content() }
    }
}

@Composable
private fun HeaderSection() {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Column {
            Text("Welcome back,", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("John Doe", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(" 👋", fontSize = 28.sp)
            }
        }
        Box(
            Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Purple, Purple2))), Alignment.Center
        ) {
            Text("JD", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
private fun BalanceCard(total: Double, income: Double, expenses: Double) {
    var show by remember { mutableStateOf(true) }
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    PressCard {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.linearGradient(listOf(Purple, Purple2, Pink)))
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color.White.copy(0.15f), Color.Transparent),
                            Offset(100f, 100f),
                            400f
                        )
                    )
            )
            Column(Modifier.padding(24.dp)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(0.2f)), Alignment.Center
                        ) {
                            Icon(
                                icon = IconValue(Icons.ic_wallet),
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Total Balance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(0.9f)
                        )
                    }
                    IconButton({ show = !show }) {
                        Icon(

                            icon = IconValue(if (show) Icons.ic_visibility else Icons.ic_visibility_off),
                            modifier = Modifier.size(20.dp),
                            tint = Color.White.copy(0.8f)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    if (show) fmt.format(total) else "••••••",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(24.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                    SubCard("Income", income, true, Modifier.weight(1f))
                    SubCard("Expenses", expenses, false, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun SubCard(label: String, amount: Double, isIncome: Boolean, modifier: Modifier) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(0.15f))
            .border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isIncome) Color(0xFF047750) else Red), Alignment.Center
            ) {
                Icon(
                    icon = IconValue(if (isIncome) Icons.ic_trend_up else Icons.ic_down_trend),
                    modifier = Modifier.size(14.dp),
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.8f))
        }
        Spacer(Modifier.height(8.dp))
        Text(
            fmt.format(amount),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun HealthCard(score: Int, trend: Int) {
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon = IconValue(Icons.ic_shield),
                        modifier = Modifier.size(24.dp),
                        tint = Purple
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Financial Health",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cyan.copy(0.2f))
                        .padding(8.dp, 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            icon = IconValue(Icons.ic_trend_up),
                            modifier = Modifier.size(14.dp),
                            tint = Cyan
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "$trend%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Cyan
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            GaugeView(score)
            Spacer(Modifier.height(16.dp))
            Text(
                "Based on spending habits, savings rate, and budget adherence",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GaugeView(score: Int) {
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

@Composable
private fun NetWorthCard(worth: Double, change: Double, pct: Double) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Purple2), Alignment.Center
                ) {
                    Text("$", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "Total Net Worth",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                fmt.format(worth),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Cyan.copy(0.2f))
                        .padding(8.dp, 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            icon = IconValue(Icons.ic_trend_up),
                            modifier = Modifier.size(14.dp),
                            tint = Cyan
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "+${fmt.format(change).replace("$", "")}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Cyan
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "(+$pct%) this month",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            Spacer(Modifier.height(16.dp))
            Sparkline(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}

@Composable
private fun Sparkline(modifier: Modifier) {
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

@Composable
private fun CashFlowCard(income: Double, expenses: Double) {
    val flow = income - expenses
    val fmt = NumberFormat.getNumberInstance(Locale.US)
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text("Monthly Cash Flow", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Cyan)
                    .padding(16.dp, 8.dp)
            ) {
                Text(
                    "+${fmt.format(flow.toInt())}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003333)
                )
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(24.dp)) {
                FlowItem(true, income)
                FlowItem(false, expenses)
            }
            Spacer(Modifier.height(16.dp))
            MiniBarChart()
        }
    }
}

@Composable
private fun FlowItem(isIncome: Boolean, amount: Double) {
    val fmt = NumberFormat.getNumberInstance(Locale.US)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isIncome) Purple.copy(0.2f) else Red.copy(0.2f)), Alignment.Center
        ) {
            Icon(
                icon = IconValue(if (isIncome) Icons.ic_down_trend else Icons.ic_trend_up),
                modifier = Modifier.size(16.dp),
                tint = if (isIncome) Purple else Red
            )
        }
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                if (isIncome) "Income" else "Expenses",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
            Text(
                "$${fmt.format(amount.toInt())}",
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
        }
    }
}

@Preview
@Composable
private fun MiniBarChartPreview() {
    MizanTheme {
        MiniBarChart()
    }
}

@Composable
private fun MiniBarChart() {
    val data = listOf(
        0.3f to 0.5f,
        0.4f to 0.6f,
        0.7f to 0.3f,
        0.8f to 0.4f,
        0.9f to 0.4f,
    )
    Row(
        modifier = Modifier.fillMaxWidth(), Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { (inc, exp) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    Modifier
                        .width(20.dp)
                        .height((inc * 60).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Cyan)
                )
                Box(
                    Modifier
                        .width(20.dp)
                        .height((exp * 60).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Red)
                )
            }
        }
    }
}


@Preview
@Composable
private fun EmergencyCardPreview() {
    MizanTheme {
        EmergencyCard(
            current = 8500.0,
            goal = 12000.0,
            months = 6
        )
    }
}


@Composable
private fun EmergencyCard(current: Double, goal: Double, months: Int) {
    val prog = (current / goal).coerceIn(0.0, 1.0)
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    val rem = goal - current;
    val monthly = rem / 12
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon = IconValue(Icons.ic_shield),
                        modifier = Modifier.size(24.dp),
                        tint = Purple
                    )

                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Emergency Fund",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, TextMuted.copy(0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp, 6.dp)
                ) {
                    Text("$months months", fontSize = 12.sp, color = TextGray)
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column {
                    Text(
                        fmt.format(current),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        "of ${fmt.format(goal)} goal",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "${(prog * 100).toInt()}%",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cyan
                    )
                    Text("complete", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(CardBorderColor)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(prog.toFloat())
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(Purple, Cyan)))
                )
            }
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBg)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        icon = IconValue(Icons.ic_trend_up),
                        modifier = Modifier.size(20.dp),
                        tint = Purple
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "${fmt.format(rem)} remaining",
                            fontWeight = FontWeight.Medium,
                            color = TextWhite
                        )
                        Text(
                            "Save ~${fmt.format(monthly)}/month to reach goal in 1 year",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsRow(budget: Double, spent: Double, limit: Double, savings: Double) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
        GlassCard(Modifier.weight(1f)) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Orange, Red))), Alignment.Center
                    ) {
                        Icon(
                            icon = IconValue(Icons.ic_trend_up),
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Budget Used",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "${budget.toInt()}%",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    "${fmt.format(spent)} of ${fmt.format(limit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(CardBg)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth((budget / 100).toFloat().coerceIn(0f, 1f))
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Brush.horizontalGradient(listOf(Orange, Red)))
                    )
                }
            }
        }
        GlassCard(Modifier.weight(1f)) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Cyan), Alignment.Center
                    ) {
                        Icon(
                            icon = IconValue(Icons.ic_calendar_month),
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF003333)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("This Month", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    fmt.format(savings),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    "+12.5% from last month",
                    style = MaterialTheme.typography.bodySmall,
                    color = Cyan
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChartCardPreview() {
    MizanTheme {
        ChartCard(data = emptyList())
    }
}

@Composable
private fun ChartCard(data: List<WeeklySpendingPoint>) {
    GlassCard {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(
                    "Spending Overview",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Details", style = MaterialTheme.typography.bodySmall, color = Purple)
                    Spacer(Modifier.width(4.dp))
                    Text("→", color = Purple)
                }
            }
            Spacer(Modifier.height(24.dp))
            SpendingChart(
                data, Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}

@Composable
private fun SpendingChart(data: List<WeeklySpendingPoint>, modifier: Modifier) {
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

@Composable
private fun CategoriesSection(cats: List<CategorySpending>, onClick: (String) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(
                "Top Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Text("See all", style = MaterialTheme.typography.bodySmall, color = Purple)
        }
        Spacer(Modifier.height(16.dp))
        GlassCard {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DonutChart(cats.take(4), Modifier.size(140.dp))
                Spacer(Modifier.width(24.dp))
                Column(Modifier.weight(1f), Arrangement.spacedBy(12.dp)) {
                    cats.take(4).forEach { CatItem(it) { onClick(it.category) } }
                }
            }
        }
    }
}

@Composable
private fun DonutChart(cats: List<CategorySpending>, modifier: Modifier) {
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

@Composable
private fun CatItem(cat: CategorySpending, onClick: () -> Unit) {
    val colors = mapOf(
        "bills" to Cyan,
        "shopping" to Orange,
        "food" to Color(0xFFFF6B9D),
        "transport" to Purple
    )
    val col = colors[cat.category.lowercase()] ?: Purple
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(col)
            )
            Spacer(Modifier.width(8.dp))
            Text(cat.categoryLabel, style = MaterialTheme.typography.bodyMedium, color = TextGray)
        }
        Text(
            fmt.format(cat.totalAmount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = TextWhite
        )
    }
}

@Composable
private fun InsightsSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨", fontSize = 20.sp); Spacer(Modifier.width(8.dp))
            Text(
                "AI Insights",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Purple2)
                    .padding(8.dp, 4.dp)
            ) {
                Text("Smart", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.White)
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            Arrangement.spacedBy(12.dp)
        ) {
            InsightItem(
                "📈",
                "Great savings momentum!",
                "You saved 15% more than last month. Keep it up!",
                "+\$245",
                Cyan
            )
            InsightItem(
                "⚠️",
                "Subscription alert",
                "You have 3 unused subscriptions totaling \$47/mo.",
                null,
                Orange
            )
        }
    }
}

@Composable
private fun InsightItem(icon: String, title: String, sub: String, hl: String?, hlCol: Color) {
    GlassCard(Modifier.width(280.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(icon, fontSize = 24.sp)
                if (hl != null) Text(
                    hl,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = hlCol
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Spacer(Modifier.height(4.dp))
            Text(sub, style = MaterialTheme.typography.bodySmall, color = TextMuted, maxLines = 2)
        }
    }
}

@Composable
private fun TransactionsSection(txns: List<Transaction>) {
    Column {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(
                "Recent Transactions",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Text("See all", style = MaterialTheme.typography.bodySmall, color = Purple)
        }
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            txns.take(5).forEach { TxnItem(it) }
        }
    }
}

@Composable
private fun TxnItem(txn: Transaction) {
    val fmt = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFmt = SimpleDateFormat("hh:mm a • MMM d", Locale.US)
    val isInc = txn.type == TransactionType.INCOME
    val catColors = mapOf(
        "food" to Color(0xFFFFD93D),
        "transport" to Purple,
        "shopping" to Orange,
        "income" to Cyan,
        "entertainment" to Pink,
        "bills" to Cyan
    )
    val col = catColors[txn.category.lowercase()] ?: Purple
    val emoji = when (txn.category.lowercase()) {
        "food" -> "🍽️"; "transport" -> "🚗"; "shopping" -> "🛍️"; "income" -> "💰"; "entertainment" -> "🎬"; "bills" -> "📄"; else -> "💳"
    }
    GlassCard {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(col.copy(0.2f)),
                Alignment.Center
            ) { Text(emoji, fontSize = 20.sp) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        txn.description,
                        fontWeight = FontWeight.Medium,
                        color = TextWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, false)
                    )
                    if (!isInc) {
                        Spacer(Modifier.width(4.dp)); Text("↗", fontSize = 12.sp, color = Red)
                    }
                }
                Text(
                    dateFmt.format(txn.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    if (isInc) "+${fmt.format(txn.amount)}" else "-${fmt.format(txn.amount)}",
                    fontWeight = FontWeight.SemiBold,
                    color = if (isInc) Cyan else Red
                )
                Text(
                    txn.categoryLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp), Arrangement.spacedBy(24.dp)
    ) {
        LoadingSkeleton(height = 200); LoadingSkeleton(height = 160); LoadingSkeleton(height = 300)
    }
}

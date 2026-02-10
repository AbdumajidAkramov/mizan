package dev.esbi.mizan.feature.goals.presentation.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.goals.domain.model.Goal
import dev.esbi.mizan.feature.goals.presentation.GoalsViewModel
import dev.esbi.mizan.feature.goals.presentation.store.GoalsStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun FinancialGoalsScreen(
    viewModel: GoalsViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState(initial = GoalsStore.State())

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            contentPadding = PaddingValues(
                start = MizanTheme.premium.spacing.lg,
                end = MizanTheme.premium.spacing.lg,
                top = MizanTheme.premium.spacing.xl,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xl)
        ) {
            // Header
            item { GoalsHeader(onBack = onBack) }

            // Overall Progress Card
            item {
                OverallProgressCard(
                    totalSaved = state.totalSaved,
                    totalTarget = state.totalTarget,
                    overallProgress = state.overallProgress,
                    goalCount = state.goals.size
                )
            }

            // Active Goals Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Active Goals",
                        style = MizanTheme.premium.typography.headingSm,
                        color = MizanTheme.premium.text.primary.copy(alpha = 0.9f)
                    )
                    Text(
                        "${state.goals.count { !it.isCompleted }} in progress",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            // Goal Cards
            items(state.goals, key = { it.id }) { goal ->
                GoalCard(
                    goal = goal,
                    onClick = { viewModel.onIntent(GoalsStore.Intent.ShowAddSavingsDialog(goal.id)) }
                )
            }

            // Add New Goal Button
            item {
                AddGoalButton(onClick = { viewModel.onIntent(GoalsStore.Intent.ShowAddGoalDialog) })
            }

            // Motivational Tip Card
            item {
                TipCard(overallProgress = state.overallProgress)
            }

            // Quick Stats Grid
            item {
                QuickStatsRow(
                    completedCount = state.goals.count { it.isCompleted },
                    avgProgress = if (state.goals.isNotEmpty()) state.goals.map { it.progressPercent }
                        .average() else 0.0
                )
            }
        }
    }

    // Add Savings Dialog
    if (state.showAddSavingsDialog && state.selectedGoalId != null) {
        AddSavingsDialog(
            onDismiss = { viewModel.onIntent(GoalsStore.Intent.DismissDialog) },
            onConfirm = { amount ->
                viewModel.onIntent(
                    GoalsStore.Intent.AddAmountToGoal(
                        state.selectedGoalId!!,
                        amount
                    )
                )
            }
        )
    }

    // Add Goal Dialog
    if (state.showAddGoalDialog) {
        AddGoalDialog(
            onDismiss = { viewModel.onIntent(GoalsStore.Intent.DismissDialog) },
            onConfirm = { name, target ->
                viewModel.onIntent(
                    GoalsStore.Intent.AddGoal(
                        name = name,
                        targetAmount = target,
                        deadline = null,
                        icon = "target",
                        color = "#0EA5E9"
                    )
                )
            }
        )
    }
}

// ── Header ──────────────────────────────────────────────────────────────

@Composable
private fun GoalsHeader(onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MizanTheme.premium.glass.bg)
                .border(1.dp, MizanTheme.premium.glass.border, CircleShape)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_notification),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(MizanTheme.premium.spacing.md))
        Column {
            Text(
                "Financial Goals",
                style = MizanTheme.premium.typography.headingLg,
                color = MizanTheme.premium.text.primary
            )
            Text(
                "Achieve your dreams, one step at a time.",
                style = MizanTheme.premium.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }
    }
}

// ── Overall Progress Card ───────────────────────────────────────────────

@Composable
private fun OverallProgressCard(
    totalSaved: Double,
    totalTarget: Double,
    overallProgress: Double,
    goalCount: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (overallProgress / 100.0).toFloat().coerceIn(0f, 1f),
        animationSpec = tween(800),
        label = "progress"
    )
    val skyBlue = Color(0xFF0EA5E9)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xxl))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.10f),
                        Color.White.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.20f),
                RoundedCornerShape(MizanTheme.premium.radius.xxl)
            )
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(250.dp)
                .graphicsLayer { alpha = 0.2f }
                .background(
                    Brush.radialGradient(colors = listOf(skyBlue, Color.Transparent)),
                    shape = CircleShape
                )
        )

        Column(modifier = Modifier.padding(MizanTheme.premium.spacing.xl)) {
            // Label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎯", fontSize = 20.sp)
                Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                Text(
                    "Total Savings Progress",
                    style = MizanTheme.premium.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Amount
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    formatUZS(totalSaved),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                Text(
                    "/ ${formatUZS(totalTarget)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
            Text(
                "UZS",
                style = MizanTheme.premium.typography.bodySm,
                color = Color.White.copy(alpha = 0.4f)
            )

            Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(alpha = 0.10f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(skyBlue, Color(0xFF06B6D4))
                            )
                        )
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Bottom row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(skyBlue.copy(alpha = 0.2f))
                        .border(1.dp, skyBlue.copy(alpha = 0.3f), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📈", fontSize = 14.sp)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "${overallProgress.roundToInt()}% Achieved",
                            style = MizanTheme.premium.typography.labelSm,
                            color = skyBlue
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✨", fontSize = 14.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "$goalCount ${if (goalCount == 1) "Goal" else "Goals"}",
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

// ── Goal Card ───────────────────────────────────────────────────────────

@Composable
private fun GoalCard(goal: Goal, onClick: () -> Unit) {
    val progress = goal.progressPercent
    val completed = goal.isCompleted
    val progressColor = if (completed) Color(0xFF10B981) else Color(0xFF0EA5E9)
    val iconColor = try {
        Color(android.graphics.Color.parseColor(goal.color))
    } catch (_: Exception) {
        Color(0xFF0EA5E9)
    }
    val animatedProgress by animateFloatAsState(
        targetValue = (progress / 100.0).toFloat().coerceIn(0f, 1f),
        animationSpec = tween(600),
        label = "goalProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.glass.bg)
            .border(
                1.dp,
                MizanTheme.premium.glass.border,
                RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        Column {
            // Top row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(iconColor.copy(alpha = 0.15f))
                        .border(
                            1.dp,
                            iconColor.copy(alpha = 0.25f),
                            RoundedCornerShape(MizanTheme.premium.radius.lg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(goalIconEmoji(goal.icon), fontSize = 24.sp)
                }

                Spacer(Modifier.width(MizanTheme.premium.spacing.md))

                // Title & Amount
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            goal.name,
                            style = MizanTheme.premium.typography.labelLg,
                            color = Color.White,
                            maxLines = 1
                        )
                        if (completed) {
                            Spacer(Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFF10B981).copy(alpha = 0.2f))
                                    .border(
                                        1.dp,
                                        Color(0xFF10B981).copy(alpha = 0.3f),
                                        RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "✓",
                                    style = MizanTheme.premium.typography.labelSm,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            formatUZS(goal.currentAmount),
                            style = MizanTheme.premium.typography.labelMd,
                            color = Color.White
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "/ ${formatUZS(goal.targetAmount)} UZS",
                            style = MizanTheme.premium.typography.bodySm,
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    }
                }

                // Percentage Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(progressColor.copy(alpha = 0.2f))
                        .border(1.dp, progressColor.copy(alpha = 0.3f), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        "${progress.roundToInt()}%",
                        style = MizanTheme.premium.typography.labelSm,
                        color = progressColor
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White.copy(alpha = 0.10f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(progressColor)
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

            // Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📅", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Target: ${goal.deadline?.let { formatDate(it) } ?: "No deadline"}",
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                }
                if (completed) {
                    Text(
                        "Goal Achieved! 🎉",
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color(0xFF10B981)
                    )
                } else {
                    goal.deadline?.let { deadline ->
                        val daysLeft =
                            ((deadline - System.currentTimeMillis()) / 86_400_000).toInt()
                        if (daysLeft > 0) {
                            Text(
                                "$daysLeft days left",
                                style = MizanTheme.premium.typography.bodySm,
                                color = Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Add Goal Button ─────────────────────────────────────────────────────

@Composable
private fun AddGoalButton(onClick: () -> Unit) {
    val skyBlue = Color(0xFF0EA5E9)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.20f),
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .clickable { onClick() }
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(skyBlue.copy(alpha = 0.2f))
                    .border(1.dp, skyBlue.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = skyBlue)
            }
            Spacer(Modifier.width(MizanTheme.premium.spacing.md))
            Column {
                Text(
                    "Add New Goal",
                    style = MizanTheme.premium.typography.labelLg,
                    color = Color.White
                )
                Text(
                    "Start planning your next achievement",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}

// ── Tip Card ────────────────────────────────────────────────────────────

@Composable
private fun TipCard(overallProgress: Double) {
    val skyBlue = Color(0xFF0EA5E9)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.glass.bg)
            .border(
                1.dp,
                MizanTheme.premium.glass.border,
                RoundedCornerShape(MizanTheme.premium.radius.xl)
            )
            .padding(MizanTheme.premium.spacing.lg)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(skyBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("✨", fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Financial Tip",
                    style = MizanTheme.premium.typography.labelMd,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "You're ${overallProgress.roundToInt()}% closer to your dreams! Consider automating your savings to reach goals faster.",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.6f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// ── Quick Stats ─────────────────────────────────────────────────────────

@Composable
private fun QuickStatsRow(completedCount: Int, avgProgress: Double) {
    val emerald = Color(0xFF10B981)
    val skyBlue = Color(0xFF0EA5E9)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Completed
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                .background(MizanTheme.premium.glass.bg)
                .border(
                    1.dp,
                    MizanTheme.premium.glass.border,
                    RoundedCornerShape(MizanTheme.premium.radius.xl)
                )
                .padding(MizanTheme.premium.spacing.lg)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(emerald.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            emerald.copy(alpha = 0.3f),
                            RoundedCornerShape(MizanTheme.premium.radius.lg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 20.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Completed",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "$completedCount",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    if (completedCount == 1) "goal" else "goals",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }
        }

        // Avg Progress
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                .background(MizanTheme.premium.glass.bg)
                .border(
                    1.dp,
                    MizanTheme.premium.glass.border,
                    RoundedCornerShape(MizanTheme.premium.radius.xl)
                )
                .padding(MizanTheme.premium.spacing.lg)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(skyBlue.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            skyBlue.copy(alpha = 0.3f),
                            RoundedCornerShape(MizanTheme.premium.radius.lg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📈", fontSize = 20.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Avg. Progress",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${avgProgress.roundToInt()}%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "across all goals",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

// ── Dialogs ─────────────────────────────────────────────────────────────

@Composable
private fun AddSavingsDialog(onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var amountText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Savings") },
        text = {
            TextField(
                value = amountText,
                onValueChange = { amountText = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Amount (UZS)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                amountText.toDoubleOrNull()?.let { onConfirm(it) }
            }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AddGoalDialog(onDismiss: () -> Unit, onConfirm: (String, Double) -> Unit) {
    var name by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Goal") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal Name") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = targetText,
                    onValueChange = { targetText = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Target Amount (UZS)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val target = targetText.toDoubleOrNull()
                if (name.isNotBlank() && target != null && target > 0) {
                    onConfirm(name, target)
                }
            }) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// ── Utils ───────────────────────────────────────────────────────────────

private fun formatUZS(amount: Double): String {
    return String.format("%,.0f", amount).replace(',', ' ')
}

private fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("MMM yyyy", Locale.US)
    return sdf.format(Date(millis))
}

private fun goalIconEmoji(icon: String): String = when (icon) {
    "home" -> "🏠"
    "car" -> "🚗"
    "plane" -> "✈️"
    "trophy" -> "🏆"
    "graduation" -> "🎓"
    else -> "🎯"
}

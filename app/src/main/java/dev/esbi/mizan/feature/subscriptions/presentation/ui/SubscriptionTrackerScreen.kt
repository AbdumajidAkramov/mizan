package dev.esbi.mizan.feature.subscriptions.presentation.ui

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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.subscriptions.presentation.SubscriptionsViewModel
import dev.esbi.mizan.presentation.feature.subscriptions.domain.model.BillingCycle
import dev.esbi.mizan.presentation.feature.subscriptions.domain.model.Subscription
import dev.esbi.mizan.presentation.feature.subscriptions.presentation.store.SubscriptionsStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SubscriptionTrackerScreen(
    viewModel: SubscriptionsViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState(initial = SubscriptionsStore.State())

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
            item { SubscriptionsHeader(onBack = onBack) }

            // Monthly Summary Card
            item {
                MonthlySummaryCard(
                    monthlyTotal = state.totalMonthlyCost.toDouble(),
                    subscriptionCount = state.subscriptions.size,
                    subscriptions = state.subscriptions
                )
            }

            // Active Subscriptions Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Active Subscriptions",
                        style = MizanTheme.premium.typography.headingSm,
                        color = MizanTheme.premium.text.primary.copy(alpha = 0.9f)
                    )
                    Text(
                        "${state.subscriptions.size} ${if (state.subscriptions.size == 1) "service" else "services"}",
                        style = MizanTheme.premium.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            // Subscription Items
            items(state.subscriptions, key = { it.id }) { subscription ->
                SubscriptionItem(subscription = subscription)
            }

            // Add New Subscription Button
            item {
                AddSubscriptionButton(onClick = { viewModel.onIntent(SubscriptionsStore.Intent.ShowAddDialog) })
            }

            // Insights Card
            item {
                InsightsCard(monthlyTotal = state.totalMonthlyCost.toDouble())
            }
        }
    }
}

// ── Header ──────────────────────────────────────────────────────────────

@Composable
private fun SubscriptionsHeader(onBack: () -> Unit) {
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
                painter = painterResource(Icons.ic_notification),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(MizanTheme.premium.spacing.md))
        Column {
            Text(
                "Subscriptions",
                style = MizanTheme.premium.typography.headingLg,
                color = MizanTheme.premium.text.primary
            )
            Text(
                "Manage your recurring payments",
                style = MizanTheme.premium.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }
    }
}

// ── Monthly Summary Card ────────────────────────────────────────────────

@Composable
private fun MonthlySummaryCard(
    monthlyTotal: Double,
    subscriptionCount: Int,
    subscriptions: List<Subscription>
) {
    val emerald = Color(0xFF10B981)
    val amber = Color(0xFFF59E0B)

    // Find next payment
    val now = System.currentTimeMillis()
    val nextPayment = subscriptions
        .filter { it.nextRenewalDate > now }
        .minByOrNull { it.nextRenewalDate }
    val daysUntilNext = nextPayment?.let {
        ((it.nextRenewalDate - now) / 86_400_000).toInt().coerceAtLeast(0)
    } ?: 0

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
                .size(200.dp)
                .graphicsLayer { alpha = 0.2f }
                .background(
                    Brush.radialGradient(colors = listOf(emerald, Color.Transparent)),
                    shape = CircleShape
                )
        )

        Column(modifier = Modifier.padding(MizanTheme.premium.spacing.xl)) {
            // Icon + Label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(emerald.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            emerald.copy(alpha = 0.3f),
                            RoundedCornerShape(MizanTheme.premium.radius.lg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💳", fontSize = 20.sp)
                }
                Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
                Column {
                    Text(
                        "Monthly Total",
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                    Text(
                        "$subscriptionCount active subscriptions",
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Total Amount
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    formatUZS(monthlyTotal),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "UZS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Next payment badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(amber.copy(alpha = 0.2f))
                    .border(1.dp, amber.copy(alpha = 0.3f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📅", fontSize = 14.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Next payment in $daysUntilNext days",
                        style = MizanTheme.premium.typography.labelSm,
                        color = amber
                    )
                }
            }
        }
    }
}

// ── Subscription Item ───────────────────────────────────────────────────

@Composable
private fun SubscriptionItem(subscription: Subscription) {
    val iconColor = try {
        Color(android.graphics.Color.parseColor(subscription.color))
    } catch (_: Exception) {
        Color(0xFF10B981)
    }
    val now = System.currentTimeMillis()
    val daysUntil = ((subscription.nextRenewalDate - now) / 86_400_000).toInt().coerceAtLeast(0)

    // Progress: how much of the billing cycle has elapsed
    val totalDays = when (subscription.billingCycle) {
        BillingCycle.YEARLY -> 365
        BillingCycle.MONTHLY -> 30
    }
    val daysPassed = totalDays - daysUntil
    val progressPercent = (daysPassed.toFloat() / totalDays).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent,
        animationSpec = tween(600),
        label = "subProgress"
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
            .padding(MizanTheme.premium.spacing.md)
    ) {
        Column {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Service Icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(iconColor.copy(alpha = 0.15f))
                        .border(
                            1.dp,
                            iconColor.copy(alpha = 0.25f),
                            RoundedCornerShape(MizanTheme.premium.radius.lg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(subscriptionIconEmoji(subscription.icon), fontSize = 26.sp)
                }

                Spacer(Modifier.width(MizanTheme.premium.spacing.md))

                // Service Info
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            subscription.name,
                            style = MizanTheme.premium.typography.labelLg,
                            color = Color.White,
                            maxLines = 1
                        )
                        Spacer(Modifier.width(6.dp))
                        StatusBadge(subscription)
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "${
                            subscription.billingCycle.name.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        } • Next: ${formatShortDate(subscription.nextRenewalDate)}",
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color.White.copy(alpha = 0.4f)
                    )
                }

                // Amount
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        formatUZS(subscription.amount.toDouble()),
                        style = MizanTheme.premium.typography.labelLg,
                        color = Color.White
                    )
                    Text(
                        "UZS",
                        style = MizanTheme.premium.typography.bodySm,
                        color = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Time until next payment",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Text(
                    "$daysUntil ${if (daysUntil == 1) "day" else "days"}",
                    style = MizanTheme.premium.typography.labelSm,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Spacer(Modifier.height(4.dp))
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
                        .background(Color(0xFF10B981))
                )
            }
        }
    }
}

// ── Status Badge ────────────────────────────────────────────────────────

@Composable
private fun StatusBadge(subscription: Subscription) {
    val emerald = Color(0xFF10B981)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(emerald.copy(alpha = 0.2f))
            .border(1.dp, emerald.copy(alpha = 0.3f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            "Active",
            style = MizanTheme.premium.typography.labelSm,
            color = emerald
        )
    }
}

// ── Add Subscription Button ─────────────────────────────────────────────

@Composable
private fun AddSubscriptionButton(onClick: () -> Unit) {
    val emerald = Color(0xFF10B981)

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
                    .background(emerald.copy(alpha = 0.2f))
                    .border(1.dp, emerald.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = emerald)
            }
            Spacer(Modifier.width(MizanTheme.premium.spacing.md))
            Column {
                Text(
                    "Add New Subscription",
                    style = MizanTheme.premium.typography.labelLg,
                    color = Color.White
                )
                Text(
                    "Track your recurring payments",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}

// ── Insights Card ───────────────────────────────────────────────────────

@Composable
private fun InsightsCard(monthlyTotal: Double) {
    val emerald = Color(0xFF10B981)

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
                    .background(emerald.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("⚡", fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Smart Tip",
                    style = MizanTheme.premium.typography.labelMd,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "You're spending ${formatUZS(monthlyTotal)} UZS per month on subscriptions. Review unused services to save money.",
                    style = MizanTheme.premium.typography.bodySm,
                    color = Color.White.copy(alpha = 0.6f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// ── Utils ───────────────────────────────────────────────────────────────

private fun formatUZS(amount: Double): String {
    return String.format("%,.0f", amount).replace(',', ' ')
}

private fun formatShortDate(millis: Long): String {
    val sdf = SimpleDateFormat("MMM d", Locale.US)
    return sdf.format(Date(millis))
}

private fun subscriptionIconEmoji(icon: String): String = when (icon) {
    "tv" -> "📺"
    "music" -> "🎵"
    "play" -> "▶️"
    "cloud" -> "☁️"
    "film" -> "🎬"
    "zap" -> "⚡"
    "gamepad" -> "🎮"
    "phone" -> "📱"
    "coffee" -> "☕"
    else -> "💳"
}

package dev.esbi.mizan.feature.budget.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.budget.domain.model.BudgetStatus
import dev.esbi.mizan.feature.budget.domain.model.CategoryBudget
import dev.esbi.mizan.ui.animation.animateProgressAsState
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.utils.Strings
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CategoryBudgetItem(
    budget: CategoryBudget,
    categoryColor: Color,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }

    val animatedProgress by animateProgressAsState(
        targetProgress = (budget.percentage / 100f).coerceIn(0f, 1f)
    )

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    categoryColor.copy(alpha = 0.4f),
                                    categoryColor.copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getCategoryEmoji(budget.categoryId),
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = budget.categoryName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${numberFormat.format(budget.spentAmount)} / ${
                                numberFormat.format(
                                    budget.budgetAmount
                                )
                            }",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Status Badge
                        when (budget.status) {
                            BudgetStatus.OVER_BUDGET -> {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFF6B6B).copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "⚠️",
                                            fontSize = 10.sp
                                        )
                                        Text(
                                            text = stringResource(Strings.budget_over_budget),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFFF6B6B)
                                        )
                                    }
                                }
                            }

                            BudgetStatus.NEAR_LIMIT -> {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFEE140).copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "⚠️",
                                            fontSize = 10.sp
                                        )
                                        Text(
                                            text = stringResource(Strings.budget_near_limit),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFFEE140)
                                        )
                                    }
                                }
                            }

                            BudgetStatus.NORMAL -> {}
                        }
                    }
                }

                // Edit Button
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_edit),
                        contentDescription = stringResource(Strings.budget_edit),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val progressModifier = when (budget.status) {
                    BudgetStatus.OVER_BUDGET ->
                        Modifier.background(
                            color = Color(0xFFFF6B6B),
                            shape = RoundedCornerShape(4.dp)
                        )

                    BudgetStatus.NEAR_LIMIT ->
                        Modifier.background(
                            color = Color(0xFFFEE140),
                            shape = RoundedCornerShape(4.dp)
                        )

                    BudgetStatus.NORMAL ->
                        Modifier.background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    categoryColor,
                                    categoryColor.copy(alpha = 0.8f)
                                )
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                }
                Box(modifier = progressModifier)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Strings.budget_percentage_used_value, budget.percentage),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(
                        Strings.budget_left,
                        numberFormat.format(budget.budgetAmount - budget.spentAmount)
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun getCategoryEmoji(categoryId: String): String {
    return when (categoryId) {
        "food" -> "🍔"
        "transport" -> "🚗"
        "shopping" -> "🛍️"
        "bills" -> "📄"
        "entertainment" -> "🎬"
        "health" -> "💪"
        "travel" -> "✈️"
        "tech" -> "💻"
        else -> "📊"
    }
}

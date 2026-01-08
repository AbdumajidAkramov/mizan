package dev.esbi.mizan.feature.transactions.presentation.ui.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.transactions.domain.model.Transaction
import dev.esbi.mizan.feature.transactions.domain.model.TransactionType
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PremiumTransactionItem(
    transaction: Transaction,
    onClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(transaction.category)
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 2
    }
    
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    val date = Date(transaction.timestamp)

    PremiumCard(
        variant = PremiumCardVariant.Glass,
        onClick = { onClick(transaction) },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
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
                    text = getCategoryEmoji(transaction.category),
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Transaction Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = transaction.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = PremiumColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Icon(
                        painter = painterResource(
                            if (transaction.type == TransactionType.INCOME)
                                android.R.drawable.arrow_down_float
                            else
                                android.R.drawable.arrow_up_float
                        ),
                        contentDescription = null,
                        tint = if (transaction.type == TransactionType.INCOME)
                            Color(0xFF06FFA5)
                        else
                            Color(0xFFFF6B6B),
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = timeFormat.format(date),
                        fontSize = 12.sp,
                        color = PremiumColors.TextTertiary
                    )
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(PremiumColors.TextMuted)
                    )
                    Text(
                        text = dateFormat.format(date),
                        fontSize = 12.sp,
                        color = PremiumColors.TextTertiary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Amount
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}${numberFormat.format(transaction.amount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == TransactionType.INCOME)
                        Color(0xFF06FFA5)
                    else
                        PremiumColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.categoryName,
                    fontSize = 11.sp,
                    color = PremiumColors.TextMuted
                )
            }
        }
    }
}

private fun getCategoryColor(categoryId: String): Color {
    return when (categoryId) {
        "food" -> Color(0xFFFF6B6B)
        "transport" -> Color(0xFF4ECDC4)
        "shopping" -> Color(0xFFFFBE0B)
        "bills" -> Color(0xFF667EEA)
        "entertainment" -> Color(0xFFFF006E)
        "health" -> Color(0xFF06FFA5)
        "income" -> Color(0xFF4FACFE)
        else -> Color(0xFF667EEA)
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
        "income" -> "💰"
        else -> "📊"
    }
}

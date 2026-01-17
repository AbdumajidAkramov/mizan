package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.dashboard.domain.model.Transaction
import dev.esbi.mizan.feature.dashboard.domain.model.TransactionType
import dev.esbi.mizan.feature.dashboard.presentation.widgets.premium.PremiumCard
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Orange
import dev.esbi.mizan.ui.theme.Pink
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.Red
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TxnItem(txn: Transaction) {
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
    PremiumCard {
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
                        color = MizanTheme.premium.text.primary,
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
                    color = MizanTheme.premium.text.tertiary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    if (isInc) "+${fmt.format(txn.amount)}" else "-${fmt.format(txn.amount)}",
                    fontWeight = FontWeight.SemiBold,
                    color = if (isInc) MizanTheme.premium.colors.success else MizanTheme.premium.colors.error
                )
                Text(
                    txn.categoryLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MizanTheme.premium.text.muted
                )
            }
        }
    }
}

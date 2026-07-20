package dev.esbi.mizan.features.dashboard.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.Purple
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.domain.model.Transaction

@Composable
fun TransactionsSection(
    txns: List<Transaction>,
    onSeeAllClick: () -> Unit = {}
) {
    Column {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(
                "Recent Transactions",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MizanTheme.premium.text.primary
            )
            Text(
                text = "See all",
                style = MaterialTheme.typography.bodySmall,
                color = Purple,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            txns.take(5).forEach { TxnItem(it) }
        }
    }
}

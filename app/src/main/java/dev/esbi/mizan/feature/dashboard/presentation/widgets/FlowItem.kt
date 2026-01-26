package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.Red
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.TextWhite
import dev.esbi.mizan.ui.utils.Icons
import java.text.NumberFormat
import java.util.Locale


@Composable
fun FlowItem(isIncome: Boolean, amount: Double) {
    val fmt = NumberFormat.getNumberInstance(Locale.US)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isIncome) Purple.copy(0.2f) else Red.copy(0.2f)), Alignment.Center
        ) {
            MizanIcon(
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

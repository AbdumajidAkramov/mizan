package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.Red
import dev.esbi.mizan.ui.utils.Icons
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SubCard(label: String, amount: Double, isIncome: Boolean, modifier: Modifier) {
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

@Preview
@Composable
fun SubCardPreview() {
    MizanTheme {
        SubCard("Income", 12000.0, true, Modifier)
    }
}
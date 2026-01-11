package dev.esbi.mizan.feature.dashboard.presentation.widgets.balancecard


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.dashboard.presentation.widgets.PressCard
import dev.esbi.mizan.feature.dashboard.presentation.widgets.SubCard
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.Pink
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.Purple2
import dev.esbi.mizan.ui.utils.Icons
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BalanceCard(total: Double, income: Double, expenses: Double) {
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

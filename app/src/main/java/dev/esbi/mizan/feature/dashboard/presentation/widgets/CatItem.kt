package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.dashboard.CategorySpending
import dev.esbi.mizan.ui.theme.Cyan
import dev.esbi.mizan.ui.theme.Orange
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.TextGray
import dev.esbi.mizan.ui.theme.TextWhite
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CatItem(cat: CategorySpending, onClick: () -> Unit) {
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

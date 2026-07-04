package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.domain.model.dashboard.CategorySpending
import dev.esbi.mizan.design.kit.glass.GlassCard
import dev.esbi.mizan.design.theme.Purple
import dev.esbi.mizan.design.theme.TextWhite

@Composable
fun CategoriesSection(cats: List<CategorySpending>, onClick: (String) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(
                "Top Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
            Text("See all", style = MaterialTheme.typography.bodySmall, color = Purple)
        }
        Spacer(Modifier.height(16.dp))
        GlassCard {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DonutChart(cats.take(4), Modifier.size(140.dp))
                Spacer(Modifier.width(24.dp))
                Column(Modifier.weight(1f), Arrangement.spacedBy(12.dp)) {
                    cats.take(4).forEach { CatItem(it) { onClick(it.category) } }
                }
            }
        }
    }
}

package dev.esbi.mizan.feature.statistics.presentation.ui.widgets


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.statistics.domain.model.CategoryData
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun TopCategoriesCard(
    categoryData: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    GlassCard {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "Top Categories",
                style = MizanTheme.typography.headingMd,
                color = MizanTheme.premium.text.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Highest spending categories",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
            Spacer(modifier = Modifier.height(24.dp))

            BarChart(
                data = categoryData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

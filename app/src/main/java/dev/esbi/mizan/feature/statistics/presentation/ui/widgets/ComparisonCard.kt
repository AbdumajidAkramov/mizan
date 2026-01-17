package dev.esbi.mizan.feature.statistics.presentation.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.kit.glass.GlassCard
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun ComparisonCard(
    label: String,
    amount: String,
    change: String,
    isPositive: Boolean,
    gradientColors: List<Color>,
    icon: IconValue,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.padding(16.dp)
    ) {

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    dev.esbi.mizan.ui.kit.icon.Icon(
                        icon = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = MizanTheme.premium.text.muted
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = amount,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MizanTheme.premium.text.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = change,
                    fontSize = 11.sp,
                    color = if (isPositive) MizanTheme.premium.colors.success else MizanTheme.premium.colors.error
                )
                Text(
                    text = " vs last month",
                    fontSize = 11.sp,
                    color = MizanTheme.premium.text.muted
                )

            }
        }
    }
}

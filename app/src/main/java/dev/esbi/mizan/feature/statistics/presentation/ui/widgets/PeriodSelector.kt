package dev.esbi.mizan.feature.statistics.presentation.ui.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod
import dev.esbi.mizan.ui.theme.colors.MizanTheme


@Composable
internal fun PeriodSelector(
    selectedPeriod: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val periods = listOf(TimePeriod.WEEK, TimePeriod.MONTH, TimePeriod.YEAR)
    val selectedIndex = periods.indexOf(selectedPeriod)

    val offsetX by animateFloatAsState(
        targetValue = selectedIndex * 120f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "period_offset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MizanTheme.premium.colors.surface2)
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX.dp)
                .width(120.dp)
                .height(48.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MizanTheme.premium.colors.surface3)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            periods.forEach { period ->

                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(48.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onPeriodSelected(period) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = period.name.lowercase().capitalize(Locale.current),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (period == selectedPeriod) MizanTheme.premium.text.primary else MizanTheme.premium.text.tertiary
                    )
                }
            }
        }
    }
}

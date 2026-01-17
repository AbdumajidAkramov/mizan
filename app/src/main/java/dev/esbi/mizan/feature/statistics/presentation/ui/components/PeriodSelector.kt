package dev.esbi.mizan.feature.statistics.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod
import dev.esbi.mizan.ui.theme.PremiumColors

/*

@Composable
fun PeriodSelector(
    selectedPeriod: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PeriodButton(
            label = stringResource(R.string.statistics_period_week),
            isSelected = selectedPeriod == TimePeriod.WEEK,
            onClick = { onPeriodSelected(TimePeriod.WEEK) },
            modifier = Modifier.weight(1f)
        )
        PeriodButton(
            label = stringResource(R.string.statistics_period_month),
            isSelected = selectedPeriod == TimePeriod.MONTH,
            onClick = { onPeriodSelected(TimePeriod.MONTH) },
            modifier = Modifier.weight(1f)
        )
        PeriodButton(
            label = stringResource(R.string.statistics_period_year),
            isSelected = selectedPeriod == TimePeriod.YEAR,
            onClick = { onPeriodSelected(TimePeriod.YEAR) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PeriodButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) PremiumColors.Surface3 else androidx.compose.ui.graphics.Color.Transparent
    val textColor = if (isSelected) PremiumColors.TextPrimary else PremiumColors.TextTertiary

    Text(
        text = label,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
    )
}
*/

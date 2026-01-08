package dev.esbi.mizan.feature.transactions.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.feature.transactions.domain.model.TransactionFilter
import dev.esbi.mizan.ui.theme.PremiumColors

@Composable
fun FilterChips(
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            label = stringResource(R.string.transactions_filter_all),
            isSelected = selectedFilter == TransactionFilter.ALL,
            onClick = { onFilterSelected(TransactionFilter.ALL) },
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
            )
        )

        FilterChip(
            label = stringResource(R.string.transactions_filter_expenses),
            isSelected = selectedFilter == TransactionFilter.EXPENSE,
            onClick = { onFilterSelected(TransactionFilter.EXPENSE) },
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFFF093FB), Color(0xFFF5576C))
            ),
            icon = android.R.drawable.arrow_down_float
        )

        FilterChip(
            label = stringResource(R.string.transactions_filter_income),
            isSelected = selectedFilter == TransactionFilter.INCOME,
            onClick = { onFilterSelected(TransactionFilter.INCOME) },
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
            ),
            icon = android.R.drawable.arrow_up_float
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    gradient: Brush,
    icon: Int? = null,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        gradient
    } else {
        Brush.linearGradient(
            colors = listOf(
                PremiumColors.Surface2,
                PremiumColors.Surface2
            )
        )
    }

    val textColor = if (isSelected) Color.White else PremiumColors.TextSecondary

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null && isSelected) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

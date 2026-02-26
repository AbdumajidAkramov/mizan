package dev.esbi.mizan.feature.premiumaddtransaction.currency

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun MizanCurrencySelector(
    currencies: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs),
        contentPadding = PaddingValues(horizontal = MizanTheme.premium.spacing.sm), // Chekkalardan bo'shliq
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(currencies) { currency ->
            CurrencyChip(
                label = currency,
                isSelected = currency == selectedCurrency,
                onClick = { onCurrencySelected(currency) }
            )
        }
    }
}


@Composable
fun CurrencyChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Ranglarni silliq o'zgarishi uchun animatsiya
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MizanTheme.premium.colors.emerald.copy(alpha = 0.2f)
        else Color.Transparent,
        label = "bgColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MizanTheme.premium.colors.emerald
        else MizanTheme.premium.text.tertiary,
        label = "contentColor"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MizanTheme.premium.colors.emerald.copy(alpha = 0.3f)
        else MizanTheme.premium.colors.secondaryLight,
        label = "borderColor"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.height(30.dp),
        shape = RoundedCornerShape(MizanTheme.premium.radius.full), // To'liq dumaloq
        color = backgroundColor,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = MizanTheme.premium.spacing.md),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MizanTheme.typography.labelLg.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor
                )
            )
        }
    }
}

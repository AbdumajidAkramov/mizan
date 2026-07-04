package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.design.theme.colors.MizanTheme

@Composable
fun AddTransactionCurrencySelector(
    currencies: List<Currency>,
    selectedCurrency: Currency?,
    onCurrencySelected: (Currency) -> Unit,
    onAddNewSubCurrency: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sort: Main currency first, then others by order index

    val sortedCurrencies = currencies.sortedWith(
        compareByDescending<Currency> { it.isMainCurrency }
            .thenBy { it.orderIndex }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        LazyRow(
            modifier = modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(sortedCurrencies) { currency ->
                CurrencyChip(
                    label = currency.code,
                    isSelected = currency == selectedCurrency,
                    isMain = currency.isMainCurrency,
                    onClick = { onCurrencySelected(currency) }
                )
            }
            item {
                Row(modifier = Modifier) {
                    Spacer(modifier = Modifier.width(16.dp))
                    CurrencyChip(
                        label = "+",
                        isMain = false,
                        isSelected = false,
                        onClick = onAddNewSubCurrency,
                    )
                }
            }
        }
    }
}


@Composable
fun CurrencyChip(
    label: String,
    isMain: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        Brush.linearGradient(
            colors = listOf(
                MizanTheme.premium.colors.emerald.copy(alpha = 0.3f),
                MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MizanTheme.premium.colors.surface2.copy(alpha = 0.15f),
                MizanTheme.premium.colors.surface2.copy(alpha = 0.08f)
            )
        )
    }
    val borderColor = if (isSelected) {
        MizanTheme.premium.colors.emerald.copy(alpha = 0.5f)
    } else {
        MizanTheme.premium.colors.surface2.copy(alpha = 0.2f)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isMain) {
                "$label ★"
            } else {
                label
            },
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) {
                MizanTheme.premium.colors.emerald
            } else {
                MizanTheme.premium.text.primary.copy(alpha = 0.8f)
            }
        )
    }
}

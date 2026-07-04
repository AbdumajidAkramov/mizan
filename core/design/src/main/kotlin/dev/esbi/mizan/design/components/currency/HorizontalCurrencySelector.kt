package dev.esbi.mizan.presentation.utils.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import dev.esbi.mizan.design.components.currency.CurrencyModel
import dev.esbi.mizan.design.theme.colors.MizanTheme

/**
 * Horizontal currency selector with glassmorphic badges.
 * Displays main currency first, followed by enabled sub-currencies.
 * 
 * @param currencies List of available currencies (main + sub-currencies)
 * @param selectedCurrency Currently selected currency
 * @param onCurrencySelected Callback when a currency is selected
 * @param modifier Modifier for the composable
 */
@Composable
fun HorizontalCurrencySelector(
    currencies: List<CurrencyModel>,
    selectedCurrency: CurrencyModel?,
    onCurrencySelected: (CurrencyModel) -> Unit,
    modifier: Modifier = Modifier
) {
    // Sort: Main currency first, then others by order index
    val sortedCurrencies = currencies.sortedWith(
        compareByDescending<CurrencyModel> { it.isMainCurrency }
            .thenBy { it.orderIndex }
    )

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(sortedCurrencies) { currency ->
            CurrencyBadge(
                currency = currency,
                isSelected = currency.code == selectedCurrency?.code,
                onClick = { onCurrencySelected(currency) }
            )
        }
    }
}

/**
 * Individual currency badge with glassmorphic styling.
 */
@Composable
private fun CurrencyBadge(
    currency: CurrencyModel,
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
                Color.White.copy(alpha = 0.15f),
                Color.White.copy(alpha = 0.08f)
            )
        )
    }

    val borderColor = if (isSelected) {
        MizanTheme.premium.colors.emerald.copy(alpha = 0.5f)
    } else {
        Color.White.copy(alpha = 0.2f)
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
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (currency.isMainCurrency) {
                "${currency.code} ★"
            } else {
                currency.code
            },
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) {
                MizanTheme.premium.colors.emerald
            } else {
                Color.White.copy(alpha = 0.8f)
            }
        )
    }
}

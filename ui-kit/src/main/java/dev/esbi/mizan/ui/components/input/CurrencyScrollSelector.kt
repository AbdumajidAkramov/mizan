package dev.esbi.mizan.ui.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

data class SelectorCurrency(
    val code: String,
    val symbol: String
)

@Composable
fun CurrencyScrollSelector(
    currencies: List<SelectorCurrency>,
    selectedCurrency: SelectorCurrency?,
    onCurrencySelected: (SelectorCurrency) -> Unit,
    onAddCustomClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Currency",
            color = MizanTheme.premium.text.secondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 0.dp) // parent might apply padding
        ) {
            items(currencies, key = { it.code }) { currency ->
                val isSelected = selectedCurrency?.code == currency.code
                CurrencyChip(
                    currency = currency,
                    isSelected = isSelected,
                    onClick = { onCurrencySelected(currency) }
                )
            }

            item {
                AddCustomCurrencyChip(onClick = onAddCustomClick)
            }
        }

        if (errorText != null) {
            Text(
                text = errorText,
                color = MizanTheme.premium.colors.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )
        }
    }
}

@Composable
private fun CurrencyChip(
    currency: SelectorCurrency,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MizanTheme.premium.colors.emerald else MizanTheme.premium.background.secondary
    val bgColor = if (isSelected) MizanTheme.premium.colors.emerald.copy(alpha = 0.2f) else MizanTheme.premium.background.secondary

    Box(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = currency.symbol,
            color = if (isSelected) MizanTheme.premium.colors.emerald else MizanTheme.premium.text.secondary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun AddCustomCurrencyChip(onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MizanTheme.premium.background.secondary)
            .border(1.dp, MizanTheme.premium.background.secondary, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Custom",
                tint = MizanTheme.premium.text.secondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Custom",
                color = MizanTheme.premium.text.secondary,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

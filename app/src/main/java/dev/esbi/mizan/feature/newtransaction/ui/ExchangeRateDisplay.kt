package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Premium glassmorphic exchange rate display component.
 * Shows current exchange rate and opens bottom sheet on click.
 * 
 * Features:
 * - Glassmorphic design with blur effect
 * - Animated scale on rate change
 * - Edit icon for clear affordance
 * - Only visible for sub-currencies
 */
@Composable
fun ExchangeRateDisplay(
    selectedCurrency: Currency?,
    mainCurrency: Currency?,
    manualExchangeRate: BigDecimal?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Only show for sub-currencies
    if (selectedCurrency == null || mainCurrency == null || selectedCurrency.isMainCurrency) {
        return
    }

    val currentRate = manualExchangeRate ?: selectedCurrency.exchangeRate
    
    // Animate scale when rate changes
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 300),
        label = "rate_scale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.White.copy(alpha = 0.08f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Exchange Rate",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f)
            )
            
            Text(
                text = "1 ${selectedCurrency.code} = ${formatRate(currentRate)} ${mainCurrency.code}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MizanTheme.premium.colors.emerald,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            // Show indicator if manual rate is set
            if (manualExchangeRate != null) {
                Text(
                    text = "Custom rate",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = MizanTheme.premium.colors.emerald.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit exchange rate",
            tint = MizanTheme.premium.colors.emerald.copy(alpha = 0.8f)
        )
    }
}

/**
 * Format exchange rate with up to 8 decimal places.
 * Strips trailing zeros for cleaner display.
 */
private fun formatRate(rate: BigDecimal): String {
    val rounded = rate.setScale(8, RoundingMode.HALF_UP)
    return rounded.stripTrailingZeros().toPlainString()
}

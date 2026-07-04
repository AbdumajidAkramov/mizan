package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.design.theme.colors.MizanTheme
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Displays the equivalent amount in main currency with smooth animations.
 * Only visible when a sub-currency is selected.
 * 
 * Example: "≈ 1,230,000 UZS"
 */
@Composable
fun EquivalentAmountDisplay(
    selectedCurrency: Currency?,
    mainCurrency: Currency?,
    equivalentAmount: BigDecimal,
    modifier: Modifier = Modifier
) {
    // Only show for sub-currencies
    if (selectedCurrency == null || mainCurrency == null || selectedCurrency.isMainCurrency) {
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.emerald.copy(alpha = 0.15f),
                        MizanTheme.premium.colors.emerald.copy(alpha = 0.08f)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "≈ ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.6f)
        )
        
        // Animated amount value
        AnimatedContent(
            targetState = formatAmount(equivalentAmount),
            transitionSpec = {
                (slideInVertically(
                    animationSpec = tween(300),
                    initialOffsetY = { it / 2 }
                ) + fadeIn(tween(300))).togetherWith(
                    slideOutVertically(
                        animationSpec = tween(300),
                        targetOffsetY = { -it / 2 }
                    ) + fadeOut(tween(300))
                )
            },
            label = "equivalent_amount"
        ) { formattedAmount ->
            Text(
                text = formattedAmount,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MizanTheme.premium.colors.emerald
            )
        }
        
        Text(
            text = " ${mainCurrency.code}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

/**
 * Format amount with thousand separators and 2 decimal places.
 */
private fun formatAmount(amount: BigDecimal): String {
    val rounded = amount.setScale(2, RoundingMode.HALF_UP)
    return String.format("%,.2f", rounded.toDouble())
        .replace(",", " ")
}

package dev.esbi.mizan.ui.kit.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.PremiumColors
import java.text.NumberFormat
import java.util.Locale

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class AmountTextSize(val fontSize: TextUnit) {
    SM(12.sp),
    BASE(14.sp),
    LG(18.sp),
    XL(24.sp),
    XXL(32.sp)
}

@Composable
fun AmountText(
    amount: Double,
    type: TransactionType? = null,
    showSign: Boolean = false,
    size: AmountTextSize = AmountTextSize.BASE,
    modifier: Modifier = Modifier
) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val formattedAmount = numberFormat.format(amount).removePrefix("$")
    
    val sign = if (showSign) {
        when (type) {
            TransactionType.INCOME -> "+"
            TransactionType.EXPENSE -> "-"
            null -> ""
        }
    } else ""
    
    val color = when (type) {
        TransactionType.INCOME -> Color(0xFF00F2A0)
        TransactionType.EXPENSE -> PremiumColors.TextPrimary
        null -> PremiumColors.TextPrimary
    }
    
    Text(
        text = "$sign$$formattedAmount",
        fontSize = size.fontSize,
        color = color,
        fontWeight = FontWeight.Normal,
        modifier = modifier
    )
}

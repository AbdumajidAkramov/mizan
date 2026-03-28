package dev.esbi.mizan.ui.components.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun AccountRow(
    id: Long,
    name: String,
    balance: Double,
    currencyCode: String,
    colorHex: String?,
    iconName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("#,###.00", DecimalFormatSymbols(Locale.US)).apply {
        val symbols = this.decimalFormatSymbols
        symbols.groupingSeparator = ' '
        this.decimalFormatSymbols = symbols
    }

    val isNegative = balance < 0
    val formatted = formatter.format(kotlin.math.abs(balance))
    val parts = formatted.split(".")
    val integer = parts[0]
    val decimal = if (parts.size > 1) parts[1] else "00"

    // Parse the color, fallback to emerald-ish if invalid or null
    val parsedColor = try {
        if (!colorHex.isNullOrBlank()) {
            Color(android.graphics.Color.parseColor(colorHex))
        } else {
            Color(0xFF667EEA) // PremiumPrimary as fallback
        }
    } catch (e: Exception) {
        Color(0xFF667EEA)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Box
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(parsedColor.copy(alpha = 0.2f))
                .border(1.dp, parsedColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Pseudo-icon rendering - ideally we resolve iconName to drawable resource Id
            // using LocalContext or a helper, here we just use a generic icon text or placeholder
            Text(
                text = iconName?.take(1)?.uppercase() ?: "A",
                color = parsedColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Account Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // Optional Last Four digits simulation can be added here
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Balance
        val balanceColor = if (isNegative) Color(0xFFF5576C) else Color.White

        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isNegative) {
                    Text(
                        text = "-",
                        color = Color(0xFFF5576C),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = integer,
                    color = balanceColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ".$decimal",
                    color = balanceColor.copy(alpha = if (isNegative) 0.7f else 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = currencyCode,
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Chevron
        Text(
            text = "›",
            color = Color.White.copy(alpha = 0.3f),
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

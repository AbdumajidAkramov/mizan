package dev.esbi.mizan.ui.kit.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/*

@Composable
fun AccountRow(
    name: String,
    subtitle: String?,
    balance: BigDecimal,
    currencyCode: String,
    iconName: String?,
    color: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val iconBgColor = remember(color) { parseHexColor(color) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MizanTheme.premium.background.secondary)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AccountIcon(
            iconBgColor = iconBgColor,
            iconName = iconName
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = MizanTheme.premium.text.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    color = MizanTheme.premium.text.muted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatBalance(balance),
                color = MizanTheme.premium.text.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            MizanBadge(
                text = currencyCode,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MizanTheme.premium.text.muted,
            modifier = Modifier.size(20.dp)
        )
    }
}
*/

@Composable
private fun AccountIcon(
    iconBgColor: Color,
    iconName: String?
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(iconBgColor.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(iconBgColor)
        )
    }
}

private fun formatBalance(amount: BigDecimal): String {
    val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
        isGroupingUsed = true
    }
    return formatter.format(amount.toDouble())
}

private fun parseHexColor(hex: String?): Color {
    if (hex == null) return Color(0xFF4F8EF7)
    return try {
        val clean = hex.trimStart('#')
        val colorLong = when (clean.length) {
            6 -> "FF$clean".toLong(16)
            8 -> clean.toLong(16)
            else -> return Color(0xFF4F8EF7)
        }
        Color(colorLong.toInt())
    } catch (e: Exception) {
        Color(0xFF4F8EF7)
    }
}

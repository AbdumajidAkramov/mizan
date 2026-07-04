package dev.esbi.mizan.ui.kit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.PremiumColors

enum class BadgeVariant {
    DEFAULT,
    SUCCESS,
    WARNING,
    ERROR,
    INFO,
    OUTLINE
}

@Composable
fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.DEFAULT
) {
    val (backgroundColor, textColor) = when (variant) {
        BadgeVariant.DEFAULT -> Color(0xFF667EEA) to Color.White
        BadgeVariant.SUCCESS -> Color(0xFF00F2A0) to Color.White
        BadgeVariant.WARNING -> Color(0xFFFFA34D) to Color.White
        BadgeVariant.ERROR -> Color(0xFFFF6B6B) to Color.White
        BadgeVariant.INFO -> Color(0xFF4FACFE) to Color.White
        BadgeVariant.OUTLINE -> Color.Transparent to PremiumColors.TextPrimary
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Preview
@Composable
fun BadgePreview() {
    MizanTheme {
        Badge(
            modifier = Modifier,
            text = "New",
            variant = BadgeVariant.WARNING
        )
    }
}

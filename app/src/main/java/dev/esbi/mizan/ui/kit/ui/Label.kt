package dev.esbi.mizan.ui.kit.ui

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.PremiumColors

@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
    required: Boolean = false
) {
    Text(
        text = if (required) "$text *" else text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = PremiumColors.TextSecondary,
        modifier = modifier
    )
}

@Composable
fun FormLabel(
    text: String,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    description: String? = null
) {
    androidx.compose.foundation.layout.Column(modifier = modifier) {
        Label(text = text, required = required)

        if (description != null) {
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = PremiumColors.TextMuted
            )
        }
    }
}

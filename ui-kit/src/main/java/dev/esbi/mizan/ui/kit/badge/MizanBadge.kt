package dev.esbi.mizan.ui.kit.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun MizanBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MizanTheme.premium.background.tertiary,
    textColor: Color = MizanTheme.premium.text.secondary
) {
    Text(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp
    )
}

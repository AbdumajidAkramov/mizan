package dev.esbi.mizan.ui.kit.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.PremiumColors

@Composable
fun Toggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true
) {
    val offsetX by animateFloatAsState(
        targetValue = if (checked) 24f else 2f,
        animationSpec = tween(durationMillis = 200),
        label = "toggle_offset"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF667EEA) else PremiumColors.Surface3,
        animationSpec = tween(durationMillis = 200),
        label = "toggle_bg"
    )
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label != null) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = if (enabled) PremiumColors.TextPrimary else PremiumColors.TextMuted,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        
        Box(
            modifier = Modifier
                .size(width = 48.dp, height = 24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .clickable(enabled = enabled) { onCheckedChange(!checked) }
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = offsetX.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

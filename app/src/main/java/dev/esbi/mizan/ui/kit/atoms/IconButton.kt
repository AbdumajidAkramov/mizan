package dev.esbi.mizan.ui.kit.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.PremiumColors

enum class IconButtonSize(val containerSize: Int, val iconSize: Int) {
    SMALL(32, 16),
    MEDIUM(40, 20),
    LARGE(48, 24)
}

enum class IconButtonVariant {
    FILLED,
    OUTLINED,
    STANDARD
}

@Composable
fun KitIconButton(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IconButtonSize = IconButtonSize.MEDIUM,
    variant: IconButtonVariant = IconButtonVariant.STANDARD,
    enabled: Boolean = true,
    contentDescription: String? = null
) {
    val backgroundColor = when (variant) {
        IconButtonVariant.FILLED -> Color(0xFF667EEA)
        IconButtonVariant.OUTLINED -> Color.Transparent
        IconButtonVariant.STANDARD -> PremiumColors.Surface2
    }
    
    val iconColor = when (variant) {
        IconButtonVariant.FILLED -> Color.White
        IconButtonVariant.OUTLINED -> Color(0xFF667EEA)
        IconButtonVariant.STANDARD -> PremiumColors.TextSecondary
    }
    
    Box(
        modifier = modifier
            .size(size.containerSize.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = if (enabled) iconColor else iconColor.copy(alpha = 0.4f),
            modifier = Modifier.size(size.iconSize.dp)
        )
    }
}

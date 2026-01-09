package dev.esbi.mizan.ui.kit.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.animation.pressScale

enum class PremiumButtonVariant {
    GRADIENT_PRIMARY,
    GRADIENT_SECONDARY,
    GLASS,
    OUTLINE,
    GHOST
}

enum class PremiumButtonSize {
    SM,
    MD,
    LG,
    XL
}

@Composable
fun PremiumButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PremiumButtonVariant = PremiumButtonVariant.GRADIENT_PRIMARY,
    size: PremiumButtonSize = PremiumButtonSize.MD,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    val padding = when (size) {
        PremiumButtonSize.SM -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        PremiumButtonSize.MD -> PaddingValues(horizontal = 20.dp, vertical = 12.dp)
        PremiumButtonSize.LG -> PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        PremiumButtonSize.XL -> PaddingValues(horizontal = 32.dp, vertical = 20.dp)
    }
    
    val shape = RoundedCornerShape(12.dp)
    
    Box(
        modifier = modifier
            .clip(shape)
            .then(
                if (enabled) {
                    Modifier
                        .pressScale(interactionSource = interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                } else {
                    Modifier
                }
            )
            .background(
                when (variant) {
                    PremiumButtonVariant.GRADIENT_PRIMARY -> Brush.horizontalGradient(
                        colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                    )
                    PremiumButtonVariant.GRADIENT_SECONDARY -> Brush.horizontalGradient(
                        colors = listOf(Color(0xFF4FACFE), Color(0xFF00F2FE))
                    )
                    PremiumButtonVariant.GLASS -> Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        )
                    )
                    PremiumButtonVariant.OUTLINE -> Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Transparent)
                    )
                    PremiumButtonVariant.GHOST -> Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Transparent)
                    )
                }
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

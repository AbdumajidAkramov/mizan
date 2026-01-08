package dev.esbi.mizan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.animation.pressScale

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    variant: PremiumCardVariant = PremiumCardVariant.Glass,
    onClick: (() -> Unit)? = null,
    enableInteraction: Boolean = true,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(20.dp)
    
    Box(
        modifier = modifier
            .clip(shape)
            .then(
                if (onClick != null && enableInteraction) {
                    Modifier
                        .pressScale(interactionSource = interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                } else if (enableInteraction) {
                    Modifier.pressScale(interactionSource = interactionSource)
                } else {
                    Modifier
                }
            )
            .then(
                when (variant) {
                    PremiumCardVariant.Glass -> Modifier
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            shape = shape
                        )
                    PremiumCardVariant.Gradient -> Modifier
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF667EEA),
                                    Color(0xFF764BA2)
                                )
                            )
                        )
                    PremiumCardVariant.Solid -> Modifier
                        .background(MaterialTheme.colorScheme.surface)
                }
            )
    ) {
        content()
    }
}

enum class PremiumCardVariant {
    Glass,
    Gradient,
    Solid
}

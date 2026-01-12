package dev.esbi.mizan.ui.kit.glass

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: CornerBasedShape = RoundedCornerShape(MizanTheme.premium.radius.lg),
    content: @Composable () -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val elevation by animateDpAsState(
        targetValue = if (isHovered) 24.dp else 16.dp,
        label = "glass-elevation"
    )

    val translateY by animateDpAsState(
        targetValue = if (isHovered) (-2).dp else 0.dp,
        label = "glass-translate"
    )

    val bgAlpha by animateFloatAsState(
        targetValue = if (isHovered) 0.08f else 0.05f,
        label = "glass-bg-alpha"
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = cornerRadius,
                ambientColor = Color.Black.copy(alpha = 0.12f),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .blur(20.dp) // backdrop-filter analog (Android 12+)
            .background(
                color = Color.White.copy(alpha = bgAlpha),
                shape = cornerRadius
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.10f),
                shape = cornerRadius
            )
            .padding(16.dp)
            .hoverable(interactionSource)
            .graphicsLayer {
                translationY = translateY.toPx()
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        content()
    }
}

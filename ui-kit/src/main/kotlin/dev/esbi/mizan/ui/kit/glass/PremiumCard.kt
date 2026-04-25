package dev.esbi.mizan.ui.kit.glass

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow

enum class CardVariant { Glass, Solid, Gradient }

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    variant: CardVariant = CardVariant.Glass,
    hover: Boolean = false,
    glow: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isInteractive = onClick != null || hover

    // Animatsiya
    val translationY by animateDpAsState(
        targetValue = if (isInteractive && isPressed) (-4).dp else 0.dp,
        label = "transY"
    )

    // Design Systemdan radiusni olish
    val shape = RoundedCornerShape(MizanTheme.premium.radius.xl)

    // Ranglarni tanlash
    val backgroundModifier = when (variant) {
        CardVariant.Glass -> Modifier
            .background(MizanTheme.premium.glass.bg)
//            .border(1.dp, MizanTheme.premium.glass.border, shape)

        CardVariant.Solid -> Modifier
            .background(MizanTheme.premium.colors.surface3)
//            .border(1.dp, MizanTheme.premium.colors.surface4, shape)

        CardVariant.Gradient -> Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MizanTheme.premium.colors.surface3,
                        MizanTheme.premium.colors.surface2
                    )
                )
            )
//            .border(1.dp, MizanTheme.premium.glass.border, shape)
    }

    // Soyani sozlash (Design Systemdagi ShadowInfo dan foydalanamiz)
    val shadowInfo = if (glow && isInteractive && isPressed) {
        MizanTheme.premium.shadows.glowPrimary
    } else {
        if (variant == CardVariant.Glass) MizanTheme.premium.shadows.sm else MizanTheme.premium.shadows.md
    }

    val shadowModifier = Modifier.shadow(
        elevation = shadowInfo.blurRadius,
        shape = shape,
        spotColor = shadowInfo.color,
        ambientColor = shadowInfo.color
    )
    PressCard(modifier, onClick) {
        Box(
            modifier = Modifier
                .premiumShadow(MizanTheme.premium.shadows.md)
                .graphicsLayer {
                    this.translationY = translationY.toPx()
                }
                .then(shadowModifier)
                .clip(shape)
                .then(backgroundModifier)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else Modifier
                ),
            content = content
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true
)
@Composable
private fun PremiumCardPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme {
        PremiumCard(
            variant = CardVariant.Glass,
            onClick = {},
            glow = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Text(
                text = "Mizan Premium",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun PressCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, tween(100), label = "scale")
    Box(
        modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                    onTap = { onClick?.invoke() })
            }) { content() }
}

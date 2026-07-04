package dev.esbi.mizan.design.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Press scale animation matching design active:scale-95
 * Scales element to 0.95 when pressed
 * Duration: 200ms
 */
fun Modifier.pressScale(
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true
): Modifier = composed {
    if (!enabled) return@composed this

    val source = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) PremiumAnimationSpec.PRESS_SCALE else 1f,
        animationSpec = PremiumAnimationSpec.pressSpec(),
        label = "pressScale"
    )

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/**
 * Hover elevation animation matching design hover:-translate-y-1
 * Translates element up by 4dp on hover (simulated via press for mobile)
 * Duration: 200ms
 */
fun Modifier.hoverElevation(
    enabled: Boolean = true
): Modifier = composed {
    if (!enabled) return@composed this

    var isHovered by remember { mutableStateOf(false) }

    val translationY by animateFloatAsState(
        targetValue = if (isHovered) PremiumAnimationSpec.HOVER_ELEVATION.toFloat() else 0f,
        animationSpec = PremiumAnimationSpec.hoverSpec(),
        label = "hoverElevation"
    )

    this
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isHovered = true
                    tryAwaitRelease()
                    isHovered = false
                }
            )
        }
        .graphicsLayer {
            this.translationY = translationY
        }
}

/**
 * Combined press and hover effect for cards
 * Matches design: hover:-translate-y-1 active:scale-95
 */
fun Modifier.cardInteraction(
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true
): Modifier = composed {
    if (!enabled) return@composed this

    val source = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) PremiumAnimationSpec.PRESS_SCALE else 1f,
        animationSpec = PremiumAnimationSpec.pressSpec(),
        label = "cardScale"
    )

    val translationY by animateFloatAsState(
        targetValue = if (isPressed) 0f else PremiumAnimationSpec.HOVER_ELEVATION.toFloat(),
        animationSpec = PremiumAnimationSpec.hoverSpec(),
        label = "cardTranslation"
    )

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.translationY = translationY
    }
}

/**
 * Animated progress bar matching design transition-all duration-500
 * Animates width changes smoothly
 */
@Composable
fun animateProgressAsState(
    targetProgress: Float
): State<Float> {
    return animateFloatAsState(
        targetValue = targetProgress.coerceIn(0f, 1f),
        animationSpec = PremiumAnimationSpec.progressSpec(),
        label = "progress"
    )
}

/**
 * Animated rotation for needles/gauges
 * Duration: 1000ms, Easing: ease-out
 */
@Composable
fun animateRotationAsState(
    targetRotation: Float
): State<Float> {
    return animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = PremiumAnimationSpec.needleSpec(),
        label = "rotation"
    )
}

/**
 * Shimmer effect for loading skeletons
 * Matches design shimmer animation
 */
fun Modifier.shimmerEffect(
    enabled: Boolean = true
): Modifier = composed {
    if (!enabled) return@composed this

    // Shimmer is already implemented in LoadingSkeleton.kt
    // This is a placeholder for additional shimmer effects
    this
}

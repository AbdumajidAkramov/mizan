package dev.esbi.mizan.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * Entrance animation matching CSS fadeInUp
 * From: opacity 0, translateY(20px)
 * To: opacity 1, translateY(0)
 * Duration: 500ms, Easing: ease-out
 */
fun fadeInUpEnter(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.FADE_IN_UP_DURATION,
            easing = PremiumAnimationSpec.EaseOut
        )
    ) + slideInVertically(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.FADE_IN_UP_DURATION,
            easing = PremiumAnimationSpec.EaseOut
        ),
        initialOffsetY = { PremiumAnimationSpec.FADE_IN_UP_OFFSET }
    )
}

fun fadeInUpExit(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.FADE_IN_UP_DURATION / 2,
            easing = PremiumAnimationSpec.EaseOut
        )
    ) + slideOutVertically(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.FADE_IN_UP_DURATION / 2,
            easing = PremiumAnimationSpec.EaseOut
        ),
        targetOffsetY = { -PremiumAnimationSpec.FADE_IN_UP_OFFSET }
    )
}

/**
 * Entrance animation matching CSS slideInRight
 * From: opacity 0, translateX(20px)
 * To: opacity 1, translateX(0)
 * Duration: 400ms, Easing: ease-out
 */
fun slideInRightEnter(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SLIDE_IN_RIGHT_DURATION,
            easing = PremiumAnimationSpec.EaseOut
        )
    ) + slideInHorizontally(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SLIDE_IN_RIGHT_DURATION,
            easing = PremiumAnimationSpec.EaseOut
        ),
        initialOffsetX = { PremiumAnimationSpec.SLIDE_IN_RIGHT_OFFSET }
    )
}

fun slideInRightExit(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SLIDE_IN_RIGHT_DURATION / 2,
            easing = PremiumAnimationSpec.EaseOut
        )
    ) + slideOutHorizontally(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SLIDE_IN_RIGHT_DURATION / 2,
            easing = PremiumAnimationSpec.EaseOut
        ),
        targetOffsetX = { PremiumAnimationSpec.SLIDE_IN_RIGHT_OFFSET }
    )
}

/**
 * Entrance animation matching CSS scaleIn
 * From: opacity 0, scale(0.95)
 * To: opacity 1, scale(1)
 * Duration: 300ms, Easing: ease-out
 */
fun scaleInEnter(): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SCALE_IN_DURATION,
            easing = PremiumAnimationSpec.EaseOut
        )
    ) + scaleIn(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SCALE_IN_DURATION,
            easing = PremiumAnimationSpec.EaseOut
        ),
        initialScale = PremiumAnimationSpec.SCALE_IN_FROM
    )
}

fun scaleInExit(): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SCALE_IN_DURATION / 2,
            easing = PremiumAnimationSpec.EaseOut
        )
    ) + scaleOut(
        animationSpec = tween(
            durationMillis = PremiumAnimationSpec.SCALE_IN_DURATION / 2,
            easing = PremiumAnimationSpec.EaseOut
        ),
        targetScale = PremiumAnimationSpec.SCALE_IN_FROM
    )
}

/**
 * Composable wrapper for fadeInUp animation
 */
@Composable
fun FadeInUpAnimation(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeInUpEnter(),
        exit = fadeInUpExit(),
        modifier = modifier,
        content = content
    )
}

/**
 * Composable wrapper for slideInRight animation
 */
@Composable
fun SlideInRightAnimation(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInRightEnter(),
        exit = slideInRightExit(),
        modifier = modifier,
        content = content
    )
}

/**
 * Composable wrapper for scaleIn animation
 */
@Composable
fun ScaleInAnimation(
    visible: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleInEnter(),
        exit = scaleInExit(),
        modifier = modifier,
        content = content
    )
}

/**
 * Staggered entrance animation for list items
 * Each item appears with a delay based on its index
 */
@Composable
fun StaggeredFadeInUp(
    index: Int,
    delayMillis: Int = PremiumAnimationSpec.STAGGER_DELAY,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay((index * delayMillis).toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeInUpEnter(),
        exit = fadeInUpExit(),
        modifier = modifier,
        content = content
    )
}

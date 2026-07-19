package dev.esbi.mizan.design.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

/**
 * Premium Animation Specifications
 * Matches design/src/styles/premium-theme.css animation timing
 */
object PremiumAnimationSpec {

    /**
     * Easing curves matching CSS ease-out
     * CSS ease-out ≈ cubic-bezier(0, 0, 0.58, 1)
     */
    val EaseOut = CubicBezierEasing(0f, 0f, 0.58f, 1f)

    /**
     * Standard easing for most transitions
     */
    val Standard = FastOutSlowInEasing

    /**
     * fadeInUp animation - Main screen entrance
     * Duration: 500ms, Easing: ease-out
     * Transform: translateY(20px) -> 0
     */
    const val FADE_IN_UP_DURATION = 500
    const val FADE_IN_UP_OFFSET = 20

    fun <T> fadeInUpSpec(): AnimationSpec<T> = tween(
        durationMillis = FADE_IN_UP_DURATION,
        easing = EaseOut
    )

    /**
     * slideInRight animation - Side panel entrance
     * Duration: 400ms, Easing: ease-out
     * Transform: translateX(20px) -> 0
     */
    const val SLIDE_IN_RIGHT_DURATION = 400
    const val SLIDE_IN_RIGHT_OFFSET = 20

    fun <T> slideInRightSpec(): AnimationSpec<T> = tween(
        durationMillis = SLIDE_IN_RIGHT_DURATION,
        easing = EaseOut
    )

    /**
     * scaleIn animation - Modal/dialog entrance
     * Duration: 300ms, Easing: ease-out
     * Transform: scale(0.95) -> 1
     */
    const val SCALE_IN_DURATION = 300
    const val SCALE_IN_FROM = 0.95f

    fun <T> scaleInSpec(): AnimationSpec<T> = tween(
        durationMillis = SCALE_IN_DURATION,
        easing = EaseOut
    )

    /**
     * Progress bar animation
     * Duration: 500ms, Easing: ease-out
     */
    const val PROGRESS_DURATION = 500

    fun <T> progressSpec(): AnimationSpec<T> = tween(
        durationMillis = PROGRESS_DURATION,
        easing = EaseOut
    )

    /**
     * Button press animation
     * Duration: 200ms, Scale: 0.95
     */
    const val PRESS_DURATION = 200
    const val PRESS_SCALE = 0.95f

    fun <T> pressSpec(): AnimationSpec<T> = tween(
        durationMillis = PRESS_DURATION,
        easing = Standard
    )

    /**
     * Card hover animation
     * Duration: 200ms, translateY: -4dp
     */
    const val HOVER_DURATION = 200
    const val HOVER_ELEVATION = -4

    fun <T> hoverSpec(): AnimationSpec<T> = tween(
        durationMillis = HOVER_DURATION,
        easing = Standard
    )

    /**
     * Toggle/switch animation
     * Duration: 300ms
     */
    const val TOGGLE_DURATION = 300

    fun <T> toggleSpec(): AnimationSpec<T> = tween(
        durationMillis = TOGGLE_DURATION,
        easing = Standard
    )

    /**
     * Health score needle animation
     * Duration: 1000ms, Easing: ease-out
     */
    const val NEEDLE_DURATION = 1000

    fun <T> needleSpec(): AnimationSpec<T> = tween(
        durationMillis = NEEDLE_DURATION,
        easing = EaseOut
    )

    /**
     * Stagger delay for sequential card entrance
     * Each card delayed by 80ms (calculated from 500ms / 6 cards ≈ 80ms)
     */
    const val STAGGER_DELAY = 80

    /**
     * Calculate stagger delay for index
     */
    fun staggerDelay(index: Int): Int = index * STAGGER_DELAY
}

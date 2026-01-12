package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun AnimSection(visible: Boolean, delayMs: Int = 50, content: @Composable () -> Unit) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (visible) {
            delay(delayMs.toLong()); show = true
        }
    }
    AnimatedVisibility(
        show,
        enter = fadeIn(tween(100)) + slideInVertically(tween(100)) { it / 4 }) { content() }
}

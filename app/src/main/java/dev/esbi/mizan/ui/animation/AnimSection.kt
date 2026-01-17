package dev.esbi.mizan.ui.animation

import androidx.compose.runtime.Composable

@Composable
fun AnimSection(visible: Boolean, delayMs: Int = 50, content: @Composable () -> Unit) {
    content()
//    var show by remember { mutableStateOf(false) }
//    LaunchedEffect(visible) {
//        if (visible) {
//            delay(delayMs.toLong()); show = true
//        }
//    }
//    AnimatedVisibility(
//        show,
//        enter = fadeIn(tween(100)) + slideInVertically(tween(100)) { it / 4 }) { content() }
}

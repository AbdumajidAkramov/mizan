package dev.esbi.mizan.ui.kit.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: CornerBasedShape = RoundedCornerShape(MizanTheme.premium.radius.lg),
    content: @Composable () -> Unit
) {
    Box(
        Modifier
            .clip(cornerRadius)
            .background(MizanTheme.premium.background.secondary.copy(0.95f))
//            .border(
//                1.dp,
//                MizanTheme.premium.glass.border,
//                cornerRadius
//            )
            .then(modifier)
    ) { content() }
}

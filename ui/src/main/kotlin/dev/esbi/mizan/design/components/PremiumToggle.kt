package dev.esbi.mizan.design.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun PremiumToggle(
    isChecked: Boolean,
    checkedIcon: @Composable () -> Unit,
    uncheckedIcon: @Composable () -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offsetX by animateFloatAsState(
        targetValue = if (isChecked) 30f else 2f,
        animationSpec = tween(durationMillis = 300),
        label = "theme_toggle_offset"
    )

    val backgroundBrush = if (isChecked) {
        Brush.horizontalGradient(
            colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(Color(0xFFFA709A), Color(0xFFFEE140))
        )
    }

    Box(
        modifier = modifier
            .size(width = 56.dp, height = 28.dp)
            .clip(CircleShape)
            .background(backgroundBrush)
            .clickable(onClick = onToggle),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX.dp)
                .size(24.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                checkedIcon()
            } else {
                uncheckedIcon()
            }
        }
    }
}

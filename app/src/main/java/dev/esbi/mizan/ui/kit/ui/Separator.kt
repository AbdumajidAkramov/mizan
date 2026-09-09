package dev.esbi.mizan.ui.kit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.PremiumColors

@Composable
fun Separator(
    modifier: Modifier = Modifier,
    color: Color = PremiumColors.Surface3,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}

@Composable
fun VerticalSeparator(
    modifier: Modifier = Modifier,
    color: Color = PremiumColors.Surface3,
    thickness: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .width(thickness)
            .background(color)
    )
}

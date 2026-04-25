package dev.esbi.mizan.ui.kit.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.Pink
import dev.esbi.mizan.ui.theme.Purple
import dev.esbi.mizan.ui.theme.Purple2

@Composable
fun GradientColorCard(
    modifier: Modifier = Modifier,
    brush: Brush = Brush.linearGradient(listOf(Purple, Purple2, Pink)),
    cornerShape: Shape = RoundedCornerShape(24.dp),
    topRightCircleRadius: Dp = 150.dp,
    bottomStartCircleRadius: Dp = 250.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cornerShape)
            .background(brush)
    ) {
        // Background gradient orbs
        Box(
            modifier = Modifier
                .size(topRightCircleRadius)
                .offset(x = 250.dp, y = (-20).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f))
                .blur(50.dp)
        )

        Box(
            modifier = Modifier
                .size(bottomStartCircleRadius)
                .offset(x = (-30).dp, y = 100.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.1f))
                .blur(40.dp)
        )

        content()
    }
}

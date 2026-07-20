package dev.esbi.mizan.features.dashboard.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.Cyan
import dev.esbi.mizan.design.theme.MizanTheme
import dev.esbi.mizan.design.theme.Red

@Composable
fun MiniBarChart() {
    val data = listOf(
        0.3f to 0.5f,
        0.4f to 0.6f,
        0.7f to 0.3f,
        0.8f to 0.4f,
        0.9f to 0.4f,
    )
    Row(
        modifier = Modifier.fillMaxWidth(), Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { (inc, exp) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    Modifier
                        .width(20.dp)
                        .height((inc * 60).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Cyan)
                )
                Box(
                    Modifier
                        .width(20.dp)
                        .height((exp * 60).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Red)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MiniBarChartPreview() {
    MizanTheme {
        MiniBarChart()
    }
}

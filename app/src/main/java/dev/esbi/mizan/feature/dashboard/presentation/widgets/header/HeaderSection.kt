package dev.esbi.mizan.feature.dashboard.presentation.widgets.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.dashboard.presentation.ui.Purple
import dev.esbi.mizan.feature.dashboard.presentation.ui.Purple2
import dev.esbi.mizan.feature.dashboard.presentation.ui.TextGray
import dev.esbi.mizan.feature.dashboard.presentation.ui.TextWhite
import dev.esbi.mizan.ui.theme.MizanTheme


@Composable
fun HeaderSection() {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Column {
            Text(
                "Welcome back,",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("John Doe", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Text(" 👋", fontSize = 28.sp)
            }
        }
        Box(
            Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Purple, Purple2))), Alignment.Center
        ) {
            Text("JD", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun HeaderSectionPreview() {
    MizanTheme {
        HeaderSection()
    }
}

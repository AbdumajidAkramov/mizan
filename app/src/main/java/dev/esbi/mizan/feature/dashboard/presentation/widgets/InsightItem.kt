package dev.esbi.mizan.feature.dashboard.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.TextMuted
import dev.esbi.mizan.ui.theme.TextWhite

@Composable
fun InsightItem(icon: String, title: String, sub: String, hl: String?, hlCol: Color) {
    GlassCard(Modifier.width(280.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(icon, fontSize = 24.sp)
                if (hl != null) Text(
                    hl,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = hlCol
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold, color = TextWhite)
            Spacer(Modifier.height(4.dp))
            Text(sub, style = MaterialTheme.typography.bodySmall, color = TextMuted, maxLines = 2)
        }
    }
}

package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.colors.MizanTheme

@Composable
internal fun HeaderSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("✨", fontSize = 28.sp)
            Spacer(Modifier.width(MizanTheme.premium.spacing.sm))
            Text(
                "Financial Mirror",
                style = MizanTheme.premium.typography.headingXl,
                color = MizanTheme.premium.text.primary
            )
        }
        Spacer(Modifier.height(MizanTheme.premium.spacing.xs))
        Text(
            "AI-powered insights into your financial future",
            style = MizanTheme.premium.typography.bodyMd,
            color = MizanTheme.premium.text.tertiary
        )
    }
}

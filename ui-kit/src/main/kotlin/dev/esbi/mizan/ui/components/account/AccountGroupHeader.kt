package dev.esbi.mizan.ui.components.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun AccountGroupHeader(
    title: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MizanTheme.premium.text.primary.copy(alpha = 0.9f),
            style = MizanTheme.typography.headingSm,
        )
        Text(
            text = "$count ${if (count == 1) "account" else "accounts"}",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.primary.copy(alpha = 0.9f),
        )
    }
}

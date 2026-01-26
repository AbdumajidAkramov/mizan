package dev.esbi.mizan.feature.newtransaction.transactiontype

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

@Composable
fun TransactionTypeContent(
    amount: String,
    onTypeSelect: (Transaction.Type) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))
        Text(
            "Amount",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )
        Text(
            amount.annotatedString(),
            style = MizanTheme.typography.displayMd,
            color = MizanTheme.premium.text.primary
        )
        Spacer(Modifier.weight(1f))
        Text(
            "What type of transaction?",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )

        Spacer(Modifier.height(32.dp))

        Column(modifier = Modifier) {
            TransactionType.entries.forEach { type ->
                TransactionTypeItem(
                    type = type,
                    onSelect = {
                        onTypeSelect(type)
                    }
                )
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
internal fun TransactionTypeItem(
    type: Transaction.Type,
    onSelect: () -> Unit
) {
    val config = getTypeConfig(type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .background(MizanTheme.premium.glass.bg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Ripple yo'q (Reactdagi kabi minimalist)
            ) { onSelect() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = config.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            MizanIcon(
                icon = config.icon,
                modifier = Modifier.size(28.dp),
                tint = config.color
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                config.label,
                style = MizanTheme.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
            Text(
                config.caption,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }
        Spacer(Modifier.weight(1f))
        MizanIcon(
            icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_chevron_right),
            modifier = Modifier,
            tint = MizanTheme.premium.text.muted
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF1A1A2E
)
@Composable
fun TransactionTypeStepPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme(darkTheme = true) {
        TransactionTypeContent(
            amount = "121233.98",
            onTypeSelect = {}
        )
    }
}

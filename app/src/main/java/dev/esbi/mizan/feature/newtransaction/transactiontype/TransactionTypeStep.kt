package dev.esbi.mizan.feature.newtransaction.transactiontype

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionIntent
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionState
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun TransactionTypeStep(
    state: NewTransactionState,
    onIntent: (NewTransactionIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Amount Summary
        AmountSummary(
            amount = state.displayValue
        )
        
        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))
        
        // Title
        Text(
            text = "What type of transaction?",
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.tertiary,
            modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.md)
        )
        
        // Transaction Type Options
        TransactionTypeOptions(
            onSelectType = { type -> onIntent(NewTransactionIntent.OnTransactionTypeSelect(type)) }
        )
    }
}

@Composable
private fun AmountSummary(
    amount: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Amount",
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary
        )
        
        Text(
            text = "$$amount",
            style = MizanTheme.typography.bodyLg.copy(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MizanTheme.premium.text.primary
        )
    }
}

@Composable
private fun TransactionTypeOptions(
    onSelectType: (TransactionType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MizanTheme.premium.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        TransactionTypeOption(
            type = TransactionType.Expense,
            icon = Icons.Default.KeyboardArrowUp,
            title = "Expense",
            description = "Money spent on purchases",
            color = Color(0xFFF5576C),
            onSelect = { onSelectType(TransactionType.Expense) }
        )
        
        TransactionTypeOption(
            type = TransactionType.Income,
            icon = Icons.Default.KeyboardArrowDown,
            title = "Income",
            description = "Money received",
            color = Color(0xFF4FACFE),
            onSelect = { onSelectType(TransactionType.Income) }
        )
        
        TransactionTypeOption(
            type = TransactionType.Transfer,
            icon = Icons.Default.Refresh,
            title = "Transfer",
            description = "Move money between accounts",
            color = MizanTheme.premium.colors.emerald,
            onSelect = { onSelectType(TransactionType.Transfer) }
        )
    }
}

@Composable
private fun TransactionTypeOption(
    type: TransactionType,
    icon: ImageVector,
    title: String,
    description: String,
    color: Color,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MizanTheme.premium.colors.surface2)
            .clickable { onSelect() }
            .padding(MizanTheme.premium.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
        
        // Text Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MizanTheme.typography.bodyLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = description,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
        }
        
        // Chevron
        Icon(
            icon = IconValue("chevron_right"),
            modifier = Modifier.size(20.dp),
            tint = MizanTheme.premium.text.muted
        )
    }
}

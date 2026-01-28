package dev.esbi.mizan.feature.newtransaction.confirm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.confirm.state.ConfirmTransactionUiState
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmTransactionContent(
    state: ConfirmTransactionUiState,
    onNoteChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
            .verticalScroll(rememberScrollState())
    ) {
        // Amount Display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${state.amount} ${state.currencyCode}",
                style = MizanTheme.premium.typography.displaySm,
                color = when (state.transactionType) {
                    Transaction.Type.INCOME -> MizanTheme.premium.colors.success
                    Transaction.Type.EXPENSE -> MizanTheme.premium.colors.error
                    Transaction.Type.TRANSFER -> MizanTheme.premium.colors.primary
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xxl))

        // Details Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.md)
        ) {
            // Category Row
            if (state.categoryName != null) {
                DetailRow(
                    icon = Icons.ic_add, // TODO: Use proper category icon
                    title = "Category",
                    value = state.categoryName,
                    onClick = null
                )
                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))
            }

            // Account Row
            DetailRow(
                icon = Icons.ic_wallet,
                title = "Account",
                value = state.accountName,
                onClick = null
            )

            // To Account Row (for transfers)
            if (state.toAccountName != null) {
                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))
                DetailRow(
                    icon = Icons.ic_wallet,
                    title = "To Account",
                    value = state.toAccountName,
                    onClick = null
                )
            }

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

            // Date Row
            DetailRow(
                icon = Icons.ic_calendar_month,
                title = "Date",
                value = formatDate(state.date),
                onClick = onDateClick
            )
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))

        // Note Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.md)
        ) {
            Text(
                text = "Note",
                style = MizanTheme.premium.typography.labelMd,
                color = MizanTheme.premium.text.secondary,
                modifier = Modifier.padding(bottom = MizanTheme.premium.spacing.sm)
            )

            OutlinedTextField(
                value = state.note,
                onValueChange = onNoteChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Add a note...",
                        style = MizanTheme.premium.typography.bodyMd,
                        color = MizanTheme.premium.text.tertiary
                    )
                },
                shape = RoundedCornerShape(MizanTheme.premium.radius.md),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MizanTheme.premium.colors.surface1,
                    focusedContainerColor = MizanTheme.premium.colors.surface1,
                    unfocusedBorderColor = MizanTheme.premium.colors.surface4,
                    focusedBorderColor = MizanTheme.premium.colors.primary,
                    unfocusedTextColor = MizanTheme.premium.text.primary,
                    focusedTextColor = MizanTheme.premium.text.primary
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        Button(
            onClick = onConfirmClick,
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.md)
                .height(56.dp),
            shape = RoundedCornerShape(MizanTheme.premium.radius.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = when (state.transactionType) {
                    Transaction.Type.INCOME -> MizanTheme.premium.colors.success
                    Transaction.Type.EXPENSE -> MizanTheme.premium.colors.error
                    Transaction.Type.TRANSFER -> MizanTheme.premium.colors.primary
                }
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Save Transaction",
                    style = MizanTheme.premium.typography.labelLg,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))
    }
}

@Composable
private fun DetailRow(
    icon: Int,
    title: String,
    value: String,
    onClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.md))
            .background(MizanTheme.premium.colors.surface1)
            .let { modifier ->
                if (onClick != null) {
                    modifier.clickable { onClick() }
                } else {
                    modifier
                }
            }
            .padding(MizanTheme.premium.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MizanIcon(
            icon = IconValue(icon),
            contentDescription = null,
            tint = MizanTheme.premium.text.secondary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.md))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MizanTheme.premium.typography.bodySm,
                color = MizanTheme.premium.text.secondary
            )
            Text(
                text = value,
                style = MizanTheme.premium.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )
        }

        if (onClick != null) {
            MizanIcon(
                icon = IconValue(Icons.ic_chevron_right),
                contentDescription = null,
                tint = MizanTheme.premium.text.tertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}

package dev.esbi.mizan.feature.newtransaction.confirm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.confirm.state.ConfirmTransactionUiState
import dev.esbi.mizan.feature.premiumaddtransaction.part2.formatMizanAmount
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConfirmTransactionContent(
    state: ConfirmTransactionUiState,
    onNoteChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
    onSaveAsTemplateChange: (Boolean) -> Unit = {}
) {
    val typeColor = when (state.transactionType) {
        Transaction.Type.INCOME -> MizanTheme.premium.colors.emerald
        Transaction.Type.EXPENSE -> Color(0xFFF5576C)
        Transaction.Type.TRANSFER -> MizanTheme.premium.colors.primary
    }
    Scaffold(
        topBar = {
            ConfirmSaveHeader(
                onBack = onBackClick,
                onClose = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MizanTheme.premium.background.primary)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = MizanTheme.premium.spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

            // Main Receipt Card
            ReceiptCard(
                state = state,
                typeColor = typeColor
            )

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            // Date Selector Card
            ActionCard(
                icon = R.drawable.ic_calendar_month,
                text = formatDate(state.date),
                onClick = onDateClick
            )

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            // Note Input Card
            NoteInputCard(
                note = state.note,
                onNoteChange = onNoteChange
            )

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            // Save as Template Card
            SaveAsTemplateCard(
                isChecked = state.saveAsTemplate,
                onCheckedChange = onSaveAsTemplateChange
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))

            // Save Button
            SaveButton(
                isLoading = state.isLoading,
                onClick = onConfirmClick
            )

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))
        }
    }

}

@Composable
private fun ReceiptCard(
    state: ConfirmTransactionUiState,
    typeColor: Color
) {
    val typeName = when (state.transactionType) {
        Transaction.Type.INCOME -> "Income"
        Transaction.Type.EXPENSE -> "Expense"
        Transaction.Type.TRANSFER -> "Transfer"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = typeColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
            ),
        color = MizanTheme.premium.colors.surface2,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.lg)
        ) {
            // Top Row: Type Label + Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Type Label
                Text(
                    text = typeName,
                    style = MizanTheme.typography.bodyMd,
                    color = typeColor,
                    fontWeight = FontWeight.Medium
                )

                // Direction Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(typeColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = when (state.transactionType) {
                                Transaction.Type.INCOME -> R.drawable.ic_trend_up
                                Transaction.Type.EXPENSE -> R.drawable.ic_down_trend
                                Transaction.Type.TRANSFER -> R.drawable.ic_swap_horizontal
                            }
                        ),
                        contentDescription = null,
                        tint = typeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Amount
            Text(
                text = formatMizanAmount(state.amount.value, currency = state.amount.currency),
                style = MizanTheme.premium.typography.displayMd.copy(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MizanTheme.premium.text.primary,
                modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.sm)
            )

            Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

            // Category Section
            if (state.categoryName != null) {
                Text(
                    text = "Category",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )
                Text(
                    text = state.categoryName,
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.Medium
                )
                if (state.subCategoryName != null) {
                    Text(
                        text = state.subCategoryName,
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.secondary
                    )
                }
                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))
            }

            // Account Section
            Text(
                text = "Account",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
            Text(
                text = state.accountName,
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )

            // To Account (for transfers)
            if (state.toAccountName.isNullOrBlank().not()) {
                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = state.toAccountName,
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionCard(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .clickable { onClick() },
        color = MizanTheme.premium.colors.surface2,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MizanTheme.premium.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            MizanIcon(
                icon = IconValue(icon),
                contentDescription = null,
                tint = MizanTheme.premium.text.secondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun NoteInputCard(
    note: String,
    onNoteChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MizanTheme.premium.colors.surface2,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            MizanIcon(
                icon = IconValue(Icons.ic_file),
                contentDescription = null,
                tint = MizanTheme.premium.text.secondary,
                modifier = Modifier.size(24.dp)
            )
            TextField(
                value = note,
                onValueChange = onNoteChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Add a note (optional)",
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.tertiary
                    )
                },
                textStyle = MizanTheme.typography.bodyMd.copy(
                    color = MizanTheme.premium.text.primary
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = MizanTheme.premium.colors.emerald
                ),
                singleLine = true
            )
        }
    }
}

@Composable
private fun SaveAsTemplateCard(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
            .clickable { onCheckedChange(!isChecked) },
        color = MizanTheme.premium.colors.surface2,
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MizanTheme.premium.spacing.md,
                    vertical = MizanTheme.premium.spacing.sm
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
        ) {
            MizanIcon(
                icon = IconValue(Icons.ic_star),
                contentDescription = null,
                tint = MizanTheme.premium.text.secondary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Save as Template",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MizanTheme.premium.colors.emerald,
                    uncheckedThumbColor = MizanTheme.premium.text.tertiary,
                    uncheckedTrackColor = MizanTheme.premium.colors.surface3
                )
            )
        }
    }
}

@Composable
internal fun SaveButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable(enabled = !isLoading) { onClick() },
        color = MizanTheme.premium.colors.emerald,
        shape = RoundedCornerShape(28.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Save Transaction",
                    style = MizanTheme.typography.bodyLg,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfirmSaveHeader(
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Confirm & Save",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                MizanIcon(
                    icon = IconValue(Icons.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.primary
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MizanTheme.premium.colors.surface2)
                    .clickable { onClose() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MizanTheme.premium.background.primary
        )
    )
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}

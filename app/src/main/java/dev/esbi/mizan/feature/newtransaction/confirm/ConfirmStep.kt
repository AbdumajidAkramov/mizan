package dev.esbi.mizan.feature.newtransaction.confirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionData
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionIntent
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionState
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun ConfirmStep(
    state: NewTransactionState,
    onIntent: (NewTransactionIntent) -> Unit,
    onSave: (NewTransactionData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: Implement Confirmation UI
        Text(
            text = "Confirm & Save",
            style = MizanTheme.typography.bodyLg,
            color = MizanTheme.premium.text.primary,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))
        
        Text(
            text = "Amount: $${state.displayValue}",
            style = MizanTheme.typography.bodyLg,
            color = MizanTheme.premium.text.secondary
        )
        
        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xl))
        
        Button(
            onClick = {
                val transactionData = NewTransactionData(
                    amount = state.displayValue.toDoubleOrNull() ?: 0.0,
                    type = state.transactionType ?: dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType.Expense,
                    category = state.categoryId,
                    subcategory = state.subcategoryId,
                    fromAccountId = state.fromAccountId,
                    toAccountId = state.toAccountId,
                    date = state.selectedDate,
                    notes = state.notes.takeIf { it.isNotEmpty() }
                )
                onSave(transactionData)
            },
            enabled = state.canSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MizanTheme.premium.colors.emerald
            )
        ) {
            Text(
                text = "Save Transaction",
                style = MizanTheme.typography.bodyMd,
                fontWeight = FontWeight.Medium,
                color = androidx.compose.ui.graphics.Color.White
            )
        }
    }
}

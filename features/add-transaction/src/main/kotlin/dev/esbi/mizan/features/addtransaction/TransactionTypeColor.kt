package dev.esbi.mizan.features.addtransaction

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.esbi.mizan.domain.model.Transaction

@Composable
fun Transaction.Type.color(): Color = when (this) {
    Transaction.Type.EXPENSE -> Color(0xFFF5576C)
    Transaction.Type.INCOME -> Color(0xFF4FACFE)
    Transaction.Type.TRANSFER -> Color(0xFF10B981)
}

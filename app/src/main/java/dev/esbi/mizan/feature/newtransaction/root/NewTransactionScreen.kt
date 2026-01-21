package dev.esbi.mizan.feature.newtransaction.root

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputStep
import dev.esbi.mizan.feature.newtransaction.choosecategory.ChooseCategoryStep
import dev.esbi.mizan.feature.newtransaction.confirm.ConfirmStep
import dev.esbi.mizan.feature.newtransaction.transfer.TransferStep
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeStep
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun NewTransactionScreen(
    store: NewTransactionStore,
    onClose: () -> Unit,
    onSave: (transaction: NewTransactionData) -> Unit,
    onManageCategories: () -> Unit = {}
) {
    // Simple state observation without lifecycle for now
    val state = store.state
    
    LaunchedEffect(Unit) {
        store.accept(NewTransactionIntent.LoadInitialData)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            NewTransactionHeader(
                currentStep = state.currentStep,
                transactionType = state.transactionType,
                onBack = { store.accept(NewTransactionIntent.NavigateBack) },
                onClose = onClose
            )
            
            // Main Content with Animated Transitions
            AnimatedContent(
                targetState = state.currentStep,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "step_transition"
            ) { step ->
                when (step) {
                    NewTransactionStep.AMOUNT_INPUT -> {
                        AmountInputStep(
                            state = state,
                            onIntent = store::accept
                        )
                    }
                    
                    NewTransactionStep.TRANSACTION_TYPE -> {
                        TransactionTypeStep(
                            state = state,
                            onIntent = store::accept
                        )
                    }
                    
                    NewTransactionStep.DETAILS -> {
                        when (state.transactionType) {
                            dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType.Transfer -> {
                                TransferStep(
                                    state = state,
                                    onIntent = store::accept
                                )
                            }
                            else -> {
                                ChooseCategoryStep(
                                    state = state,
                                    onIntent = store::accept,
                                    onManageCategories = onManageCategories
                                )
                            }
                        }
                    }
                    
                    NewTransactionStep.CONFIRM -> {
                        ConfirmStep(
                            state = state,
                            onIntent = store::accept,
                            onSave = onSave
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewTransactionHeader(
    currentStep: NewTransactionStep,
    transactionType: dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType?,
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.md
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Back Button (show on all steps except first)
            if (currentStep != NewTransactionStep.AMOUNT_INPUT) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            MizanTheme.premium.colors.surface2,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MizanTheme.premium.text.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            // Title
            Text(
                text = when (currentStep) {
                    NewTransactionStep.AMOUNT_INPUT -> "New Transaction"
                    NewTransactionStep.TRANSACTION_TYPE -> "Transaction Type"
                    NewTransactionStep.DETAILS -> when (transactionType) {
                        dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType.Transfer -> "Select Accounts"
                        else -> "Choose Category"
                    }
                    NewTransactionStep.CONFIRM -> "Confirm & Save"
                },
                style = MizanTheme.typography.bodyMd,
                fontWeight = FontWeight.Medium,
                color = MizanTheme.premium.text.secondary
            )
        }
        
        // Close Button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(32.dp)
                .background(
                    MizanTheme.premium.colors.surface2,
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MizanTheme.premium.text.secondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Data class for the final transaction
data class NewTransactionData(
    val amount: Double,
    val type: dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType,
    val category: String? = null,
    val subcategory: String? = null,
    val fromAccountId: String? = null,
    val toAccountId: String? = null,
    val date: java.time.LocalDate,
    val notes: String? = null
)

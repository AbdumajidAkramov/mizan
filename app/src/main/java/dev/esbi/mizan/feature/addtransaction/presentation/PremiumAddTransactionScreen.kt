package dev.esbi.mizan.feature.addtransaction.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionResult
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.steps.ConfirmStep
import dev.esbi.mizan.feature.addtransaction.presentation.steps.DetailsStep
import dev.esbi.mizan.feature.addtransaction.presentation.steps.TransactionTypeStep
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.AmountInputStep
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.time.LocalDate


@Composable
internal fun PremiumAddTransactionScreen(
    viewModel: AddTransactionViewModel,
    onClose: () -> Unit,
    onSave: (TransactionResult) -> Unit
) {
    val state by viewModel.state.collectAsState(initial = AddTransactionStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                else -> Unit
            }
        }
    }


    // State Management
//    var inputMode by remember { mutableStateOf(InputMode.Manual) }
//    var flowState by remember { mutableStateOf(FlowState.Amount) }

    // Data States
    var displayValue by remember { mutableStateOf("0") }
    var transactionType by remember { mutableStateOf(TransactionType.Expense) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var fromAccountId by remember { mutableStateOf<String?>(null) }
    var toAccountId by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var notes by remember { mutableStateOf("") }

    // UI Toggles
    var showDatePicker by remember { mutableStateOf(false) }
    var showNotesInput by remember { mutableStateOf(false) }

    // Calculator Logikasi (Soddalashtirilgan)
    fun handleNumberClick(num: String) {
        if (displayValue == "0") displayValue = num
        else if (displayValue.length < 12) displayValue += num
    }

    Scaffold(
        containerColor = MizanTheme.premium.background.primary,
        topBar = {
            // Header
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (state.flowState != FlowState.Amount) {
                        IconButton(
                            onClick = {
                                viewModel.onIntent(AddTransactionStore.Intent.OnNext())
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MizanTheme.premium.colors.surface2)
                        ) {
                            Icon(
                                icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_chevron_left),
                                modifier = Modifier.size(20.dp),
                                tint = MizanTheme.premium.text.secondary
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = when (state.flowState) {
                            FlowState.Amount -> "New Transaction"
                            FlowState.Type -> "Transaction Type"
                            FlowState.Details -> if (transactionType == TransactionType.Transfer) "Select Accounts" else "Choose Category"
                            FlowState.Confirm -> "Confirm & Save"
                        },
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.secondary
                    )
                }
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MizanTheme.premium.colors.surface2)
                ) {
                    Icon(Icons.Rounded.Close, null, tint = MizanTheme.premium.text.secondary)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
/*
            TransactionTypeSelector(
                selectedType = dev.esbi.mizan.feature.addtransaction.presentation.triple.TransactionType.Expense,
                onTypeSelect = {}
            )
*/
            // ANIMATED CONTENT SWITCHER
            AnimatedContent(
                targetState = state.flowState,
                transitionSpec = {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                },
                label = "FlowAnimation"
            ) { targetFlow ->
                when (targetFlow) {
                    FlowState.Amount -> AmountInputStep(
                        amount = state.amount,
                        inputMode = state.inputMode,
                        onModeChange = {
                            viewModel.onIntent(
                                AddTransactionStore.Intent.OnInputModeChange(
                                    it
                                )
                            )
                        },
                        onNumberClick = {
                            viewModel.onIntent(AddTransactionStore.Intent.OnKeypadClick(it))
                        },
                        onNext = {
                            if ((displayValue.toFloatOrNull() ?: 0f) > 0f) {
                                viewModel.onIntent(AddTransactionStore.Intent.OnNext(FlowState.Type))
                            }
                        }
                    )

                    FlowState.Type -> TransactionTypeStep(
                        amount = displayValue,
                        onTypeSelect = {
                            transactionType = it
                            viewModel.onIntent(AddTransactionStore.Intent.OnNext(FlowState.Details))
                        }
                    )

                    FlowState.Details -> DetailsStep(
                        amount = displayValue,
                        type = transactionType,
                        selectedCategory = selectedCategory,
                        onSelectCategory = {
                            selectedCategory = it
                            viewModel.onIntent(AddTransactionStore.Intent.OnNext(FlowState.Confirm))
                        },
                        fromAccount = fromAccountId,
                        toAccount = toAccountId,
                        onSelectFromAccount = { fromAccountId = it },
                        onSelectToAccount = { toAccountId = it },
                        onNextTransfer = {
                            if (fromAccountId != null && toAccountId != null) {
                                viewModel.onIntent(AddTransactionStore.Intent.OnNext(FlowState.Confirm))
                            }
                        }
                    )

                    FlowState.Confirm -> ConfirmStep(
                        amount = displayValue,
                        type = transactionType,
                        category = selectedCategory,
                        fromAccount = fromAccountId,
                        toAccount = toAccountId,
                        date = selectedDate,
                        notes = notes,
                        onDateChange = { selectedDate = it },
                        onNotesChange = { notes = it },
                        onSave = {
                            onSave(
                                TransactionResult(
                                    amount = displayValue.toDoubleOrNull() ?: 0.0,
                                    type = transactionType,
                                    category = selectedCategory,
                                    fromAccountId = fromAccountId,
                                    toAccountId = toAccountId,
                                    date = selectedDate,
                                    notes = notes
                                )
                            )
                            onClose()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PremiumAddTransactionScreenPreview() {
//    dev.esbi.mizan.ui.theme.MizanTheme() {
//        PremiumAddTransactionScreen(
//            onClose = {},
//            onSave = {}
//        )
//    }
}
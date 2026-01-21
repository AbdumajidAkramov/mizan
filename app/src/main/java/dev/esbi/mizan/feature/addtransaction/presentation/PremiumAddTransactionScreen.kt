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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.steps.ConfirmStep
import dev.esbi.mizan.feature.addtransaction.presentation.steps.DetailsStep
import dev.esbi.mizan.feature.addtransaction.presentation.steps.TransactionTypeStep
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.AmountInputStep
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme


@Composable
internal fun PremiumAddTransactionScreen(
    viewModel: AddTransactionViewModel,
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    val state by viewModel.state.collectAsState(initial = AddTransactionStore.State())
    val accept  = viewModel::onIntent

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is AddTransactionStore.Label.Close -> {
                    onSave()
                }
            }
        }
    }

    // Data States

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
                                viewModel.onIntent(AddTransactionStore.Intent.BackToPrev)
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
                            FlowState.Details -> if (state.type == TransactionType.Transfer) "Select Accounts" else "Choose Category"
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
                selectedType = state.type,
                onTypeSelect = {
                    viewModel.onIntent(AddTransactionStore.Intent.OnTransactionTypeChange(it))
                }
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
                        state = state,
                        accept = viewModel::onIntent,
                    )

                    FlowState.Type -> TransactionTypeStep(
                        amount = state.amountText,
                        onTypeSelect = {
                            viewModel.onIntent(AddTransactionStore.Intent.OnTransactionTypeSelect(it))
                        }
                    )

                    FlowState.Details -> DetailsStep(
                        state = state,
                        accept = viewModel::onIntent
                    )

                    FlowState.Confirm -> ConfirmStep(
                        state = state,
                        accept = viewModel::onIntent,
                    )
                }
            }
        }
    }
}

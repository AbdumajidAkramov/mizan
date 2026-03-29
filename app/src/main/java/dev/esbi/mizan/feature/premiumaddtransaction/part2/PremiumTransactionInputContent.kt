package dev.esbi.mizan.feature.premiumaddtransaction.part2

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.newtransaction.input.TransactionInputState
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeInfo
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeItem
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
internal fun PremiumTransactionInputContent(
    state: NewTransactionStore.State,
    accept: (NewTransactionStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        if (state.part2 !is TransactionInputState.TransactionAmountInput) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MizanTheme.typography.headingMd,
                        color = MizanTheme.premium.text.primary,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))
                }
                IconButton(onClick = {
                    accept(NewTransactionStore.Intent.HidePart2)
                }) {
                    MizanIcon(
                        icon = IconValue(Icons.ic_close),
                        contentDescription = "Back",
                        tint = MizanTheme.premium.text.primary
                    )
                }
            }
        }
        Box(
            modifier = Modifier.padding(horizontal = MizanTheme.premium.spacing.lg)
        ) {
            when (state.part2) {
                is TransactionInputState.TransactionTypeSelector -> {
                    title = "Transaction Type"
                    desc = "Choose the type of transaction"
                    val types = listOf(
                        TransactionTypeInfo(
                            type = TransactionType.EXPENSE,
                            label = "Expense",
                            description = "Money spent",
                            iconRes = Icons.ic_trend_up,
                            color = Color(0xFFF5576C),
                            bgColor = Color(0x1AF5576C) // 10% opacity
                        ),
                        TransactionTypeInfo(
                            type = TransactionType.INCOME,
                            label = "Income",
                            description = "Money received",
                            iconRes = Icons.ic_down_trend,
                            color = Color(0xFF4FACFE),
                            bgColor = Color(0x1A4FACFE) // 10% opacity
                        ),
                        TransactionTypeInfo(
                            type = TransactionType.TRANSFER,
                            label = "Transfer",
                            description = "Move between accounts",
                            iconRes = Icons.ic_swap_horizontal,
                            color = Color(0xFF10B981),
                            bgColor = Color(0x1A10B981) // 10% opacity
                        )
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Main Card
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Type Options
                            types.forEach { typeInfo ->
                                val isSelected = state.transactionType == typeInfo.type
                                var isPressed by remember { mutableStateOf(false) }
                                val scale by animateFloatAsState(
                                    targetValue = if (isPressed) 0.98f else 1f,
                                    animationSpec = tween(200),
                                    label = "scale"
                                )

                                TransactionTypeItem(
                                    typeInfo = typeInfo,
                                    isSelected = isSelected,
                                    scale = scale,
                                    onClick = {
                                        accept(
                                            NewTransactionStore.Intent.SelectTransactionType(
                                                typeInfo.type
                                            )
                                        )
                                    },
                                    onPress = { isPressed = true },
                                    onRelease = { isPressed = false }
                                )

                                if (typeInfo.type != TransactionType.TRANSFER) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                is TransactionInputState.TransactionAmountInput -> {
                    title = ""
                    desc = ""
                    Box(modifier = Modifier) {
                        PremiumCalculatorKeypad(
                            onNumberClick = {
                                accept(NewTransactionStore.AmountInputIntent.OnNumberClick(it))
                            }
                        )
                    }
                }

                is TransactionInputState.TransactionCategorySelector -> {
                    title = "Select Category"
                    desc = "Choose a category for this transaction"
                    TransactionCategories(
                        state = state,
                        accept = accept
                    )
                }

                is TransactionInputState.TransactionAccountSelector -> {
                    title = "Select Category"
                    desc = "Choose a category for this transaction"
                    /*AccountSelectionContent(
                        accounts = state.accounts,
                        selectedAccountId = if (state.selectedAccountActive) {
                            state.selectedAccountId
                        } else state.targetAccountId,

                        onAccountClick = { account ->
                            if (state.selectedAccountActive) {
                                accept(NewTransactionStore.Intent.UpdateSelectedAccount(account.id))
                            } else {
                                accept(NewTransactionStore.Intent.UpdateTargetAccount(account.id))
                            }
                        },
                        onAddAccountClick = {
                            accept(NewTransactionStore.Intent.OpenAccountManageScreen)
                        },
                        onClose = { accept(NewTransactionStore.Intent.CloseAccountSelection) },
                        modifier = Modifier
                    )*/
                }

                is TransactionInputState.TransactionEmpty -> {
                    title = ""
                    desc = ""
                }
            }
        }
    }
}

@Composable
fun TransactionCategories(
    state: NewTransactionStore.State,
    accept: (NewTransactionStore.Intent) -> Unit,
) {
    /* CategorySelectionSheet(
         isVisible = true,
         categories = state.categoryChooserState.categories,
         selectedCategory = state.categoryChooserState.selectedParentId,
         selectedChildId = state.categoryChooserState.selectedChildId,
         onCategorySelect = { category->
             accept(NewTransactionStore.Intent.OnCategorySelect(category))
         },
         onNavigateToManageCategories = {
             accept(NewTransactionStore.Intent.OpenCategoryManageScreen)
         },
     )*/
}

@Preview(
    showBackground = true
)
@Composable
internal fun PremiumTransactionInputContentPreview() {


    MizanTheme {
        PremiumTransactionInputContent(
            state = NewTransactionStore.State(
                part2 = TransactionInputState.TransactionCategorySelector(),
                accounts = MockAccount.mockAccounts
            ),
            accept = {},
            modifier = Modifier
        )
    }
}


object MockAccount {
    val uzs = Currency(
        code = "UZS",
        name = "O'zbek so'mi",
        symbol = "so'm",
        rateToBase = 1.0,
        isBaseCurrency = true
    )

    val usd = Currency(
        code = "USD",
        name = "US Dollar",
        symbol = "$",
        rateToBase = 12_500.0,
        isBaseCurrency = false
    )

    val mockAccounts = listOf(
        Account(
            id = 1L,
            groupId = 100L,
            name = "Cash Wallet",
            type = Account.Type.CASH,
            balance = 250_000.0,
            currency = uzs,
            iconName = "ic_cash",
            color = "#4CAF50",
            isArchived = false,
            excludeFromTotal = false,
            description = "Main daily cash"
        ),
        Account(
            id = 4L,
            groupId = 100L,
            name = "Cash Wallet",
            type = Account.Type.CASH,
            balance = 250_000.0,
            currency = uzs,
            iconName = "ic_cash",
            color = "#4CAF50",
            isArchived = false,
            excludeFromTotal = false,
            description = "Main daily cash"
        ),
        Account(
            id = 2L,
            groupId = 100L,
            name = "Humo Card",
            type = Account.Type.CARD,
            balance = 1_450_000.0,
            currency = uzs,
            iconName = "ic_card",
            color = "#2196F3",
            isArchived = false,
            excludeFromTotal = false,
            description = null
        ),
        Account(
            id = 3L,
            groupId = 200L,
            name = "Visa USD",
            type = Account.Type.CARD,
            balance = 320.0,
            currency = usd,
            iconName = "ic_visa",
            color = "#FF9800",
            isArchived = false,
            excludeFromTotal = false,
            description = "Online payments"
        )
    )
}
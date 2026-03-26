package dev.esbi.mizan.feature.premiumaddtransaction.pad

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
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.accountselector.AccountSelectionContentSimple
import dev.esbi.mizan.feature.newtransaction.categoryselect.CategorySelectionSheet
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeInfo
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeItem
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@Composable
fun AddNewTransactionPad(
    modifier: Modifier = Modifier,
    state: AddNewTransactionStore.State,
    accept: (AddNewTransactionStore.Intent) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        if (state.pad != AddNewTransactionStore.State.Pad.AmountInput) {
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
                IconButton(
                    onClick = {
                        accept(AddNewTransactionStore.Intent.OnClosePad)
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
            when (state.pad) {
                AddNewTransactionStore.State.Pad.AmountInput -> {
                    Box(modifier = Modifier) {
                        PremiumCalculatorKeypad(
                            onNumberClick = { key ->
                                accept(AddNewTransactionStore.Intent.OnNumberClick(key))
                            }
                        )
                    }
                }

                AddNewTransactionStore.State.Pad.TypeSelector -> {
                    title = "Transaction Type"
                    desc = "Choose the type of transaction"
                    val types = listOf(
                        TransactionTypeInfo(
                            type = TransactionType.EXPENSE,
                            label = "Expense",
                            description = "Money spent",
                            iconRes = R.drawable.ic_trend_up,
                            color = Color(0xFFF5576C),
                            bgColor = Color(0x1AF5576C) // 10% opacity
                        ),
                        TransactionTypeInfo(
                            type = TransactionType.INCOME,
                            label = "Income",
                            description = "Money received",
                            iconRes = R.drawable.ic_down_trend,
                            color = Color(0xFF4FACFE),
                            bgColor = Color(0x1A4FACFE) // 10% opacity
                        ),
                        TransactionTypeInfo(
                            type = TransactionType.TRANSFER,
                            label = "Transfer",
                            description = "Move between accounts",
                            iconRes = R.drawable.ic_swap_horizontal,
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
                                            AddNewTransactionStore.Intent.SelectTransactionType(
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

                AddNewTransactionStore.State.Pad.CategorySelector -> {
                    title = "Select Category"
                    desc = "Choose a category for this transaction"
                    CategorySelectionSheet(
                        modifier = Modifier.padding(top = 24.dp),
                        categories = state.categories,
                        selectedParentId = state.selectedCategory?.id,
                        selectedChildId = state.selectedSubCategory?.id,
                        onParentSelected = { category ->
                            accept(
                                AddNewTransactionStore.Intent.OnCategorySelect(category = category)
                            )
                        },
                        onChildSelected = { category ->
                            accept(
                                AddNewTransactionStore.Intent.OnSubCategorySelect(subCategory = category)
                            )
                        },
                        onNavigateToManageCategories = {
                            accept(AddNewTransactionStore.Intent.OpenCategoryManageScreen)
                        },
                    )
                }

                AddNewTransactionStore.State.Pad.AccountSelector -> {
                    title = "Select Account"
                    desc = "Choose a account for this transaction"
                    AccountSelectionContentSimple(
                        accounts = state.accounts,
                        selectedAccount = state.selectedAccount,
                        onAccountClick = { account ->
                            accept(AddNewTransactionStore.Intent.UpdateSelectedAccount(account))
                        },
                        onAddAccountClick = {
                            accept(AddNewTransactionStore.Intent.OpenAccountManageScreen)
                        },
                        modifier = Modifier.padding(top = 24.dp)
                    )

                }

                AddNewTransactionStore.State.Pad.TargetAccountSelector -> {
                    title = "Select Target account"
                    desc = "Choose a account for this transfer"
                    AccountSelectionContentSimple(
                        accounts = state.accounts,
                        selectedAccount = state.targetAccount,
                        onAccountClick = { account ->
                            accept(AddNewTransactionStore.Intent.UpdateTargetAccount(account))
                        },
                        onAddAccountClick = {
                            accept(AddNewTransactionStore.Intent.OpenAccountManageScreen)
                        },
                        modifier = Modifier.padding(top = 24.dp)
                    )

                }

                else -> {}
            }
        }
    }
}

@Composable
internal fun AddNewTransactionNumberPad(

) {

}

@Preview(
    showBackground = true
)
@Composable
fun AddNewTransactionPadPreview() {
    val categories = listOf(
        CategoryEntity(
            id = 1L,
            name = "Oziq-ovqat",
            type = Transaction.Type.EXPENSE,
            iconName = "",
            color = ""
        ),
        CategoryEntity(
            id = 2L,
            name = "Transport",
            type = Transaction.Type.EXPENSE,
            iconName = "",
            color = ""
        ),
        CategoryEntity(
            id = 3L,
            name = "Finance",
            type = Transaction.Type.EXPENSE,
            iconName = "",
            color = ""
        ),
        CategoryEntity(
            id = 4L,
            name = "Oziq-ovqat",
            type = Transaction.Type.EXPENSE,
            iconName = "",
            color = "",
            parentId = 1
        ),
    )


    MizanTheme {
        AddNewTransactionPad(
            modifier = Modifier,
            state = AddNewTransactionStore.State(
                allCategories = categories,
                selectedCategory = CategoryEntity(
                    id = 2L,
                    name = "Oziq-ovqat",
                    type = Transaction.Type.EXPENSE,
                    iconName = "",
                    color = ""
                ),
                selectedSubCategory = null,
                pad = AddNewTransactionStore.State.Pad.CategorySelector
            ),
            accept = {}
        )
    }
}

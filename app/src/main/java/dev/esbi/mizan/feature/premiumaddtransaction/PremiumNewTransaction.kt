package dev.esbi.mizan.feature.premiumaddtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputHeader
import dev.esbi.mizan.feature.premiumaddtransaction.currency.MizanCurrencySelector
import dev.esbi.mizan.feature.premiumaddtransaction.part1.TransactionTypeSelector
import dev.esbi.mizan.feature.premiumaddtransaction.part2.MizanResizableAmount
import dev.esbi.mizan.feature.premiumaddtransaction.part2.color
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import java.math.BigDecimal

@UiComposable
@Composable
fun PremiumNewTransaction(
    state: AddNewTransactionStore.State,
    accept: (AddNewTransactionStore.Intent) -> Unit,
) {
    val displayText = state.displayText
    val interactionSource = remember { MutableInteractionSource() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AmountInputHeader(
                showTemplates = state.showTemplates,
                onTemplatesToggle = {
                    accept(AddNewTransactionStore.Intent.ToggleTemplates)
                },
                onClose = {
                    accept(AddNewTransactionStore.Intent.Back)
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TransactionTypeSelector(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                selectedType = state.transactionType,
                onTypeSelect = {
                    accept(AddNewTransactionStore.Intent.SelectTransactionType(it))
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            // Displey qismi

            /*
                        // Calculation String (if any)
                        if (displayText.isNotEmpty()) {
                            Text(
                                text = displayText,
                                style = MizanTheme.typography.bodySm,
                                color = MizanTheme.premium.text.tertiary,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
            */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(text = state.expression, color = MizanTheme.premium.text.primary)

                    MizanResizableAmount(
                        modifier = Modifier.padding(vertical = MizanTheme.premium.spacing.sm),
                        amount = state.currentValue.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        color = state.transactionType.color(),
                    )
                }

                Box(
                    modifier = Modifier
                        .height(128.dp)
                        .width(48.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                topStart = MizanTheme.premium.radius.xxl,
                                bottomStart = MizanTheme.premium.radius.xxl
                            )
                        )
                        .background(MizanTheme.premium.glass.bg)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                accept(AddNewTransactionStore.Intent.ShowTransactionDetails)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    MizanIcon(
                        modifier = Modifier,
                        icon = IconValue(Icons.ic_chevron_left),
                        tint = MizanTheme.premium.colors.emerald
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Account",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "BANK",
                        style = MizanTheme.typography.headingMd,
                        color = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Category",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "BAR",
                        style = MizanTheme.typography.headingMd,
                        color = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MizanTheme.premium.background.tertiary,
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                MizanCurrencySelector(
                    currencies = state.currencies,
                    selectedCurrency = state.currency,
                    onCurrencySelected = {
                        accept(AddNewTransactionStore.Intent.OnUpdateCurrency(it))
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = MizanTheme.premium.radius.xxl,
                            topEnd = MizanTheme.premium.radius.xxl
                        )
                    )
                    .background(MizanTheme.premium.background.secondary)
                    .padding(MizanTheme.premium.spacing.lg),
                contentAlignment = Alignment.BottomCenter
            ) {
                PremiumCalculatorKeypad(
                    onNumberClick = {
                        accept(AddNewTransactionStore.Intent.OnNumberClick(it))
                    }
                )
            }

            // Save Button
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = {
                    accept(AddNewTransactionStore.Intent.Next)
                }
            ) {
                Text(
                    text = "Next to Confirm",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.text.primary
                )
            }
        }

        /*
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Column'ning umumiy balandligini olamiz
                    val parentHeight = maxHeight
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MizanTheme.premium.background.primary),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // Part1
                        PremiumNewTransactionPart1(
                            state = state,
                            accept = accept,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        accept(AddNewTransactionStore.Intent.OnClosePad)
                                    }
                                ),
                        )

                        state.pad?.let { pad ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(
                                        min = parentHeight / 2,
                                        max = parentHeight * 2 / 3
                                    )
                                    .background(
                                        color = MizanTheme.premium.background.primary,
                                        shape = RoundedCornerShape(
                                            topStart = MizanTheme.premium.radius.xxl,
                                            topEnd = MizanTheme.premium.radius.xxl
                                        )
                                    )
                                    .border(
                                        color = MizanTheme.premium.glass.border,
                                        width = 1.dp,
                                        shape = RoundedCornerShape(
                                            topStart = MizanTheme.premium.radius.xxl,
                                            topEnd = MizanTheme.premium.radius.xxl
                                        )
                                    )
                                    .padding(top = MizanTheme.premium.spacing.md),
                            ) {
                                AddNewTransactionPad(
                                    modifier = Modifier,
                                    state = state,
                                    accept = accept
                                )
                            }
                        }
                    }
                }
        */

    }
}

@Preview(
    showBackground = true
)
@Composable
fun PremiumNewTransactionPreview() {
    MizanTheme() {
        PremiumNewTransaction(
            state = AddNewTransactionStore.State(),
            accept = {}
        )
    }
}
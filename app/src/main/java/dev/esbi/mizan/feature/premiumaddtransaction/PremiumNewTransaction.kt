package dev.esbi.mizan.feature.premiumaddtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputHeader
import dev.esbi.mizan.feature.premiumaddtransaction.pad.AddNewTransactionPad
import dev.esbi.mizan.feature.premiumaddtransaction.part1.PremiumNewTransactionPart1
import dev.esbi.mizan.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@UiComposable
@Composable
fun PremiumNewTransaction(
    state: AddNewTransactionStore.State,
    accept: (AddNewTransactionStore.Intent) -> Unit,
) {

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
                /*
                                if (state.part2 !is TransactionInputState.TransactionEmpty) {
                                    // Part2
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
                                        PremiumTransactionInputContent(
                                            state = state,
                                            accept = accept,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                */
            }
        }

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
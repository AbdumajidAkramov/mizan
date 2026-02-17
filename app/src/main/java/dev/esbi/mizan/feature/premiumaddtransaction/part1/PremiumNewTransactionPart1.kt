package dev.esbi.mizan.feature.premiumaddtransaction.part1

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.utils.AutoResizingText
import dev.esbi.mizan.feature.newtransaction.amountinput.TemplatesCarousel
import dev.esbi.mizan.feature.newtransaction.input.TransactionInputState
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

@Composable
fun PremiumNewTransactionPart1(
    state: NewTransactionStore.State,
    accept: (NewTransactionStore.Intent) -> Unit,
    showTemplates: Boolean,
    modifier: Modifier = Modifier
) {
    val displayText = state.displayText
    val amountText = state.amountText
    val currency = state.currency
    val selectedAccount = state.accounts.find { it.id == state.selectedAccountId }
    val selectedCategory = state.categoryChooserState.selectedCategory
    val selectedSubCategory =
        state.categoryChooserState.selectedChildId?.let { childId ->
            state.categoryChooserState.categories.find { it.id == childId }
        }
    // Transfer-specific accounts
    val targetAccount = state.accounts.find { it.id == state.targetAccountId }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Templates Carousel (Animated)
        AnimatedVisibility(
            visible = showTemplates,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            TemplatesCarousel(
                onTemplateClick = { /* TODO: Apply template */ },
                onManageClick = { /* TODO: Navigate to manage templates */ }
            )
        }
        // Calculation String (if any)
        if (displayText.isNotEmpty()) {
            Text(
                text = displayText,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Large Amount Display
        val formattedAmount = amountText.annotatedString(
            currency = currency
        )
        AutoResizingText(
            text = formattedAmount,
            style = MizanTheme.typography.displayXl.copy(
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 56.sp
            ),
            color = MizanTheme.premium.text.primary,
            maxLines = 1,
            minFontSize = 24.sp,
            modifier = Modifier
                .padding(vertical = MizanTheme.premium.spacing.sm)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        accept(NewTransactionStore.Intent.ShowAmountInputPad)
                    }
                )
        )

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        TransactionContextContent(
            transactionType = state.transactionType,
            selectedCategory = selectedCategory,
            selectedSubCategory = selectedSubCategory,
            selectedAccount = selectedAccount,
            targetAccount = targetAccount,

            onTypeClick = { accept(NewTransactionStore.Intent.ShowTypeSelector) },
            onCategoryClick = { accept(NewTransactionStore.Intent.ShowCategorySelector) },
            onSelectedAccountClick = { accept(NewTransactionStore.Intent.ShowSelectAccountSelector) },
            onTargetAccountClick = { accept(NewTransactionStore.Intent.ShowTargetAccountSelector) }
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { accept(NewTransactionStore.Intent.SmartNext) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981), // Emerald
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Next",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

}

@Composable
@Preview(showBackground = true)
fun PremiumNewTransactionPart1Preview() {
    MizanTheme() {
        PremiumNewTransactionPart1(
            state = NewTransactionStore.State(
                transactionType = TransactionType.INCOME,
                part2 = TransactionInputState.TransactionTypeSelector(),
            ),
            accept = {},
            showTemplates = false,
            modifier = Modifier
        )
    }
}

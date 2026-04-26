package dev.esbi.mizan.feature.premiumaddtransaction.part1

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import dev.esbi.mizan.data.local.entity.category.CategoryEntity
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.addtransaction2.presentation.utils.AutoResizingText
import dev.esbi.mizan.feature.newtransaction.ui.template.TemplatesCarousel
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

@Composable
fun PremiumNewTransactionPart1(
    state: AddNewTransactionStore.State,
    accept: (AddNewTransactionStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayText = ""
    val amountText = ""
    val currency = state.selectedCurrency
    val selectedAccount = state.selectedAccount
    // Transfer-specific accounts
    val targetAccount = state.targetAccount
    val showTemplates: Boolean = state.showTemplates

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
                templateList = state.templateList,
                onTemplateClick = {
                    accept(AddNewTransactionStore.Intent.OnSelectedTemplate(it))
                },
                onManageClick = {
                    accept(AddNewTransactionStore.Intent.OpenTemplateManage)
                }
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
            currency = currency?.symbol
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
                        accept(AddNewTransactionStore.Intent.ShowAmountInputPad)
                    }
                )
        )

        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))

        TransactionContextContent(
            transactionType = state.transactionType,
            selectedCategory = state.selectedCategory,
            selectedSubCategory = state.selectedSubCategory,
            selectedAccount = selectedAccount,
            targetAccount = targetAccount,

            onTypeClick = { accept(AddNewTransactionStore.Intent.ShowTypeSelector) },
            onCategoryClick = { accept(AddNewTransactionStore.Intent.ShowCategorySelector) },
            onSelectedAccountClick = { accept(AddNewTransactionStore.Intent.ShowSelectAccountSelector) },
            onTargetAccountClick = { accept(AddNewTransactionStore.Intent.ShowTargetAccountSelector) }
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { accept(AddNewTransactionStore.Intent.Next) },
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
@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
fun PremiumNewTransactionPart1Preview() {
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
        PremiumNewTransactionPart1(
            state = AddNewTransactionStore.State(
                allCategories = categories,
                selectedCategory = CategoryEntity(
                    id = 1L,
                    name = "Oziq-ovqat",
                    type = Transaction.Type.EXPENSE,
                    iconName = "",
                    color = ""
                ),
                selectedSubCategory = CategoryEntity(
                    id = 4L,
                    name = "Fruits",
                    type = Transaction.Type.EXPENSE,
                    iconName = "",
                    color = "",
                    parentId = 1
                ),
                pad = AddNewTransactionStore.State.Pad.CategorySelector
            ),
            accept = {},
            modifier = Modifier
        )
    }
}

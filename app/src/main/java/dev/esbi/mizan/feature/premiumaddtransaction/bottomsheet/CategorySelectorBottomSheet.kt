package dev.esbi.mizan.feature.premiumaddtransaction.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.newtransaction.categorychooser.CategoryChooserState
import dev.esbi.mizan.feature.newtransaction.categorychooser.CategoryList
import dev.esbi.mizan.feature.newtransaction.categorychooser.ErrorView
import dev.esbi.mizan.feature.newtransaction.categorychooser.LoadingView
import dev.esbi.mizan.presentation.feature.premiumaddtransaction.store.AddNewTransactionStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectorBottomSheet(
    state: CategoryChooserState,
    accept: (AddNewTransactionStore.Intent) -> Unit,
) {
    val tintColor = when (state.transactionType) {
        Transaction.Type.INCOME -> MizanTheme.premium.colors.emerald
        Transaction.Type.EXPENSE -> Color(0xFFF5576C)
        Transaction.Type.TRANSFER -> MizanTheme.premium.colors.primary
    }
    Scaffold(
        modifier = Modifier,
        topBar = {
            /* CategoryChooserHeader(
                 title = "Choose Category",
                 onBack = {},
                 onClose = {}
             )*/
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            when {
                state.isLoading -> LoadingView()
                state.error != null -> ErrorView(
                    error = state.error,
                    onRetry = {
//                        accept(NewTransactionStore.CategoryChooserIntent.RetryLoad)
                    }
                )

                else -> CategoryList(
                    state = state,
                    tintColor = tintColor,
                    onParentClick = {
                        accept(AddNewTransactionStore.Intent.OnCategorySelect(it))
                    },
                    onSubCategoryClick = {
                        accept(AddNewTransactionStore.Intent.OnSubCategorySelect(it))
                    }
                )
            }
        }

    }
}

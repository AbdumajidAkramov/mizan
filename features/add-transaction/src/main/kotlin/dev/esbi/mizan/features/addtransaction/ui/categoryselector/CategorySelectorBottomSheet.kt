package dev.esbi.mizan.features.addtransaction.ui.categoryselector

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.presentation.feature.addtransaction.store.AddNewTransactionStore

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


@Composable
internal fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MizanTheme.premium.colors.primary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Loading categories...",
                style = MizanTheme.premium.typography.bodyMd,
                color = MizanTheme.premium.text.secondary
            )
        }
    }
}

@Composable
internal fun ErrorView(
    error: String,
    onRetry: () -> Unit
) {
    Log.d("ErrorView", "Error: $error")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Oops!",
                style = MizanTheme.premium.typography.headingLg,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.Bold
            )

            // Matn qismi vertikal skroll bo'ladigan qilindi
            Box(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = error,
                    style = MizanTheme.premium.typography.bodyMd,
                    color = MizanTheme.premium.text.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Skroll qo'shish
                        .padding(horizontal = 8.dp)
                )
            }


            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.primary
                )
            ) {
                Text(
                    text = "Retry",
                    style = MizanTheme.premium.typography.labelMd,
                    color = Color.White
                )
            }
        }
    }
}

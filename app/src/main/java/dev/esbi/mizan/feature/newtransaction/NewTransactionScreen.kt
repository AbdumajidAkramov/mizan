package dev.esbi.mizan.feature.newtransaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputContent
import dev.esbi.mizan.feature.newtransaction.amountinput.AmountInputViewModel
import dev.esbi.mizan.feature.newtransaction.categorychooser.CategoryChooserContent
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeContent
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewTransactionScreen(
    viewModel: AmountInputViewModel,
    onBackPressed: () -> Unit,
    onSubmit: () -> Unit
) {
    val labels by viewModel.labels.collectAsState(initial = null)
    val state by viewModel.state.collectAsState(initial = NewTransactionStore.State())
    val accept = viewModel::onIntent
    val context = LocalContext.current
    LaunchedEffect(labels) {
        when (labels) {
            NewTransactionStore.Label.MapsToNextStep -> onSubmit()
            NewTransactionStore.Label.Back -> onBackPressed()
            is NewTransactionStore.Label.ShowError -> {
                Toast.makeText(
                    context,
                    (labels as NewTransactionStore.Label.ShowError).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            null -> { /* Ignore */
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.currentPage.title,
                        style = MizanTheme.premium.typography.headingSm,
                        color = MizanTheme.premium.text.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        accept(NewTransactionStore.Intent.Back)
                    }) {
                        MizanIcon(
                            icon = IconValue(Icons.ic_arrow_back),
                            contentDescription = "Back",
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                },
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MizanTheme.premium.background.primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MizanTheme.premium.background.primary)
        ) {
            when (state.currentPage) {
                is TransactionStep.AmountInput -> {
                    AmountInputContent(
                        state = state,
                        accept = accept
                    )

                }

                is TransactionStep.TypeSelector -> {
                    TransactionTypeContent(
                        amount = state.keypadState.amountText,
                        onTypeSelect = { type ->
                            accept(NewTransactionStore.Intent.OnTypeSelect(type))
                        }
                    )
                }

                is TransactionStep.CategoryChooser -> {
                    CategoryChooserContent(
                        amount = state.keypadState.amountText,
                        state = state.categoryChooserState,
                        accept = accept
                    )
                }

                is TransactionStep.Transfer -> {

                }

                else -> {}
            }
        }
    }
}

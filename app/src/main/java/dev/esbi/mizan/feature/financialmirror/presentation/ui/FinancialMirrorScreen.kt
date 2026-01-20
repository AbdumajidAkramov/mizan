package dev.esbi.mizan.feature.financialmirror.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModel
import dev.esbi.mizan.feature.financialmirror.presentation.FinancialMirrorViewModelFactory
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStore
import dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets.FinancialMirrorScrollContent
import dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets.LoadingContent
import dev.esbi.mizan.ui.components.ErrorState

internal const val ANIM_DELAY_MS = 50

@Composable
fun FinancialMirrorScreen(
 viewModel: FinancialMirrorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState(initial = FinancialMirrorStore.State())

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is FinancialMirrorStore.Label.ShowError -> {}
                is FinancialMirrorStore.Label.NavigateToInvestment -> {}
            }
        }
    }
    when {
        state.isLoading && state.financialMirrorData == null -> LoadingContent()
        state.error != null && state.financialMirrorData == null -> ErrorState(
            message = state.error ?: "Unknown error",
            onRetry = { viewModel.onIntent(FinancialMirrorStore.Intent.Retry) },
            modifier = Modifier.fillMaxSize()
        )

        state.financialMirrorData != null -> FinancialMirrorScrollContent(
            modifier = modifier,
            data = state.financialMirrorData!!,
            selectedView = state.selectedProjectionView,
            onViewSelected = {
                viewModel.onIntent(
                    FinancialMirrorStore.Intent.SelectProjectionView(
                        it
                    )
                )
            })
    }

}

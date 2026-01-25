package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun AmountInputScreen(
    viewModel: AmountInputViewModel,
    onBackPressed: () -> Unit,
    onSubmit: () -> Unit
) {
    val labels by viewModel.labels.collectAsState(initial = null)
    
    LaunchedEffect(labels) {
        when (labels) {
            AmountInputStore.Label.MapsToNextStep -> onSubmit()
            null -> { /* Ignore */ }
        }
    }
    
    AmountInputContent(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onSubmit = onSubmit,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .background(MizanTheme.premium.background.primary)
    )
}

package dev.esbi.mizan.feature.newtransaction.amountinput

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
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AmountInputScreen(
    viewModel: AmountInputViewModel,
    onBackPressed: () -> Unit,
    onSubmit: () -> Unit
) {
    val labels by viewModel.labels.collectAsState(initial = null)
    val state by viewModel.state.collectAsState(initial = AmountInputStore.State())
    val accept = viewModel::onIntent

    LaunchedEffect(labels) {
        when (labels) {
            AmountInputStore.Label.MapsToNextStep -> onSubmit()
            AmountInputStore.Label.Back -> onBackPressed()
            null -> { /* Ignore */
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Amount input",
                        style = MizanTheme.premium.typography.headingSm,
                        color = MizanTheme.premium.text.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { accept(AmountInputStore.Intent.NavigateBack) }) {
                        dev.esbi.mizan.ui.kit.icon.MizanIcon(
                            icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_arrow_back),
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

            AmountInputContent(
                state = state,
                accept = accept
            )
        }
    }

}

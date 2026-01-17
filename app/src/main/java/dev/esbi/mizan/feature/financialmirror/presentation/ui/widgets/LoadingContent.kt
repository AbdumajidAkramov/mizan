package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.esbi.mizan.ui.components.LoadingSkeleton
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MizanTheme.premium.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
    ) {
        LoadingSkeleton(height = 200)
        LoadingSkeleton(height = 300)
        LoadingSkeleton(height = 200)
        LoadingSkeleton(height = 200)
    }
}

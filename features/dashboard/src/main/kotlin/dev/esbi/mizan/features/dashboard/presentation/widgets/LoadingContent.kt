package dev.esbi.mizan.features.dashboard.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.design.components.LoadingSkeleton

@Composable
fun LoadingContent() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp), Arrangement.spacedBy(24.dp)
    ) {
        LoadingSkeleton(height = 200); LoadingSkeleton(height = 160); LoadingSkeleton(height = 300)
    }
}

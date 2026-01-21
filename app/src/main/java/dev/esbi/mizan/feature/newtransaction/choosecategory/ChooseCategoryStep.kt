package dev.esbi.mizan.feature.newtransaction.choosecategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionIntent
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionState
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun ChooseCategoryStep(
    state: NewTransactionState,
    onIntent: (NewTransactionIntent) -> Unit,
    onManageCategories: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: Implement Category Selection UI
        Text(
            text = "Choose Category",
            style = MizanTheme.typography.bodyLg,
            color = MizanTheme.premium.text.primary,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))
        
        Text(
            text = "Category selection coming soon...",
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.secondary
        )
    }
}

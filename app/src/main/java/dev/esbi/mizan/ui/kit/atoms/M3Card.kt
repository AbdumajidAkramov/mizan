package dev.esbi.mizan.ui.kit.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class M3CardVariant {
    FILLED,
    ELEVATED,
    OUTLINED
}

@Composable
fun M3Card(
    modifier: Modifier = Modifier,
    variant: M3CardVariant = M3CardVariant.FILLED,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    when (variant) {
        M3CardVariant.FILLED -> {
            if (onClick != null) {
                Card(
                    onClick = onClick,
                    modifier = modifier,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    content = content
                )
            } else {
                Card(
                    modifier = modifier,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    content = content
                )
            }
        }
        M3CardVariant.ELEVATED -> {
            if (onClick != null) {
                ElevatedCard(
                    onClick = onClick,
                    modifier = modifier,
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp
                    ),
                    content = content
                )
            } else {
                ElevatedCard(
                    modifier = modifier,
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp
                    ),
                    content = content
                )
            }
        }
        M3CardVariant.OUTLINED -> {
            if (onClick != null) {
                OutlinedCard(
                    onClick = onClick,
                    modifier = modifier,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    content = content
                )
            } else {
                OutlinedCard(
                    modifier = modifier,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    content = content
                )
            }
        }
    }
}

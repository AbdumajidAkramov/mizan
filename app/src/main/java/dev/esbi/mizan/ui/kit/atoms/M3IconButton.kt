package dev.esbi.mizan.ui.kit.atoms

import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

enum class M3IconButtonVariant {
    STANDARD,
    FILLED,
    FILLED_TONAL,
    OUTLINED
}

@Composable
fun M3IconButton(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: M3IconButtonVariant = M3IconButtonVariant.STANDARD,
    enabled: Boolean = true,
    contentDescription: String? = null
) {
    when (variant) {
        M3IconButtonVariant.STANDARD -> {
            IconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription
                )
            }
        }
        M3IconButtonVariant.FILLED -> {
            FilledIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription
                )
            }
        }
        M3IconButtonVariant.FILLED_TONAL -> {
            FilledTonalIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription
                )
            }
        }
        M3IconButtonVariant.OUTLINED -> {
            OutlinedIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription
                )
            }
        }
    }
}

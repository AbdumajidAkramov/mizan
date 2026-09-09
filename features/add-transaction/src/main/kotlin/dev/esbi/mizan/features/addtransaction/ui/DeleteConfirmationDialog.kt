package dev.esbi.mizan.features.addtransaction.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import dev.esbi.mizan.design.theme.colors.MizanTheme

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MizanTheme.premium.background.primary.copy(1f),
        title = {
            Text(
                text = "Delete Transaction",
                style = MizanTheme.premium.typography.headingSm,
                color = MizanTheme.premium.text.primary,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete this transaction? This action cannot be undone.",
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.secondary
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = "Delete",
                    color = Color(0xFFF5576C),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    color = MizanTheme.premium.text.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

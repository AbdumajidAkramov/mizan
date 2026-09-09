package dev.esbi.mizan.ui.kit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.esbi.mizan.design.theme.PremiumColors

@Composable
fun KitDialog(
    onDismissRequest: () -> Unit,
    title: String,
    content: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = PremiumColors.TextPrimary
            )
        },
        text = content,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        containerColor = PremiumColors.Surface2,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(PremiumColors.Surface2)
                .padding(24.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel"
) {
    KitDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        content = {
            Text(
                text = message,
                fontSize = 14.sp,
                color = PremiumColors.TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismissRequest()
            }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dismissText)
            }
        }
    )
}

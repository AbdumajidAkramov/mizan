package dev.esbi.mizan.ui.kit.atoms

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class M3ButtonVariant {
    FILLED,
    FILLED_TONAL,
    ELEVATED,
    OUTLINED,
    TEXT
}

@Composable
fun M3Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: M3ButtonVariant = M3ButtonVariant.FILLED,
    enabled: Boolean = true,
    fullWidth: Boolean = false
) {
    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth().height(48.dp)
    } else {
        modifier.height(48.dp)
    }
    
    when (variant) {
        M3ButtonVariant.FILLED -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        M3ButtonVariant.FILLED_TONAL -> {
            FilledTonalButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        M3ButtonVariant.ELEVATED -> {
            ElevatedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        M3ButtonVariant.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        M3ButtonVariant.TEXT -> {
            TextButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

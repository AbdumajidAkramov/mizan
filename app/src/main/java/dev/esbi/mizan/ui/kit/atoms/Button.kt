package dev.esbi.mizan.ui.kit.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.PremiumColors

enum class ButtonVariant {
    FILLED,
    OUTLINED,
    TEXT,
    ELEVATED
}

enum class ButtonSize(val height: Int, val horizontalPadding: Int, val fontSize: Int) {
    SMALL(32, 16, 12),
    MEDIUM(40, 20, 14),
    LARGE(48, 24, 16)
}

@Composable
fun KitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.FILLED,
    size: ButtonSize = ButtonSize.MEDIUM,
    enabled: Boolean = true,
    fullWidth: Boolean = false,
    content: @Composable () -> Unit
) {
    val buttonModifier = if (fullWidth) {
        modifier.fillMaxWidth().height(size.height.dp)
    } else {
        modifier.height(size.height.dp)
    }
    
    when (variant) {
        ButtonVariant.FILLED -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF667EEA),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF667EEA).copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.4f)
                ),
                contentPadding = PaddingValues(horizontal = size.horizontalPadding.dp)
            ) {
                content()
            }
        }
        ButtonVariant.ELEVATED -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PremiumColors.Surface3,
                    contentColor = PremiumColors.TextPrimary,
                    disabledContainerColor = PremiumColors.Surface3.copy(alpha = 0.4f),
                    disabledContentColor = PremiumColors.TextPrimary.copy(alpha = 0.4f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                ),
                contentPadding = PaddingValues(horizontal = size.horizontalPadding.dp)
            ) {
                content()
            }
        }
        ButtonVariant.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF667EEA),
                    disabledContentColor = Color(0xFF667EEA).copy(alpha = 0.4f)
                ),
                border = BorderStroke(1.dp, Color(0xFF667EEA)),
                contentPadding = PaddingValues(horizontal = size.horizontalPadding.dp)
            ) {
                content()
            }
        }
        ButtonVariant.TEXT -> {
            TextButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF667EEA),
                    disabledContentColor = Color(0xFF667EEA).copy(alpha = 0.4f)
                ),
                contentPadding = PaddingValues(horizontal = size.horizontalPadding.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun KitButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.FILLED,
    size: ButtonSize = ButtonSize.MEDIUM,
    enabled: Boolean = true,
    fullWidth: Boolean = false
) {
    KitButton(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        size = size,
        enabled = enabled,
        fullWidth = fullWidth
    ) {
        Text(
            text = text,
            fontSize = size.fontSize.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

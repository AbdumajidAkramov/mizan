package dev.esbi.mizan.ui.components.input

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun MizanTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MizanTheme.premium.text.secondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        val isError = errorText != null
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MizanTheme.premium.text.tertiary,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isError) 1.5.dp else 1.dp,
                    color = if (isError) MizanTheme.premium.colors.error else Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedTextColor = MizanTheme.premium.text.primary,
                unfocusedTextColor = MizanTheme.premium.text.primary,
                errorTextColor = MizanTheme.premium.text.primary,
                cursorColor = MizanTheme.premium.colors.emerald,
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                errorContainerColor = MizanTheme.premium.colors.error.copy(alpha = 0.05f),
            ),
            shape = RoundedCornerShape(16.dp),
            isError = isError,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions
        )
        
        if (isError) {
            Text(
                text = errorText!!,
                color = MizanTheme.premium.colors.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

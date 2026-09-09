package dev.esbi.mizan.ui.kit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.PremiumColors

@Composable
fun <T> Select(
    value: T?,
    onValueChange: (T) -> Unit,
    options: List<T>,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select an option",
    displayText: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = PremiumColors.TextSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PremiumColors.Surface2)
                    .border(1.dp, PremiumColors.Surface3, RoundedCornerShape(12.dp))
                    .clickable { expanded = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value?.let { displayText(it) } ?: placeholder,
                    fontSize = 14.sp,
                    color = if (value != null) PremiumColors.TextPrimary else PremiumColors.TextMuted,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(android.R.drawable.arrow_down_float),
                    contentDescription = "Dropdown",
                    tint = PremiumColors.TextMuted
                )
            }
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PremiumColors.Surface2)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = displayText(option),
                                fontSize = 14.sp,
                                color = PremiumColors.TextPrimary
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

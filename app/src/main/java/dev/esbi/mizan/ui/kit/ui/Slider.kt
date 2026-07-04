package dev.esbi.mizan.ui.kit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.design.theme.PremiumColors

@Composable
fun KitSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    showValue: Boolean = false,
    valueFormatter: (Float) -> String = { it.toString() }
) {
    Column(modifier = modifier) {
        if (label != null || showValue) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        color = PremiumColors.TextSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (showValue) {
                    Text(
                        text = valueFormatter(value),
                        fontSize = 14.sp,
                        color = PremiumColors.TextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF667EEA),
                activeTrackColor = Color(0xFF667EEA),
                inactiveTrackColor = PremiumColors.Surface3
            )
        )
    }
}

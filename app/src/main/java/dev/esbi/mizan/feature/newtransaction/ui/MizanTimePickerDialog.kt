package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MizanTimePickerDialog(
    isVisible: Boolean,
    initialTime: Long = System.currentTimeMillis(),
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    val calendar = Calendar.getInstance().apply {
        timeInMillis = initialTime
    }
    val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
    val initialMinute = calendar.get(Calendar.MINUTE)

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(MizanTheme.premium.radius.xl),
            color = MizanTheme.premium.background.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MizanTheme.premium.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    style = MizanTheme.premium.typography.headingSm,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

                TimePicker(
                    state = timePickerState,
                   /* colors = TimePickerDefaults.colors(
                        clockDialColor = MizanTheme.premium.colors.surface3,
                        selectorColor = MizanTheme.premium.colors.emerald,
                        containerColor = MizanTheme.premium.colors.surface2,
                        periodSelectorBorderColor = MizanTheme.premium.colors.emerald,
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = MizanTheme.premium.text.secondary,
                        periodSelectorSelectedContainerColor = MizanTheme.premium.colors.emerald,
                        periodSelectorUnselectedContainerColor = MizanTheme.premium.colors.surface3,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = MizanTheme.premium.text.secondary,
                        timeSelectorSelectedContainerColor = MizanTheme.premium.colors.emerald.copy(alpha = 0.2f),
                        timeSelectorUnselectedContainerColor = MizanTheme.premium.colors.surface3,
                        timeSelectorSelectedContentColor = MizanTheme.premium.colors.emerald,
                        timeSelectorUnselectedContentColor = MizanTheme.premium.text.primary
                    )*/
                )

                Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.lg))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.text.secondary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(MizanTheme.premium.spacing.sm))

                    TextButton(
                        onClick = {
                            onTimeSelected(timePickerState.hour, timePickerState.minute)
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = "OK",
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.colors.emerald,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

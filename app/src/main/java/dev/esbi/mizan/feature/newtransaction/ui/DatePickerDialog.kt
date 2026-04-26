package dev.esbi.mizan.feature.newtransaction.ui

import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.material3.DatePickerDialog as MaterialDatePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MizanDatePickerDialog(
    isVisible: Boolean,
    initialDate: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate
        )

        MaterialDatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDate ->
                            onDateSelected(selectedDate)
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

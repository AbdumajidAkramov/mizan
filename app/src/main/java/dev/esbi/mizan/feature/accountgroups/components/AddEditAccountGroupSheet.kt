package dev.esbi.mizan.feature.accountgroups.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.AccountGroupType
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAccountGroupSheet(
    onDismiss: () -> Unit,
    onSave: (name: String, type: AccountGroupType) -> Unit,
    initialName: String = "",
    initialType: AccountGroupType = AccountGroupType.DEFAULT
) {
    var name by remember { mutableStateOf(initialName) }
    var selectedType by remember { mutableStateOf(initialType) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MizanTheme.premium.background.primary,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Account Group",
                style = MizanTheme.premium.typography.headingMd,
                color = MizanTheme.premium.text.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Group Name", color = MizanTheme.premium.text.tertiary) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MizanTheme.premium.colors.emerald,
                    focusedTextColor = MizanTheme.premium.text.primary,
                    unfocusedTextColor = MizanTheme.premium.text.primary,
                    cursorColor = MizanTheme.premium.colors.emerald
                ),
                shape = RoundedCornerShape(12.dp),
                        singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Type Selector
            Text(
                text = "Group Type",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.secondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AccountGroupType.entries.forEach { type ->
                    val isSelected = selectedType == type
                    val borderColor = if (isSelected) MizanTheme.premium.colors.emerald else MizanTheme.premium.background.secondary
                    val bgColor = if (isSelected) MizanTheme.premium.colors.emerald.copy(alpha = 0.2f) else MizanTheme.premium.background.secondary
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(
                                width = 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedType = type }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (type) {
                                AccountGroupType.DEFAULT -> "General"
                                AccountGroupType.CREDIT_CARD -> "Credit"
                                AccountGroupType.DEBIT_CARD -> "Debit"
                            },
                            color = if (isSelected) MizanTheme.premium.colors.emerald else MizanTheme.premium.text.secondary,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            style = MizanTheme.typography.bodySm
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = { if (name.isNotBlank()) onSave(name, selectedType) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.emerald,
                    disabledContainerColor = MizanTheme.premium.colors.emerald.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank()
            ) {
                Text(
                    text = "Save Group",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

package dev.esbi.mizan.feature.addtransaction.presentation.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.presentation.dialog.PremiumDatePickerDialog
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ==========================================
// 2. CONFIRM STEP (Xulosa va Saqlash)
// ==========================================

@Composable
internal fun ConfirmStep(
    state: AddTransactionStore.State,
    accept: (AddTransactionStore.Intent) -> Unit,
) {
    ConfirmStep(
        amount = state.amountText,
        currency = state.currency,
        type = state.type,
        category = state.selectedCategory,
        fromAccount = state.fromAccountId,
        toAccount = state.toAccountId,
        date = state.date,
        notes = state.notes,
        isFormValid = state.isFormValid,
        onDateChange = {
            accept(AddTransactionStore.Intent.OnDateChange(it))
        },
        onNotesChange = {
            accept(AddTransactionStore.Intent.OnNoteChange(it))
        },
        onSave = {
            accept(AddTransactionStore.Intent.OnSaveTransaction)
        }
    )
}

@Composable
internal fun ConfirmStep(
    amount: String,
    currency: String,
    type: TransactionType,
    category: String?,
    fromAccount: String?,
    toAccount: String?,
    date: LocalDate,
    notes: String,
    isFormValid: Boolean,
    onDateChange: (LocalDate) -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit
) {
    // UI State
    var showDatePicker by remember { mutableStateOf(false) }
    var showNotesInput by remember { mutableStateOf(notes.isNotEmpty()) }

    // Ranglar va Ikonkalarni aniqlash
    val (bgTint, accentColor, icon) = when (type) {
        TransactionType.Expense -> Triple(
            Color(0xFFF5576C).copy(alpha = 0.1f), // Red tint
            Color(0xFFF5576C), // Red
            IconValue(dev.esbi.mizan.ui.utils.Icons.ic_arrow_up)
        )

        TransactionType.Income -> Triple(
            Color(0xFF4FACFE).copy(alpha = 0.1f), // Blue tint
            Color(0xFF4FACFE), // Blue
            IconValue(dev.esbi.mizan.ui.utils.Icons.ic_arrow_down)
        )

        TransactionType.Transfer -> Triple(
            Color(0xFF10B981).copy(alpha = 0.1f), // Emerald tint
            Color(0xFF10B981), // Emerald
            IconValue(dev.esbi.mizan.ui.utils.Icons.ic_swap_horizontal)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = MizanTheme.premium.spacing.lg,
                vertical = MizanTheme.premium.spacing.xl
            )
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
        ) {

            // --- 1. SUMMARY CARD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MizanTheme.premium.radius.xl))
                    .background(bgTint)
                    .border(
                        2.dp,
                        accentColor.copy(alpha = 0.5f),
                        RoundedCornerShape(MizanTheme.premium.radius.xl)
                    )
                    .padding(MizanTheme.premium.spacing.xl)
            ) {
                Column {
                    // Header (Type, Amount, Icon)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = type.name,
                                style = MizanTheme.typography.bodySm,
                                color = accentColor,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = amount.annotatedString(currency = currency),
                                style = MizanTheme.typography.displaySm, // heading-3xl
                                color = MizanTheme.premium.text.primary
                            )
                        }

                        // Icon Circle
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    bgTint,
                                    RoundedCornerShape(MizanTheme.premium.radius.md)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                icon = icon,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(MizanTheme.premium.spacing.md))

                    // Footer Text (From/To or Category)
                    if (type == TransactionType.Transfer) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "From: ${
                                    fromAccount?.replace("-", " ")?.capitalize() ?: ""
                                }",
                                style = MizanTheme.typography.bodyMd,
                                color = MizanTheme.premium.text.secondary
                            )
                            Text(
                                text = "To: ${toAccount?.replace("-", " ")?.capitalize() ?: ""}",
                                style = MizanTheme.typography.bodyMd,
                                color = MizanTheme.premium.text.secondary
                            )
                        }
                    } else {
                        Text(
                            text = category?.capitalize() ?: "",
                            style = MizanTheme.typography.bodyLg,
                            color = MizanTheme.premium.text.secondary
                        )
                    }
                }
            }

            // --- 2. DATE SELECTOR ---
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(MizanTheme.premium.colors.surface2)
                        .clickable { showDatePicker = !showDatePicker }
                        .padding(MizanTheme.premium.spacing.md)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
                    ) {
                        Icon(
                            icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_calendar_month),
                            tint = MizanTheme.premium.text.tertiary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.text.primary
                        )
                    }
                }

                if (showDatePicker) {
                    PremiumDatePickerDialog(
                        initialDate = date, // Hozirgi tanlangan sana
                        onDismissRequest = { showDatePicker = false }, // Yopish
                        onDateSelected = { newDate ->
                            onDateChange(newDate) // Sanani yangilash
                            showDatePicker = false // Dialogni yopish
                        }
                    )
                }
            }

            // --- 3. NOTES INPUT ---
            if (!showNotesInput) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                        .background(MizanTheme.premium.colors.surface2)
                        .clickable { showNotesInput = true }
                        .padding(MizanTheme.premium.spacing.md)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
                    ) {
                        Icon(
                            icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_file),
                            tint = MizanTheme.premium.text.tertiary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Add a note (optional)",
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MizanTheme.premium.colors.surface2,
                            RoundedCornerShape(MizanTheme.premium.radius.lg)
                        )
                        .padding(MizanTheme.premium.spacing.md)
                ) {
                    BasicTextField(
                        value = notes,
                        onValueChange = onNotesChange,
                        textStyle = MizanTheme.typography.bodyMd.copy(color = MizanTheme.premium.text.primary),
                        cursorBrush = SolidColor(MizanTheme.premium.colors.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp),
                        decorationBox = { innerTextField ->
                            if (notes.isEmpty()) {
                                Text(
                                    "Add a note...",
                                    style = MizanTheme.typography.bodyMd,
                                    color = MizanTheme.premium.text.muted
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // --- 4. SAVE BUTTON ---
            // Validatsiya logikasi

            AnimatedVisibility(
                visible = isFormValid,
                enter = slideInVertically { it / 2 } + fadeIn()
            ) {
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981), // Emerald
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Save Transaction",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Helper Extension for String Capitalization
private fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
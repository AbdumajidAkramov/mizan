package dev.esbi.mizan.feature.addtransaction.presentation.widgets

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.R.drawable.ic_calculate
import dev.esbi.mizan.R.drawable.ic_camera_alt
import dev.esbi.mizan.R.drawable.ic_mic
import dev.esbi.mizan.feature.addtransaction.domain.models.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.FlowState
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.feature.addtransaction.presentation.utils.AutoResizingText
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

@Composable
internal fun AmountInputStep(
    state: AddTransactionStore.State,
    accept: (AddTransactionStore.Intent) -> Unit,
) {
    AmountInputStep(
        amount = state.amountText,
        displayText = state.displayText,
        currency = state.currency,
        inputMode = state.inputMode,
        onModeChange = { accept(AddTransactionStore.Intent.OnInputModeChange(it)) },
        onNumberClick = {
            accept(AddTransactionStore.Intent.OnKeypadClick(it))
        },
        onNext = {
            accept(AddTransactionStore.Intent.OnKeypadNext)
        },
    )
}

@Composable
internal fun AmountInputStep(
    amount: String,
    displayText: String,
    currency: String,
    inputMode: InputMode,
    onModeChange: (InputMode) -> Unit,
    onNumberClick: (Keypad) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text
            Text(
                text = displayText,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                modifier = Modifier
            )
            // Display
            // Yangi holat (Double ga o'tkazib formatlaymiz):
            val formattedAmount = amount.annotatedString(currency = currency)
            AutoResizingText(
                text = formattedAmount,
                style = MizanTheme.typography.displayXl,
                color = MizanTheme.premium.text.primary,
                maxLines = 1,
                minFontSize = 12.sp,
                modifier = Modifier
                    .padding(vertical = MizanTheme.premium.spacing.md)
            )

            // Mode Switcher
            Row(
                modifier = Modifier
                    .width(width = 208.dp)
                    .background(
                        MizanTheme.premium.colors.surface1,
                        RoundedCornerShape(MizanTheme.premium.radius.full)
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg)
            ) {
                listOf(
                    InputMode.Manual to IconValue(ic_calculate),
                    InputMode.Voice to IconValue(ic_mic),
                    InputMode.Scan to IconValue(ic_camera_alt)
                ).forEach { (mode, icon) ->
                    val isSelected = inputMode == mode
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MizanTheme.premium.colors.surface3 else Color.Transparent)
                            .clickable { onModeChange(mode) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon = icon,
                            modifier = Modifier.size(22.dp),
                            tint = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))
        Spacer(Modifier.weight(1f))
        // Content Body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = MizanTheme.premium.radius.xxl,
                        topEnd = MizanTheme.premium.radius.xxl
                    )
                )
                .background(MizanTheme.premium.background.secondary)
                .padding(MizanTheme.premium.spacing.lg),
            contentAlignment = Alignment.BottomCenter
        ) {
            when (inputMode) {
                InputMode.Manual -> {
                    Column {
                        PremiumCalculatorKeypad(
                            modifier = Modifier,
                            onNumberClick = onNumberClick,
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onNext,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults
                                .buttonColors(
                                    containerColor = if ((amount.toDoubleOrNull() ?: 0.0) > 0) {
                                        MizanTheme.premium.colors.emerald
                                    } else {
                                        MizanTheme.premium.colors.surface4
                                    }
                                )
                        ) {
                            Text("Next", fontSize = 18.sp)
                        }
                    }
                }

                InputMode.Voice -> PremiumEnhancedVoiceInput(
                    isListening = true,
                    onStartListening = {},
                    onStopListening = {}
                )

                InputMode.Scan -> PremiumScanInput(true, {}, {})
            }
        }
    }
}


@Preview(
    name = "AmountInputStepPreview",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AmountInputStepPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme(
        darkTheme = true
    ) {
        AmountInputStep(
            amount = "1",
            displayText = "",
            currency = "UZS",
            inputMode = InputMode.Manual,
            onModeChange = {},
            onNumberClick = {},
            onNext = {},
        )
    }
}
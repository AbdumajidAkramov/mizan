package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.utils.AutoResizingText
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.CameraInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.VoiceInputStep
import dev.esbi.mizan.feature.newtransaction.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.feature.newtransaction.amountinput.widgets.InputModeContent
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AmountInputContent(
    state: AmountInputState = AmountInputState(),
    accept: (NewTransactionStore.Intent) -> Unit
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
/*
            TransactionTypeSelector(
                selectedType = state.transactionType,
                onTypeSelect = {
                    accept(AmountInputStore.Intent.OnTypeSelect(it))
                },
            )
*/
            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Text
            Text(
                text = state.keypadState.displayText,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                modifier = Modifier
            )
            // Display
            // Yangi holat (Double ga o'tkazib formatlaymiz):
            val formattedAmount =
                state.keypadState.amountText.annotatedString(currency = state.keypadState.currency)
            AutoResizingText(
                text = formattedAmount,
                style = MizanTheme.typography.displayXl,
                color = MizanTheme.premium.text.primary,
                maxLines = 1,
                minFontSize = 12.sp,
                modifier = Modifier
                    .padding(vertical = MizanTheme.premium.spacing.md)
            )
            InputModeContent(
                inputMode = state.inputMode,
                onModeChange = { mode ->
                    accept(NewTransactionStore.Intent.OnModeChange(mode))
                }
            )
        }
        // Keypad Section - Bottom with elevation

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .background(
                    color = MizanTheme.premium.glass.bg,
                    shape = RoundedCornerShape(
                        topStart = MizanTheme.premium.radius.xxl,
                        topEnd = MizanTheme.premium.radius.xxl
                    )
                )
                .border(
                    color = MizanTheme.premium.glass.border,
                    width = 1.dp, shape = RoundedCornerShape(
                        topStart = MizanTheme.premium.radius.xxl,
                        topEnd = MizanTheme.premium.radius.xxl
                    )
                ).padding(24.dp)
        ) {
            Spacer(Modifier.weight(1f))

            when (state.inputMode) {
                InputMode.Manual -> {
                    PremiumCalculatorKeypad(onNumberClick = {
                        accept(NewTransactionStore.AmountInputIntent.OnNumberClick(it))
                    })
                    val bgColor = if (state.keypadState.canSubmit) {
                        MizanTheme.premium.colors.emerald
                    } else {
                        MizanTheme.premium.colors.surface2
                    }

                    Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
                            .background(bgColor)
                            .clickable {
                                accept(NewTransactionStore.Intent.TransactionTypesShow)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Next",
                            style = MizanTheme.typography.bodyLg,
                            fontWeight = FontWeight.Medium,
                            color = if (state.keypadState.canSubmit) {
                                Color.White
                            } else {
                                MizanTheme.premium.text.muted
                            }
                        )
                    }
                }

                InputMode.Voice -> {
                    VoiceInputStep(
                        state = state.voiceInputState,
                        onStartListening = {
                            accept(NewTransactionStore.VoiceRecognitionIntent.OnStartListening)
                        },
                        onStopListening = {
                            accept(NewTransactionStore.VoiceRecognitionIntent.OnStopListening)
                        },
                        onVoiceRecognitionError = { error ->
                            // Clear error and restart listening
                            accept(NewTransactionStore.VoiceRecognitionIntent.OnVoiceRecognitionError(error))
                        },
                        onSubmitVoice = { voiceText ->
                            // Parse the voice text again and apply it
                            accept(NewTransactionStore.VoiceRecognitionIntent.OnVoiceResult(voiceText))
                        }
                    )
                }

                InputMode.Scan -> {
                    CameraInputStep(
                        state = state.cameraInputState,
                        onStartScanning = { accept(NewTransactionStore.CameraScanIntent.OnStartCameraScan) },
                        onStopScanning = { accept(NewTransactionStore.CameraScanIntent.OnStopCameraScan) },
                        onAmountExtracted = {
                            accept(
                                NewTransactionStore.CameraScanIntent.OnAmountExtracted(it)
                            )
                        },
                        onScanResult = { text, confidence ->
                            accept(
                                NewTransactionStore.CameraScanIntent.OnReceiptScanResult(text, confidence)
                            )
                        },
                        onQRCodeScanned = { qrText ->
                            accept(NewTransactionStore.CameraScanIntent.OnQrCodeScanned(qrText))
                        },
                        onError = { error ->
                            accept(NewTransactionStore.CameraScanIntent.OnCameraScanError(error))
                        },
                        onNext = {
                        }
                    )
                }
            }
        }
    }
}

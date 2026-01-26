package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.CameraInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.KeypadContent
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.VoiceInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.feature.newtransaction.amountinput.widgets.InputModeContent
import dev.esbi.mizan.feature.newtransaction.transactiontype.TransactionTypeSelector
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AmountInputContent(
    state: AmountInputState = AmountInputState(),
    accept: (AmountInputStore.Intent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode Switcher
        Spacer(Modifier.height(MizanTheme.premium.spacing.md))
        TransactionTypeSelector(
            selectedType = state.transactionType,
            onTypeSelect = {
                accept(AmountInputStore.Intent.OnTypeSelect(it))
            },
        )
        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            InputModeContent(
                inputMode = state.inputMode,
                onModeChange = { mode ->
                    accept(
                        AmountInputStore.Intent.OnModeChange(
                            mode
                        )
                    )
                }
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        // Keypad Section - Bottom with elevation
        when (state.inputMode) {
            InputMode.Manual -> {
                KeypadContent(
                    state = state.keypadState,
                    onNumberClick = { key ->
                        accept(
                            AmountInputStore.Intent.OnNumberClick(
                                key
                            )
                        )
                    },
                    onSubmit = {
                        accept(AmountInputStore.Intent.OnSubmit)
                    }
                )
            }

            InputMode.Voice -> VoiceInputStep(
                state = state.voiceInputState,
                onStartListening = {
                    accept(AmountInputStore.Intent.OnStartListening)
                },
                onStopListening = {
                    accept(AmountInputStore.Intent.OnStopListening)
                },
                onVoiceRecognitionError = { error ->
                    // Clear error and restart listening
                    accept(AmountInputStore.Intent.OnVoiceRecognitionError(error))
                },
                onSubmitVoice = { voiceText ->
                    // Parse the voice text again and apply it
                    accept(AmountInputStore.Intent.OnVoiceResult(voiceText))
                }
            )

            InputMode.Scan -> {
                CameraInputStep(
                    state = state.cameraInputState,
                    onStartScanning = { accept(AmountInputStore.Intent.OnStartCameraScan) },
                    onStopScanning = { accept(AmountInputStore.Intent.OnStopCameraScan) },
                    onAmountExtracted = {
                        accept(
                            AmountInputStore.Intent.OnAmountExtracted(it)
                        )
                    },
                    onScanResult = { text, confidence ->
                        accept(
                            AmountInputStore.Intent.OnReceiptScanResult(text, confidence)
                        )
                    },
                    onQRCodeScanned = { qrText ->
                        accept(AmountInputStore.Intent.OnQrCodeScanned(qrText))
                    },
                    onError = { error ->
                        accept(AmountInputStore.Intent.OnCameraScanError(error))
                    },
                    onNext = {
                        accept(AmountInputStore.Intent.OnKeypadNext)
                    }
                )
            }
        }
    }
}

package dev.esbi.mizan.feature.newtransaction.inputpart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.CameraInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.VoiceInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.widgets.InputModeContent
import dev.esbi.mizan.feature.newtransaction.store.NewTransactionStore
import dev.esbi.mizan.presentation.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.ui.components.currency.ExchangeRateEditor
import dev.esbi.mizan.ui.components.currency.HorizontalCurrencySelector
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun AmountPad(
    state: NewTransactionStore.State,
    accept: (NewTransactionStore.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MizanTheme.premium.background.tertiary)
    ) {
        Column(
            modifier = Modifier.padding(MizanTheme.premium.spacing.md)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                InputModeContent(
                    inputMode = state.inputMode,
                    onModeChange = { mode ->
                        accept(NewTransactionStore.Intent.OnModeChange(mode))
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            when (state.inputMode) {
                InputMode.Manual -> {
                    // Horizontal Currency Selector
                    if (state.availableCurrencies.isNotEmpty()) {
                        HorizontalCurrencySelector(
                            currencies = state.availableCurrencies,
                            selectedCurrency = state.selectedCurrency,
                            onCurrencySelected = { currency ->
                                accept(NewTransactionStore.Intent.SelectCurrency(currency))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Exchange Rate Editor (visible only for sub-currencies)
                    if (state.selectedCurrency != null && state.mainCurrency != null && !state.selectedCurrency.isMainCurrency) {
                        Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.sm))
                        ExchangeRateEditor(
                            selectedCurrency = state.selectedCurrency,
                            mainCurrency = state.mainCurrency,
                            enteredAmount = state.amountBigDecimal,
                            manualExchangeRate = state.manualExchangeRate,
                            equivalentAmount = state.equivalentInMainCurrency,
                            onRateChanged = { rate ->
                                accept(NewTransactionStore.Intent.UpdateManualRate(rate))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.md))

                    PremiumCalculatorKeypad(onNumberClick = {
                        accept(NewTransactionStore.AmountInputIntent.OnNumberClick(it))
                    })
                    val bgColor = if (state.canSubmit) {
                        MizanTheme.premium.colors.emerald
                    } else {
                        MizanTheme.premium.colors.surface2
                    }

                    Spacer(modifier = Modifier.height(MizanTheme.premium.spacing.xs))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(MizanTheme.premium.radius.xs))
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
                            color = if (state.canSubmit) {
                                Color.White
                            } else {
                                MizanTheme.premium.text.muted
                            }
                        )
                    }
                }

                InputMode.Voice -> VoiceInputStep(
                    modifier = Modifier.fillMaxWidth(),
                    state = state.voiceInputState,
                    onStartListening = {
                        accept(NewTransactionStore.VoiceRecognitionIntent.OnStartListening)
                    },
                    onStopListening = {
                        accept(NewTransactionStore.VoiceRecognitionIntent.OnStopListening)
                    },
                    onVoiceRecognitionError = { error ->
                        // Clear error and restart listening
                        accept(
                            NewTransactionStore.VoiceRecognitionIntent.OnVoiceRecognitionError(
                                error
                            )
                        )
                    },
                    onSubmitVoice = { voiceText ->
                        // Parse the voice text again and apply it
                        accept(NewTransactionStore.VoiceRecognitionIntent.OnVoiceResult(voiceText))
                    }
                )

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
                                NewTransactionStore.CameraScanIntent.OnReceiptScanResult(
                                    text,
                                    confidence
                                )
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

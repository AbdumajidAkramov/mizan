package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.CameraInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.KeypadContent
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.VoiceInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AmountInputContent(
    state: AmountInputState = AmountInputState(),
    accept: (AmountInputStore.Intent) -> Unit
) {
    Column {
        // Mode Switcher
        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
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
                    InputMode.Manual to IconValue(dev.esbi.mizan.ui.utils.Icons.ic_calculate),
                    InputMode.Voice to IconValue(dev.esbi.mizan.ui.utils.Icons.ic_mic),
                    InputMode.Scan to IconValue(dev.esbi.mizan.ui.utils.Icons.ic_camera_alt)
                ).forEach { (mode, icon) ->
                    val isSelected = state.inputMode == mode
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MizanTheme.premium.colors.surface3 else Color.Transparent)
                            .clickable {
                                accept(
                                    AmountInputStore.Intent.OnModeChange(
                                        mode
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        dev.esbi.mizan.ui.kit.icon.MizanIcon(
                            icon = icon,
                            modifier = Modifier.size(22.dp),
                            tint = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
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

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.KeypadContent
import dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes.VoiceInputStep
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputState
import dev.esbi.mizan.feature.newtransaction.amountinput.store.AmountInputStore
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun AmountInputContent(
    viewModel: AmountInputViewModel,
    onBackPressed: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState(initial = AmountInputState())

    Column(
        modifier = modifier.padding(top = 32.dp)
    ) {
        // Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MizanTheme.premium.spacing.lg,
                    vertical = MizanTheme.premium.spacing.md
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = MizanTheme.premium.colors.surface2,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MizanTheme.premium.text.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Enter Amount",
                style = MaterialTheme.typography.bodyLarge,
                color = MizanTheme.premium.text.secondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(32.dp)) // Balance the header
        }

        Spacer(Modifier.height(24.dp))

        // Mode Switcher
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
                                viewModel.onIntent(
                                    AmountInputStore.Intent.OnModeChange(
                                        mode
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        dev.esbi.mizan.ui.kit.icon.Icon(
                            icon = icon,
                            modifier = Modifier.size(22.dp),
                            tint = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        Spacer(modifier = Modifier.weight(1f))

        // Keypad Section - Bottom with elevation
        when (state.inputMode) {
            InputMode.Manual -> {
                KeypadContent(
                    state = state.keypadState,
                    onNumberClick = { key ->
                        viewModel.onIntent(
                            AmountInputStore.Intent.OnNumberClick(
                                key
                            )
                        )
                    },
                    onSubmit = {
                        viewModel.onIntent(AmountInputStore.Intent.OnSubmit)
                    }
                )
            }

            InputMode.Voice -> VoiceInputStep(
                state = state.voiceInputState,
                onStopListening = {
                    viewModel.onIntent(AmountInputStore.Intent.OnStopVoiceRecognition)
                },
                onStartListening = {
                    viewModel.onIntent(AmountInputStore.Intent.OnStartVoiceRecognition)
                },
                onVoiceRecognitionError = {
                    viewModel.onIntent(AmountInputStore.Intent.OnVoiceRecognitionError(it))
                },
                onSubmitVoice = {}
            )

            InputMode.Scan -> {
                /*CameraInputStep(
                    state = state,
                    accept = accept
                )*/
            }
        }
    }
}

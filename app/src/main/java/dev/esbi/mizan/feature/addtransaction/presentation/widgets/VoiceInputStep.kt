package dev.esbi.mizan.feature.addtransaction.presentation.widgets

import android.Manifest
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.esbi.mizan.R.drawable.ic_mic
import dev.esbi.mizan.R.drawable.ic_stop
import dev.esbi.mizan.feature.addtransaction.presentation.store.AddTransactionStore
import dev.esbi.mizan.feature.addtransaction.presentation.utils.VoiceRecognitionEvent
import dev.esbi.mizan.feature.addtransaction.presentation.utils.VoiceSpeechRecognizer
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun VoiceInputStep(
    state: AddTransactionStore.State,
    accept: (AddTransactionStore.Intent) -> Unit,
) {
    @OptIn(ExperimentalPermissionsApi::class)
    val recordAudioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val context = LocalContext.current

    // Initialize VoiceSpeechRecognizer
    val voiceRecognizer = remember { VoiceSpeechRecognizer(context) }

    // Collect voice recognition events
    val voiceEvents by voiceRecognizer.events.collectAsStateWithLifecycle()
    val isRecognizerListening by voiceRecognizer.isListening.collectAsStateWithLifecycle()

    // Handle voice recognition events
    LaunchedEffect(voiceEvents) {
        voiceEvents?.let { event ->
            when (event) {
                is VoiceRecognitionEvent.OnReadyForSpeech -> {
                    accept(AddTransactionStore.Intent.OnStartVoiceRecognition)
                }

                is VoiceRecognitionEvent.OnResults -> {
                    val text = event.results?.firstOrNull() ?: ""
                    if (text.isNotEmpty()) {
                        accept(
                            AddTransactionStore.Intent.OnVoiceRecognitionResult(
                                text = text,
                                confidence = 0.95f,
                                isFinal = true
                            )
                        )
                    }
                }

                is VoiceRecognitionEvent.OnError -> {
                    accept(
                        AddTransactionStore.Intent.OnVoiceRecognitionError(
                            error = event.error.message
                        )
                    )
                }

                is VoiceRecognitionEvent.OnEndOfSpeech -> {
                    accept(AddTransactionStore.Intent.OnStopVoiceRecognition)
                }

                else -> {}
            }
            voiceRecognizer.clearEvent()
        }
    }

    // Cleanup when leaving the screen
    LaunchedEffect(Unit) {
        return@LaunchedEffect voiceRecognizer.destroy()
    }

    PermissionHandler(
        permission = Manifest.permission.RECORD_AUDIO,
        permissionTitle = "Microphone Access",
        permissionDescription = "Microphone permission is required to record voice input for transactions. This allows you to say things like \"Lunch 15000\" to quickly add expenses.",
        onPermissionGranted = {
            VoiceInputStep(
                isListening = state.isVoiceListening || isRecognizerListening,
                recognizedText = state.voiceRecognitionText,
                error = state.voiceRecognitionError,
                amount = state.amountText,
                onStartListening = {
                    if (recordAudioPermissionState.status.isGranted) {
                        voiceRecognizer.startListening()
                    }
                },
                onStopListening = {
                    voiceRecognizer.stopListening()
                    accept(AddTransactionStore.Intent.OnStopVoiceRecognition)
                },
                onNext = { accept(AddTransactionStore.Intent.OnKeypadNext) }
            )
        },
        onPermissionDenied = { permission ->
            PermissionDeniedScreen(
                permission = permission,
                onRequestAgain = { recordAudioPermissionState.launchPermissionRequest() }
            )
        }
    )
}

@Composable
internal fun VoiceInputStep(
    isListening: Boolean,
    recognizedText: String,
    error: String?,
    amount: String,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        // Title and Instructions
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isListening) "Listening..." else "Voice Entry",
                style = MizanTheme.typography.headingLg,
                color = MizanTheme.premium.text.primary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

            Text(
                text = if (isListening) {
                    "Say something like \"Lunch $15\" or \"Coffee 5.50\""
                } else {
                    "Tap the microphone to start voice entry"
                },
                style = MizanTheme.typography.bodyMd,
                color = MizanTheme.premium.text.secondary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(1f))

        // Microphone Button with Animation
        VoiceMicrophoneButton(
            isListening = isListening,
            onStartListening = onStartListening,
            onStopListening = onStopListening
        )

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        // Recognized Text Display
        if (recognizedText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.lg)
                    .background(
                        color = MizanTheme.premium.colors.surface1,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.md)
                    )
                    .padding(MizanTheme.premium.spacing.md)
            ) {
                Text(
                    text = "\"$recognizedText\"",
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.primary,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Extracted Amount Display
            if (amount.isNotEmpty()) {
                Text(
                    text = "Amount: $amount",
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.colors.success
                )
            }
        }

        // Error Display
        error?.let { errorMessage ->
            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.lg)
                    .background(
                        color = MizanTheme.premium.colors.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(MizanTheme.premium.radius.md)
                    )
                    .padding(MizanTheme.premium.spacing.md)
            ) {
                Text(
                    text = errorMessage,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.colors.error,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Next Button
        if (amount.isNotEmpty() && !isListening) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.lg)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.success
                )
            ) {
                Text(
                    text = "Next",
                    style = MizanTheme.typography.labelLg,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.lg))
        }
    }
}

@Composable
private fun VoiceMicrophoneButton(
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit
) {
    // Pulse animation when listening
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    // Scale animation for button press
    val buttonScale by animateFloatAsState(
        targetValue = if (isListening) 1.1f else 1f,
        label = "buttonScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(120.dp)
    ) {
        // Pulse effect background
        if (isListening) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulseScale)
                    .alpha(pulseAlpha)
                    .background(
                        color = MizanTheme.premium.colors.success.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )
        }

        // Main button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .scale(buttonScale)
                .background(
                    color = if (isListening) {
                        MizanTheme.premium.colors.success
                    } else {
                        MizanTheme.premium.colors.primary
                    },
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (isListening) {
                        onStopListening()
                    } else {
                        onStartListening()
                    }
                }
        ) {
            Icon(
                icon = if (isListening) {
                    IconValue(ic_stop)
                } else {
                    IconValue(ic_mic)
                },
                modifier = Modifier.size(36.dp),
                tint = Color.White
            )
        }
    }
}

@Preview(
    name = "Voice Input - Idle",
    showBackground = true
)
@Composable
fun VoiceInputStepIdlePreview() {
    MizanTheme {
        VoiceInputStep(
            isListening = false,
            recognizedText = "",
            error = null,
            amount = "",
            onStartListening = {},
            onStopListening = {},
            onNext = {}
        )
    }
}

@Preview(
    name = "Voice Input - Listening",
    showBackground = true
)
@Composable
fun VoiceInputStepListeningPreview() {
    MizanTheme {
        VoiceInputStep(
            isListening = true,
            recognizedText = "Lunch $15.50",
            error = null,
            amount = "15.50",
            onStartListening = {},
            onStopListening = {},
            onNext = {}
        )
    }
}

@Preview(
    name = "Voice Input - Error",
    showBackground = true
)
@Composable
fun VoiceInputStepErrorPreview() {
    MizanTheme {
        VoiceInputStep(
            isListening = false,
            recognizedText = "",
            error = "Voice recognition failed. Please try again.",
            amount = "",
            onStartListening = {},
            onStopListening = {},
            onNext = {}
        )
    }
}

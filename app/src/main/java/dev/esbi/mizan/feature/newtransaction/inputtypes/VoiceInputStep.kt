package dev.esbi.mizan.feature.newtransaction.inputtypes

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun VoiceInputStep(
    state: VoiceInputState,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onVoiceRecognitionError: (String) -> Unit,
    onSubmitVoice: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    @OptIn(ExperimentalPermissionsApi::class)
    val recordAudioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    PermissionHandler(
        modifier = modifier,
        permission = Manifest.permission.RECORD_AUDIO,
        permissionTitle = "Microphone Access",
        permissionDescription = "Microphone permission is required to record voice input for transactions. This allows you to say things like \"Lunch 15000\" to quickly add expenses.",
        onPermissionGranted = {
            VoiceInputStepContent(
                state = state,
                onStartListening = {
                    if (recordAudioPermissionState.status.isGranted) {
                        onStartListening()
                    }
                },
                onStopListening = onStopListening,
                onVoiceRecognitionError = onVoiceRecognitionError,
                onSubmitVoice = onSubmitVoice
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
private fun VoiceInputStepContent(
    state: VoiceInputState,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onVoiceRecognitionError: (String) -> Unit,
    onSubmitVoice: (String) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val micScale by animateFloatAsState(
        targetValue = if (state.isListening) 1.1f else 1f,
        animationSpec = tween(200),
        label = "mic_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(MizanTheme.premium.spacing.xl))

        // Voice Input Button
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(micScale)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (state.isListening) {
                        onStopListening()
                    } else {
                        onStartListening()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Pulsing background when listening
            if (state.isListening) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .background(
                            color = MizanTheme.premium.colors.primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                )
            }

            // Microphone icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = if (state.isListening) 
                            MizanTheme.premium.colors.error 
                        else 
                            MizanTheme.premium.colors.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (state.isListening) {
                    MizanIcon(
                        icon = IconValue(Icons.ic_micoff),
                        contentDescription = "Stop listening",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    MizanIcon(
                        icon = IconValue(Icons.ic_mic),
                        contentDescription = "Start voice input",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        // Status text
        Text(
            text = if (state.isListening) "Listening..." else "Voice Entry",
            style = MizanTheme.premium.typography.headingMd,
            color = if (state.isListening) 
                MizanTheme.premium.colors.error 
            else 
                MizanTheme.premium.text.primary
        )

        Spacer(Modifier.height(MizanTheme.premium.spacing.md))

        // Voice recognition result
        if ((state.voiceResult ?: state.voiceRecognitionResult).isNotEmpty()) {
            val recognizedText = state.voiceResult ?: state.voiceRecognitionResult
            
            Text(
                text = "I heard:",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )

            Spacer(Modifier.height(MizanTheme.premium.spacing.xs))

            Text(
                text = "\"$recognizedText\"",
                style = MizanTheme.typography.bodyLg,
                color = MizanTheme.premium.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .background(
                        color = MizanTheme.premium.colors.surface1,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.md)
                    )
                    .padding(MizanTheme.premium.spacing.md)
            )

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Submit button if we have a result
            Button(
                onClick = { onSubmitVoice(recognizedText) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Use This",
                    style = MizanTheme.typography.labelLg,
                    color = Color.White
                )
            }
        }

        // Error message
        state.voiceRecognitionError?.let { error ->
            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            Text(
                text = error,
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.md)
                    .background(
                        color = MizanTheme.premium.colors.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(MizanTheme.premium.radius.md)
                    )
                    .padding(MizanTheme.premium.spacing.md)
            )

            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

            Button(
                onClick = {
                    onVoiceRecognitionError("")
                    onStartListening()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.premium.colors.error
                )
            ) {
                Text(
                    text = "Try Again",
                    style = MizanTheme.typography.labelSm,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Instructions
        if (!state.isListening && (state.voiceResult ?: state.voiceRecognitionResult).isEmpty()) {
            Text(
                text = "Tap the microphone and speak a transaction\nExample: \"50000 for Taxi\"",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))
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
            state = VoiceInputState(),
            onStartListening = {},
            onStopListening = {},
            onVoiceRecognitionError = {},
            onSubmitVoice = {}
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
            state = VoiceInputState(isListening = true),
            onStartListening = {},
            onStopListening = {},
            onVoiceRecognitionError = {},
            onSubmitVoice = {}
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
            state = VoiceInputState(voiceRecognitionError = "Voice recognition failed. Please try again."),
            onStartListening = {},
            onStopListening = {},
            onVoiceRecognitionError = {},
            onSubmitVoice = {}
        )
    }
}

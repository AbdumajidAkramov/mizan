package dev.esbi.mizan.feature.addtransaction.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons
import kotlinx.coroutines.delay

@Composable
fun PremiumEnhancedVoiceInput(
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Soundwave animation
    val bars = remember { mutableStateListOf(*Array(12) { 20f }) }

    LaunchedEffect(isListening) {
        if (isListening) {
            while (true) {
                for (i in bars.indices) {
                    bars[i] = (20..60).random().toFloat()
                }
                delay(100)
            }
        } else {
            for (i in bars.indices) {
                bars[i] = 20f
            }
        }
    }

    Column(
        modifier = modifier.padding(vertical = MizanTheme.premium.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mic Button
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(if (isListening) MizanTheme.premium.colors.success else MizanTheme.premium.colors.surface2)
                .clickable { if (isListening) onStopListening() else onStartListening() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon = if (isListening) {
                    IconValue(Icons.ic_micoff)
                } else {
                    IconValue(Icons.ic_mic)
                },
                tint = if (isListening) Color.White else MizanTheme.premium.text.primary,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        // Soundwave
        if (isListening) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(80.dp)
            ) {
                bars.forEach { height ->
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(height.dp)
                            .clip(CircleShape)
                            .background(MizanTheme.premium.colors.success.copy(alpha = 0.6f))
                    )
                }
            }
        }

        // Text
        Text(
            if (isListening) "I'm listening..." else "AI Voice Entry",
            style = MizanTheme.typography.headingMd,
            color = MizanTheme.premium.text.primary
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "\"Dinner \$45 yesterday\"",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
            Text(
                text = "\"Transfer \$2000 from Bank to Cash\"",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )
            Text(
                text = "\"Coffee 5 dollars\"",
                style = MizanTheme.typography.bodySm,
                color = MizanTheme.premium.text.tertiary
            )

        }
    }
}

@Preview
@Composable
fun PremiumEnhancedVoiceInputPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme() {
        PremiumEnhancedVoiceInput(
            isListening = true,
            onStartListening = {},
            onStopListening = {}
        )
    }
}

package dev.esbi.mizan.feature.newtransaction.amountinput

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.models.InputMode
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionIntent
import dev.esbi.mizan.feature.newtransaction.root.NewTransactionState
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
fun AmountInputStep(
    state: NewTransactionState,
    onIntent: (NewTransactionIntent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Amount Display Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
            ) {
                // Calculation String
                if (state.calculationString.isNotEmpty()) {
                    Text(
                        text = state.calculationString,
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
                
                // Large Amount Display
                Text(
                    text = "$${state.displayValue}",
                    style = MizanTheme.typography.bodyLg.copy(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MizanTheme.premium.text.primary
                )
                
                // Input Mode Switcher
                InputModeSwitcher(
                    currentMode = state.inputMode,
                    onModeChange = { mode -> onIntent(NewTransactionIntent.OnInputModeChange(mode)) }
                )
            }
        }
        
        // Input Method Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MizanTheme.premium.background.secondary,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .border(
                    width = 1.dp,
                    color = MizanTheme.premium.glass.border,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            when (state.inputMode) {
                InputMode.Manual -> ManualInputContent(
                    state = state,
                    onIntent = onIntent
                )
                InputMode.Voice -> VoiceInputContent(
                    state = state,
                    onIntent = onIntent
                )
                InputMode.Scan -> ScanInputContent(
                    state = state,
                    onIntent = onIntent
                )
            }
        }
    }
}

@Composable
private fun InputModeSwitcher(
    currentMode: InputMode,
    onModeChange: (InputMode) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                MizanTheme.premium.colors.surface1,
                RoundedCornerShape(50)
            )
            .padding(MizanTheme.premium.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputModeButton(
            mode = InputMode.Manual,
            currentMode = currentMode,
            onModeChange = onModeChange,
            icon = Icons.Default.Add,
            contentDescription = "Manual Input"
        )
        
        InputModeButton(
            mode = InputMode.Voice,
            currentMode = currentMode,
            onModeChange = onModeChange,
            icon = Icons.Default.Search,
            contentDescription = "Voice Input"
        )
        
        InputModeButton(
            mode = InputMode.Scan,
            currentMode = currentMode,
            onModeChange = onModeChange,
            icon = Icons.Default.Settings,
            contentDescription = "Scan Receipt"
        )
    }
}

@Composable
private fun InputModeButton(
    mode: InputMode,
    currentMode: InputMode,
    onModeChange: (InputMode) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    val isSelected = mode == currentMode
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.95f,
        label = "button_scale"
    )
    
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) {
                    when (mode) {
                        InputMode.Voice -> MizanTheme.premium.colors.emerald
                        else -> MizanTheme.premium.colors.surface3
                    }
                } else {
                    Color.Transparent
                }
            )
            .clickable { onModeChange(mode) }
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isSelected) {
                when (mode) {
                    InputMode.Voice -> Color.White
                    else -> MizanTheme.premium.text.primary
                }
            } else {
                MizanTheme.premium.text.tertiary
            },
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun ManualInputContent(
    state: NewTransactionState,
    onIntent: (NewTransactionIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MizanTheme.premium.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.md)
    ) {
        PremiumCalculatorKeypad(
            onNumberClick = { key -> onIntent(NewTransactionIntent.OnNumberClick(key)) }
        )
        
        Button(
            onClick = { onIntent(NewTransactionIntent.NavigateToType) },
            enabled = state.canProceedToType,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.canProceedToType) {
                    MizanTheme.premium.colors.emerald
                } else {
                    MizanTheme.premium.colors.surface3
                }
            )
        ) {
            Text(
                text = "Next",
                style = MizanTheme.typography.bodyMd,
                fontWeight = FontWeight.Medium,
                color = if (state.canProceedToType) {
                    Color.White
                } else {
                    MizanTheme.premium.text.muted
                }
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                icon = IconValue("chevron_right"),
                modifier = Modifier.size(20.dp),
                tint = if (state.canProceedToType) {
                    Color.White
                } else {
                    MizanTheme.premium.text.muted
                }
            )
        }
    }
}

@Composable
private fun VoiceInputContent(
    state: NewTransactionState,
    onIntent: (NewTransactionIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MizanTheme.premium.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Implement Voice Input UI
        Text(
            text = "Voice Input Coming Soon",
            style = MizanTheme.typography.bodyLg,
            color = MizanTheme.premium.text.secondary
        )
    }
}

@Composable
private fun ScanInputContent(
    state: NewTransactionState,
    onIntent: (NewTransactionIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MizanTheme.premium.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Implement Scan Input UI
        Text(
            text = "Scan Input Coming Soon",
            style = MizanTheme.typography.bodyLg,
            color = MizanTheme.premium.text.secondary
        )
    }
}

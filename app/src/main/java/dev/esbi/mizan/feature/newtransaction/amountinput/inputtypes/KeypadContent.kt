package dev.esbi.mizan.feature.newtransaction.amountinput.inputtypes


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
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.addtransaction.domain.model.Keypad
import dev.esbi.mizan.feature.addtransaction.presentation.utils.AutoResizingText
import dev.esbi.mizan.feature.addtransaction.presentation.widgets.PremiumCalculatorKeypad
import dev.esbi.mizan.feature.newtransaction.amountinput.store.state.KeypadState
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

@Composable
internal fun KeypadContent(
    state: KeypadState,
    onNumberClick: (Keypad) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Text
        Text(
            text = state.displayText,
            style = MizanTheme.typography.bodySm,
            color = MizanTheme.premium.text.tertiary,
            modifier = Modifier
        )
        // Display
        // Yangi holat (Double ga o'tkazib formatlaymiz):
        val formattedAmount = state.amountText.annotatedString(currency = state.currency)
        AutoResizingText(
            text = formattedAmount,
            style = MizanTheme.typography.displayXl,
            color = MizanTheme.premium.text.primary,
            maxLines = 1,
            minFontSize = 12.sp,
            modifier = Modifier
                .padding(vertical = MizanTheme.premium.spacing.md)
        )
        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MizanTheme.premium.background.secondary,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(MizanTheme.premium.spacing.lg)
            ) {
                PremiumCalculatorKeypad(onNumberClick = onNumberClick)

                val bgColor = if (state.canSubmit) {
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
                            onSubmit()
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
        }

    }
}

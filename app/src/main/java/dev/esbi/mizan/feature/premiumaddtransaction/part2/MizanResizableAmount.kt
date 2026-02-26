package dev.esbi.mizan.feature.premiumaddtransaction.part2

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal

@Composable
fun MizanResizableAmount(
    amount: BigDecimal,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    maxFontSize: TextUnit = 56.sp,
    minFontSize: TextUnit = 16.sp,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // 1. Hisoblangan maqsadli font o'lchami
    var targetFontSize by remember { mutableStateOf(maxFontSize) }

    // 2. Silliq o'tish animatsiyasi (Wallet ilovasidagidek)
    val animatedFontSize by animateFloatAsState(
        targetValue = targetFontSize.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "fontSizeAnim"
    )

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxWidthPx = with(density) { (maxWidth * 0.8f).toPx() }

        // 3. Amount yoki ekran kengligi o'zgarganda eng mos font o'lchamini topamiz
        LaunchedEffect(amount, maxWidth) {
            targetFontSize = calculateBestFontSize(
                amount = amount,
                maxFontSize = maxFontSize,
                minFontSize = minFontSize,
                maxWidthPx = maxWidthPx,
                textMeasurer = textMeasurer
            )
        }

        Text(
            text = formatMizanAmount(amount, fractionFontSize = (animatedFontSize * 0.5f).sp),
            modifier = Modifier.fillMaxWidth(),
            softWrap = false,
            maxLines = 1,
            color = color,
            textAlign = TextAlign.End,
            style = MizanTheme.typography.displayLg.copy(
                fontSize = animatedFontSize.sp,
                // Raqamlar o'zgarganda matn "sakrab" ketmasligi uchun tabular figures ishlatamiz
                fontFeatureSettings = "tnum"
            )
        )
    }
}

/**
 * Matnni chizmasdan turib, eng mos font o'lchamini aniqlaydigan funksiya
 */
private fun calculateBestFontSize(
    amount: BigDecimal,
    maxFontSize: TextUnit,
    minFontSize: TextUnit,
    maxWidthPx: Float,
    textMeasurer: TextMeasurer
): TextUnit {
    var currentSize = maxFontSize.value

    // Oddiy va tezkor iteratsiya (Binary search ham qilsa bo'ladi, lekin bu yetarli)
    while (currentSize > minFontSize.value) {
        val layoutResult = textMeasurer.measure(
            text = amount.toPlainString(), // Formatlangan matnni ham qo'shish mumkin
            style = TextStyle(fontSize = currentSize.sp),
            maxLines = 1
        )

        // Agar matn kengligi ajratilgan joydan kichik bo'lsa - to'xtaymiz
        if (layoutResult.size.width <= maxWidthPx * 0.95f) { // 5% zaxira joy qoldiramiz
            break
        }
        currentSize -= 2f // 2sp qadam bilan kichraytiramiz
    }

    return currentSize.coerceAtLeast(minFontSize.value).sp
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MizanResizableAmountPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme() {
        MizanResizableAmount(
            amount = BigDecimal("0.0"),
        )
    }
}

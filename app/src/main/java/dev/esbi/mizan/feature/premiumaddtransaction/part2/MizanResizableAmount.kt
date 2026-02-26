package dev.esbi.mizan.feature.premiumaddtransaction.part2

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
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
    maxFontSize: TextUnit = 56.sp,
    minFontSize: TextUnit = 24.sp,
    color: Color = Color.Unspecified
) {
    // 1. Har safar amount o'zgarganda fontSize'ni maxFontSize'ga qaytaramiz
    var fontSize by remember(amount) { mutableStateOf(maxFontSize) }
    var readyToDraw by remember(amount) { mutableStateOf(false) }

    val annotatedText = formatMizanAmount(amount, fractionFontSize = (fontSize.value * 0.5).sp)

    Text(
        text = annotatedText,
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                if (readyToDraw) drawContent()
            },
        softWrap = false,
        maxLines = 1,
        style = MizanTheme.typography.displayLg.copy(
            fontSize = fontSize,
            textAlign = TextAlign.End,
            color = color
        ),
        onTextLayout = { textLayoutResult ->
            // 2. Agar hali ham sig'mayotgan bo'lsa va font o'lchami min'dan katta bo'lsa - kichraytiramiz
            if (textLayoutResult.hasVisualOverflow && fontSize > minFontSize) {
                fontSize = (fontSize.value * 0.9f).sp
            } else {
                // Sig'gan bo'lsa, chizishga ruxsat beramiz
                readyToDraw = true
            }
        }
    )
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

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
    color: Color = Color.Unspecified,
    maxFontSize: TextUnit = 56.sp,
    minFontSize: TextUnit = 12.sp,
) {
    // Joriy font o'lchamini saqlash uchun state
    var fontSize by remember { mutableStateOf(maxFontSize) }
    var readyToDraw by remember { mutableStateOf(false) }

    // Avvalgi formatlash funksiyamizdan foydalanamiz
    val annotatedText = formatMizanAmount(amount, fractionFontSize = (fontSize.value * 0.5).sp)

    Text(
        text = annotatedText,
        modifier = modifier
            .fillMaxWidth()
            .drawWithContent {
                // Faqat font o'lchami aniqlab bo'lingandan keyin chizamiz (likillashni oldini olish uchun)
                if (readyToDraw) drawContent()
            },
        softWrap = false, // Yangi qatorga o'tmasligi shart
        maxLines = 1,
        color = color,
        textAlign = TextAlign.End,
        style = MizanTheme.typography.displayLg.copy(
            fontSize = fontSize,
            textAlign = TextAlign.Center
        ),
        onTextLayout = { textLayoutResult ->
            // Agar matn kengligi Box'dan oshib ketsa yoki qatorga sig'masa
            if (textLayoutResult.hasVisualOverflow && fontSize > minFontSize) {
                fontSize = (fontSize.value * 0.95f).sp // 10% ga kichraytiramiz
            } else {
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

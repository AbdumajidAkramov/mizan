package dev.esbi.mizan.features.addtransaction.utils

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizingText(
    text: AnnotatedString, // O'ZGARISH: String -> AnnotatedString
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = 1,
    minFontSize: TextUnit = 12.sp
) {
    var resizedTextStyle by remember(text) { mutableStateOf(style) }
    var shouldDraw by remember(text) { mutableStateOf(false) }

    Text(
        text = text,
        // Matn o'lchami hisoblanib, sig'adigan bo'lmaguncha uni shaffof (ko'rinmas) qilamiz
        color = if (shouldDraw) color else Color.Transparent,
        modifier = modifier,
        maxLines = maxLines,
        softWrap = false,
        overflow = TextOverflow.Visible,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth || result.didOverflowHeight) {
                val currentSize = resizedTextStyle.fontSize

                if (currentSize > minFontSize) {
                    val newSize = currentSize * 0.9f
                    resizedTextStyle = resizedTextStyle.copy(fontSize = newSize)
                } else {
                    shouldDraw = true
                }
            } else {
                shouldDraw = true
            }
        }
    )
}
package dev.esbi.mizan.feature.addtransaction.presentation.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    maxLines: Int = 1,
    maxFontSize: TextUnit = 18.sp,
    minFontSize: TextUnit = 10.sp,
    stepGranularity: TextUnit = 1.sp, // nechaga kichraytirib boradi
    overflow: TextOverflow = TextOverflow.Clip
) {
    var fontSize by remember(text, maxFontSize) { mutableStateOf(maxFontSize) }
    var readyToDraw by remember(text) { mutableStateOf(false) }

    Text(
        text = text,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        softWrap = false,
        style = style.copy(fontSize = fontSize),
        color = color,
        onTextLayout = { result: TextLayoutResult ->
            if (result.hasVisualOverflow && fontSize > minFontSize) {
                val next = (fontSize.value - stepGranularity.value).coerceAtLeast(minFontSize.value)
                fontSize = next.sp
                readyToDraw = false
            } else {
                readyToDraw = true
            }
        }
    )
}
package dev.esbi.mizan.feature.premiumaddtransaction.part1

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import kotlin.math.abs

@Composable
fun CurrencyWheelPicker(
    currencies: List<Currency>,
    initialCurrency: String,
    onCurrencySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MizanTheme.premium.text.tertiary
) {
    val itemHeight = 40.dp
    val visibleItemsCount = 3 // 1 ta markazda, 2 ta yonlarda
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = currencies.indexOfFirst { it.code == initialCurrency }
            .coerceAtLeast(0)
    )

    // Markaziy elementni aniqlash va callback yuborish
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerIndex = listState.firstVisibleItemIndex + (visibleItemsCount / 2)
            if (centerIndex < currencies.size) {
                onCurrencySelected(currencies[centerIndex].code)
                // Markazga "magnit" kabi yopishish (Snap)
                listState.animateScrollToItem(listState.firstVisibleItemIndex)
            }
        }
    }

    Box(
        modifier = modifier
            .width(80.dp)
            .height(itemHeight * visibleItemsCount),
        contentAlignment = Alignment.Center
    ) {
        // Markazdagi tanlov chiziqlari (iOS kabi)
        Divider(
            modifier = Modifier
                .offset(y = -itemHeight / 2)
                .fillMaxWidth(0.6f),
            color = MizanTheme.premium.colors.emerald.copy(alpha = 0.2f),
            thickness = 1.dp
        )
        Divider(
            modifier = Modifier
                .offset(y = itemHeight / 2)
                .fillMaxWidth(0.6f),
            color = MizanTheme.premium.colors.emerald.copy(alpha = 0.2f),
            thickness = 1.dp
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = itemHeight), // Bo'shliqlar uchun
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState), // Magnit effekti
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(currencies) { index, currency ->
                val opacity by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val visibleItemsInfo = layoutInfo.visibleItemsInfo
                        val itemInfo = visibleItemsInfo.find { it.index == index }
                            ?: return@derivedStateOf 0.3f

                        val center = layoutInfo.viewportEndOffset / 2
                        val itemCenter = itemInfo.offset + itemInfo.size / 2
                        val distanceFromCenter = abs(center - itemCenter).toFloat()
                        val maxDistance = layoutInfo.viewportEndOffset / 2f

                        // Markazdan uzoqlashgan sari kichrayadi va shaffoflashadi
                        (1f - (distanceFromCenter / maxDistance)).coerceIn(0.3f, 1f)
                    }
                }

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .graphicsLayer(
                            alpha = opacity,
                            scaleX = opacity,
                            scaleY = opacity
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currency.code,
                        style = MizanTheme.typography.displayMd.copy(
                            fontWeight = if (opacity > 0.9f) FontWeight.Bold else FontWeight.Normal,
                            color = if (opacity > 0.9f) MizanTheme.premium.colors.emerald else color
                        )
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun CurrencyWheelPickerPreview() {
    dev.esbi.mizan.ui.theme.MizanTheme() {
        CurrencyWheelPicker(
            currencies = listOf(
                Currency("EUR", "Euro", "€", java.math.BigDecimal.ONE, false),
                Currency("UZS", "Uzbek Som", "so'm", java.math.BigDecimal.ONE, true),
                Currency("RUB", "Russian Ruble", "₽", java.math.BigDecimal.ONE, false),
                Currency("USD", "US Dollar", "$", java.math.BigDecimal.ONE, false)
            ),
            initialCurrency = "UZS",
            onCurrencySelected = {},
            modifier = Modifier
        )
    }
}

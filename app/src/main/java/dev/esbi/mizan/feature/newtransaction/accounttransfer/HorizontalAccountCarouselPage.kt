package dev.esbi.mizan.feature.newtransaction.accounttransfer

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.data.local.entity.account.AccountEntity
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.utils.annotatedString

@Composable
fun HorizontalAccountCarousel(
    label: String,
    accounts: List<AccountEntity>,
    selectedAccountId: Long?,
    onSelectAccount: (Long) -> Unit
) {
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Label
        Text(
            text = label,
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.secondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Cards Container
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.fillMaxWidth(),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState) // Snap behavior
        ) {
            items(accounts) { account ->
                HorizontalAccountCard(
                    account = account,
                    isSelected = selectedAccountId == account.id,
                    onSelect = { onSelectAccount(account.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scroll Indicators (Dots)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            accounts.forEach { account ->
                val isSelected = selectedAccountId == account.id
                val width by animateDpAsState(if (isSelected) 24.dp else 8.dp, label = "dotWidth")

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(3.dp)
                        .width(width)
                        .background(
                            color = if (isSelected) MizanTheme.premium.colors.primary
                            else MizanTheme.premium.colors.surface3,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun HorizontalAccountCard(
    account: AccountEntity,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "scale")
    val borderColor = if (isSelected) MizanTheme.premium.colors.primaryDark else Color.Transparent
    val accountColor: Color =
        parseHexColor(account.color) ?: MizanTheme.premium.background.secondary
    Surface(
        onClick = onSelect,
        modifier = modifier
            .widthIn(min = 156.dp)
            .width(200.dp)
            .height(96.dp)
            .scale(scale)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(MizanTheme.premium.radius.xl),
        color = MizanTheme.premium.colors.primaryDark.copy(alpha = 0.8f),
        border = BorderStroke(2.dp, borderColor),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(accountColor, Color.Transparent),
                            start = Offset(0f, 0f),
                            end = Offset.Infinite
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // Bottom Section - Account Info
                Column {
                    Text(
                        text = account.name,
                        style = MizanTheme.typography.bodyMd,
                        color = if (isSelected) MizanTheme.premium.text.primary else MizanTheme.premium.text.secondary
                    )
                    Text(
                        text = account.balance.toString()
                            .annotatedString(currency = account.currencyCode),
                        style = MizanTheme.typography.headingMd,
                        color = if (isSelected) MizanTheme.premium.colors.primary else MizanTheme.premium.text.primary
                    )
                }
            }
        }
    }
}

private fun parseHexColor(value: String?): Color? {
    val v = value ?: return null
    if (!v.startsWith("#")) return null
    return try {
        Color(android.graphics.Color.parseColor(v))
    } catch (_: IllegalArgumentException) {
        null
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xfde
)
@Composable
fun HorizontalAccountCarouselPagePreview() {
    var selectedAccountId by remember { mutableLongStateOf(1L) }
    dev.esbi.mizan.ui.theme.MizanTheme {
        HorizontalAccountCarousel(
            label = "Transfer",
            accounts = listOf(
                AccountEntity(
                    id = 1,
                    groupId = 1,
                    name = "Cash",
                    type = Account.Type.CASH,
                    balance = 10000.23,
                    currencyCode = "USD",
                ),
                AccountEntity(
                    id = 2,
                    groupId = 1,
                    name = "Card",
                    type = Account.Type.CARD,
                    balance = 10000.34,
                    currencyCode = "UZS",
                ),
            ),
            selectedAccountId = selectedAccountId,
            onSelectAccount = {
                selectedAccountId = it
            },
        )
    }
}
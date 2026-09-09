package dev.esbi.mizan.ui.kit.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dev.esbi.mizan.design.utils.IconRes

@Composable
fun AccountIcon(
    iconName: String?,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val iconRes = when (iconName) {
        "ic_wallet" -> IconRes.ic_wallet
        "ic_card" -> IconRes.ic_wallet
        "ic_bank" -> IconRes.ic_home
        "ic_piggy_bank" -> IconRes.ic_heart
        "ic_cash" -> IconRes.ic_attach_money
        "ic_savings" -> IconRes.ic_heart
        "ic_investment" -> IconRes.ic_trend_up
        else -> IconRes.ic_wallet
    }

    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        tint = tint,
        modifier = modifier
    )
}

package dev.esbi.mizan.ui.kit.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.ui.utils.Icons

@Composable
fun AccountIcon(
    iconName: String?,
    accountType: Account.Type,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val iconRes = when (iconName) {
        "ic_wallet" -> Icons.ic_wallet
        "ic_card" -> Icons.ic_wallet
        "ic_bank" -> Icons.ic_home
        "ic_piggy_bank" -> Icons.ic_heart
        "ic_cash" -> Icons.ic_attach_money
        "ic_savings" -> Icons.ic_heart
        "ic_investment" -> Icons.ic_trend_up
        else -> when (accountType) {
            Account.Type.CASH -> Icons.ic_attach_money
            Account.Type.CARD -> Icons.ic_wallet
            Account.Type.BANK -> Icons.ic_home
            Account.Type.SAVINGS -> Icons.ic_heart
            Account.Type.DEBT -> Icons.ic_wallet
            Account.Type.CREDIT -> Icons.ic_wallet
            Account.Type.INVESTMENT -> Icons.ic_trend_up
        }
    }

    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        tint = tint,
        modifier = modifier
    )
}

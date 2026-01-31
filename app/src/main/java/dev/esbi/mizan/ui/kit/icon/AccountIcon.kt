package dev.esbi.mizan.ui.kit.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dev.esbi.mizan.R
import dev.esbi.mizan.domain.model.Account

@Composable
fun AccountIcon(
    iconName: String?,
    accountType: Account.Type,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val iconRes = when {
        iconName == "ic_wallet" -> R.drawable.ic_wallet
        iconName == "ic_card" -> R.drawable.ic_wallet
        iconName == "ic_bank" -> R.drawable.ic_home
        iconName == "ic_piggy_bank" -> R.drawable.ic_heart
        iconName == "ic_cash" -> R.drawable.ic_attach_money
        iconName == "ic_savings" -> R.drawable.ic_heart
        iconName == "ic_investment" -> R.drawable.ic_trend_up
        else -> when (accountType) {
            Account.Type.CASH -> R.drawable.ic_attach_money
            Account.Type.CARD -> R.drawable.ic_wallet
            Account.Type.SAVINGS -> R.drawable.ic_heart
            Account.Type.DEBT -> R.drawable.ic_wallet
            Account.Type.INVESTMENT -> R.drawable.ic_trend_up
        }
    }

    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        tint = tint,
        modifier = modifier
    )
}

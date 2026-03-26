package dev.esbi.mizan.feature.premiumaddtransaction.model

import dev.esbi.mizan.domain.model.Account

data class AccountsBottomSheetModel(
    val accounts: List<Account>,
    val selectedAccount: Account?,
)

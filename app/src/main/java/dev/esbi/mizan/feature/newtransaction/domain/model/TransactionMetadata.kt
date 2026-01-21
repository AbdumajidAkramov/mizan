package dev.esbi.mizan.feature.newtransaction.domain.model

import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category

data class TransactionMetadata(
    val categories: List<Category>,
    val accounts: List<Account>
)

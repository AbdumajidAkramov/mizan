package dev.esbi.mizan.feature.transfer.domain.repository

import dev.esbi.mizan.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface TransferRepository {
    fun observeAccounts(): Flow<List<Account>>
    suspend fun executeTransfer(
        sourceAccountId: Long,
        destinationAccountId: Long,
        amount: Double,
        targetAmount: Double?,
        note: String?
    )
}

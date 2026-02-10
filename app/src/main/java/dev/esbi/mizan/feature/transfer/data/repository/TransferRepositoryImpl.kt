package dev.esbi.mizan.feature.transfer.data.repository

import androidx.room.withTransaction
import dev.esbi.mizan.data.local.MizanDatabase
import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.data.local.mapper.toDomain
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.feature.transfer.domain.repository.TransferRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransferRepositoryImpl @Inject constructor(
    private val database: MizanDatabase,
    private val accountDao: AccountDao,
    private val transactionsDao: TransactionsDao
) : TransferRepository {

    override fun observeAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccountsWithCurrency().map { list ->
            list.filter { !it.account.isArchived }.map { it.toDomain() }
        }
    }

    override suspend fun executeTransfer(
        sourceAccountId: Long,
        destinationAccountId: Long,
        amount: Double,
        targetAmount: Double?,
        note: String?
    ) {
        database.withTransaction {
            // 1. Create TRANSFER transaction record
            val transferEntity = TransactionEntity(
                type = Transaction.Type.TRANSFER,
                amount = amount,
                targetAmount = targetAmount ?: amount,
                date = System.currentTimeMillis(),
                note = note,
                accountId = sourceAccountId,
                targetAccountId = destinationAccountId
            )
            transactionsDao.insertTransaction(transferEntity)

            // 2. Decrease source account balance
            accountDao.updateBalance(sourceAccountId, -amount)

            // 3. Increase destination account balance
            accountDao.updateBalance(destinationAccountId, targetAmount ?: amount)
        }
    }
}

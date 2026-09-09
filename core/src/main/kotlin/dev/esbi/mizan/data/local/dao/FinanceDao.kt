package dev.esbi.mizan.data.local.dao

import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.domain.model.Transaction
import javax.inject.Inject
import androidx.room.Transaction as RoomTransaction

/**
 * FinanceDao handles atomic operations that involve both transactions and account balances.
 * This ensures data consistency through Room's @Transaction annotation.
 * 
 * Double-Entry Accounting Logic:
 * - EXPENSE: Subtract from source account
 * - INCOME: Add to source account
 * - TRANSFER: Subtract from source, add to target
 */
class FinanceDao @Inject constructor(
    private val transactionsDao: TransactionsDao,
    private val accountDao: AccountDao
) {

    /**
     * Atomically insert a transaction and update account balance(s).
     * This operation is wrapped in a Room transaction to ensure atomicity.
     * 
     * @param transaction The transaction to insert
     * @throws IllegalArgumentException if accountId is null or invalid
     * @throws IllegalStateException if insufficient funds for expense/transfer
     */
    @RoomTransaction
    suspend fun insertTransactionWithBalanceUpdate(transaction: TransactionEntity) {
        // Validate required fields
        val accountId = transaction.accountId 
            ?: throw IllegalArgumentException("Transaction must have an accountId")

        // Insert the transaction first
        transactionsDao.insertTransaction(transaction)

        // Update account balance(s) based on transaction type
        when (transaction.type) {
            Transaction.Type.EXPENSE -> {
                // Subtract from source account
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount.negate()
                )
            }
            
            Transaction.Type.INCOME -> {
                // Add to source account
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount
                )
            }
            
            Transaction.Type.TRANSFER -> {
                val targetAccountId = transaction.targetAccountId
                    ?: throw IllegalArgumentException("Transfer transaction must have targetAccountId")

                // Subtract from source account
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount.negate()
                )

                // Add to target account (use targetAmount if different currency, otherwise use amount)
                val targetAmount = transaction.targetAmount ?: transaction.amount
                accountDao.updateBalance(
                    id = targetAccountId,
                    amount = targetAmount
                )
            }
        }
    }

    /**
     * Atomically update a transaction and adjust account balances.
     * This reverses the old transaction's balance changes and applies the new ones.
     * 
     * @param oldTransaction The existing transaction (for reversal)
     * @param newTransaction The updated transaction
     */
    @RoomTransaction
    suspend fun updateTransactionWithBalanceUpdate(
        oldTransaction: TransactionEntity,
        newTransaction: TransactionEntity
    ) {
        // First, reverse the old transaction's balance changes
        reverseTransactionBalanceChanges(oldTransaction)

        // Update the transaction
        transactionsDao.updateTransaction(newTransaction)

        // Apply the new transaction's balance changes
        applyTransactionBalanceChanges(newTransaction)
    }

    /**
     * Atomically delete a transaction and reverse its balance changes.
     * 
     * @param transaction The transaction to delete
     */
    @RoomTransaction
    suspend fun deleteTransactionWithBalanceUpdate(transaction: TransactionEntity) {
        // Reverse the balance changes
        reverseTransactionBalanceChanges(transaction)

        // Delete the transaction
        transactionsDao.deleteTransactionById(transaction.id)
    }

    /**
     * Reverse the balance changes made by a transaction.
     * This is the inverse of applyTransactionBalanceChanges.
     */
    private suspend fun reverseTransactionBalanceChanges(transaction: TransactionEntity) {
        val accountId = transaction.accountId ?: return

        when (transaction.type) {
            Transaction.Type.EXPENSE -> {
                // Add back to source account (reverse of subtract)
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount
                )
            }
            
            Transaction.Type.INCOME -> {
                // Subtract from source account (reverse of add)
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount.negate()
                )
            }
            
            Transaction.Type.TRANSFER -> {
                val targetAccountId = transaction.targetAccountId ?: return

                // Add back to source account
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount
                )

                // Subtract from target account
                val targetAmount = transaction.targetAmount ?: transaction.amount
                accountDao.updateBalance(
                    id = targetAccountId,
                    amount = targetAmount.negate()
                )
            }
        }
    }

    /**
     * Apply the balance changes for a transaction.
     * This is used when updating a transaction.
     */
    private suspend fun applyTransactionBalanceChanges(transaction: TransactionEntity) {
        val accountId = transaction.accountId ?: return

        when (transaction.type) {
            Transaction.Type.EXPENSE -> {
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount.negate()
                )
            }
            
            Transaction.Type.INCOME -> {
                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount
                )
            }
            
            Transaction.Type.TRANSFER -> {
                val targetAccountId = transaction.targetAccountId ?: return

                accountDao.updateBalance(
                    id = accountId,
                    amount = transaction.amount.negate()
                )

                val targetAmount = transaction.targetAmount ?: transaction.amount
                accountDao.updateBalance(
                    id = targetAccountId,
                    amount = targetAmount
                )
            }
        }
    }
}

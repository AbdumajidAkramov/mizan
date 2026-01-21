package dev.esbi.mizan.feature.newtransaction.data.repository

import dev.esbi.mizan.data.local.dao.AccountDao
import dev.esbi.mizan.data.local.dao.CategoryDao
import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.entity.TransactionEntity
import dev.esbi.mizan.feature.addtransaction.domain.model.Account
import dev.esbi.mizan.feature.addtransaction.domain.model.Category
import dev.esbi.mizan.feature.addtransaction.presentation.models.TransactionType
import dev.esbi.mizan.feature.newtransaction.data.mapper.NewTransactionMapper
import dev.esbi.mizan.feature.newtransaction.domain.repository.NewTransactionRepository
import kotlinx.coroutines.flow.first
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class NewTransactionRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao,
    private val transactionsDao: TransactionsDao
) : NewTransactionRepository {
    
    override suspend fun getCategories(type: TransactionType): Result<List<Category>> {
        return try {
            val typeString = when (type) {
                TransactionType.Expense -> "EXPENSE"
                TransactionType.Income -> "INCOME"
                TransactionType.Transfer -> "EXPENSE" // Transfers typically use expense categories
            }
            
            val categoryEntities = categoryDao.getCategoriesByType(typeString).first()
            val categories = NewTransactionMapper.mapToDomainCategories(categoryEntities)
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAccounts(): Result<List<Account>> {
        return try {
            val accountEntities = accountDao.getAllAccounts().first()
            val accounts = NewTransactionMapper.mapToDomainAccounts(accountEntities)
            Result.success(accounts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveTransaction(
        amount: Double,
        type: TransactionType,
        categoryId: String?,
        accountId: String,
        note: String,
        date: Long
    ): Result<Unit> {
        return try {
            val typeString = when (type) {
                TransactionType.Expense -> "EXPENSE"
                TransactionType.Income -> "INCOME"
                TransactionType.Transfer -> "TRANSFER"
            }
            
            // Get category details if categoryId is provided
            val categoryEntity = categoryId?.let { 
                categoryDao.getCategoryById(it) 
            }
            
            val transactionEntity = TransactionEntity(
                id = UUID.randomUUID().toString(),
                amount = amount,
                category = categoryEntity?.name ?: "Uncategorized",
                categoryLabel = categoryEntity?.name ?: "Uncategorized",
                description = note,
                date = Date(date),
                type = typeString,
                colorToken = categoryEntity?.color ?: "#666666"
            )
            
            transactionsDao.insertTransaction(transactionEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

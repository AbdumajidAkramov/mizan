package dev.esbi.mizan.feature.transactions.data.repository

import dev.esbi.mizan.data.local.dao.TransactionsDao
import dev.esbi.mizan.data.local.entity.TransactionDetailEntity
import dev.esbi.mizan.feature.transactions.data.mapper.toDomain
import dev.esbi.mizan.feature.transactions.domain.model.Transaction
import dev.esbi.mizan.feature.transactions.domain.model.TransactionFilter
import dev.esbi.mizan.feature.transactions.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

/**
 * Implementation of TransactionsRepository
 * Manages transactions with Room database and filtering
 */
class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsDao: TransactionsDao
) : TransactionsRepository {

    override fun observeTransactions(
        filter: TransactionFilter,
        searchQuery: String
    ): Flow<List<Transaction>> {
        val flow = when (filter) {
            TransactionFilter.ALL -> transactionsDao.observeAllTransactionDetails()
            TransactionFilter.INCOME -> transactionsDao.observeTransactionDetailsByType("income")
            TransactionFilter.EXPENSE -> transactionsDao.observeTransactionDetailsByType("expense")
        }

        return flow.map { entities ->
            if (entities.isEmpty()) {
                // Generate and insert mock data
                val mockData = generateMockTransactions()
                transactionsDao.insertTransactionDetails(mockData)
                mockData.map { it.toDomain() }
                    .filter { matchesSearch(it, searchQuery) }
            } else {
                entities.map { it.toDomain() }
                    .filter { matchesSearch(it, searchQuery) }
            }
        }
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return transactionsDao.getTransactionDetailById(id)?.toDomain()
    }

    override suspend fun refreshTransactions() {
        // In a real app, this would fetch from API
    }

    private fun matchesSearch(transaction: Transaction, query: String): Boolean {
        if (query.isBlank()) return true
        val lowerQuery = query.lowercase()
        return transaction.title.lowercase().contains(lowerQuery) ||
                transaction.categoryName.lowercase().contains(lowerQuery)
    }

    private fun generateMockTransactions(): List<TransactionDetailEntity> {
        val now = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000L
        
        return listOf(
            // Today
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Starbucks Coffee",
                amount = 5.75,
                type = "expense",
                category = "food",
                categoryName = "Food & Dining",
                timestamp = now - (2 * 60 * 60 * 1000),
                description = "Morning coffee"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Uber Ride",
                amount = 12.50,
                type = "expense",
                category = "transport",
                categoryName = "Transport",
                timestamp = now - (4 * 60 * 60 * 1000),
                description = "Ride to office"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Freelance Payment",
                amount = 850.00,
                type = "income",
                category = "income",
                categoryName = "Income",
                timestamp = now - (6 * 60 * 60 * 1000),
                description = "Client project"
            ),
            
            // Yesterday
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Grocery Shopping",
                amount = 87.45,
                type = "expense",
                category = "food",
                categoryName = "Food & Dining",
                timestamp = now - oneDay - (3 * 60 * 60 * 1000),
                description = "Weekly groceries"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Netflix Subscription",
                amount = 15.99,
                type = "expense",
                category = "entertainment",
                categoryName = "Entertainment",
                timestamp = now - oneDay - (5 * 60 * 60 * 1000),
                description = "Monthly subscription"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Gym Membership",
                amount = 45.00,
                type = "expense",
                category = "health",
                categoryName = "Health & Fitness",
                timestamp = now - oneDay - (8 * 60 * 60 * 1000),
                description = "Monthly membership"
            ),
            
            // 2 days ago
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Amazon Purchase",
                amount = 124.99,
                type = "expense",
                category = "shopping",
                categoryName = "Shopping",
                timestamp = now - (2 * oneDay) - (2 * 60 * 60 * 1000),
                description = "Electronics"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Salary Deposit",
                amount = 3500.00,
                type = "income",
                category = "income",
                categoryName = "Income",
                timestamp = now - (2 * oneDay) - (10 * 60 * 60 * 1000),
                description = "Monthly salary"
            ),
            
            // 3 days ago
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Restaurant Dinner",
                amount = 67.80,
                type = "expense",
                category = "food",
                categoryName = "Food & Dining",
                timestamp = now - (3 * oneDay) - (7 * 60 * 60 * 1000),
                description = "Dinner with friends"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Gas Station",
                amount = 45.00,
                type = "expense",
                category = "transport",
                categoryName = "Transport",
                timestamp = now - (3 * oneDay) - (9 * 60 * 60 * 1000),
                description = "Fuel"
            ),
            
            // 4 days ago
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Electricity Bill",
                amount = 89.50,
                type = "expense",
                category = "bills",
                categoryName = "Bills & Utilities",
                timestamp = now - (4 * oneDay) - (5 * 60 * 60 * 1000),
                description = "Monthly bill"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Movie Tickets",
                amount = 28.00,
                type = "expense",
                category = "entertainment",
                categoryName = "Entertainment",
                timestamp = now - (4 * oneDay) - (8 * 60 * 60 * 1000),
                description = "Cinema"
            ),
            
            // 5 days ago
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Pharmacy",
                amount = 34.25,
                type = "expense",
                category = "health",
                categoryName = "Health & Fitness",
                timestamp = now - (5 * oneDay) - (4 * 60 * 60 * 1000),
                description = "Medications"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Online Course",
                amount = 49.99,
                type = "expense",
                category = "shopping",
                categoryName = "Shopping",
                timestamp = now - (5 * oneDay) - (6 * 60 * 60 * 1000),
                description = "Education"
            ),
            TransactionDetailEntity(
                id = UUID.randomUUID().toString(),
                title = "Bonus Payment",
                amount = 500.00,
                type = "income",
                category = "income",
                categoryName = "Income",
                timestamp = now - (5 * oneDay) - (11 * 60 * 60 * 1000),
                description = "Performance bonus"
            )
        )
    }
}

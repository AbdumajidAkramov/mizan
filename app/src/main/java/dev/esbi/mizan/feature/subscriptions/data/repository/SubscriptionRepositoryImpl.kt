package dev.esbi.mizan.feature.subscriptions.data.repository

import dev.esbi.mizan.data.local.dao.SubscriptionDao
import dev.esbi.mizan.data.local.entity.SubscriptionEntity
import dev.esbi.mizan.feature.subscriptions.data.mapper.toDomain
import dev.esbi.mizan.feature.subscriptions.data.mapper.toEntity
import dev.esbi.mizan.presentation.feature.subscriptions.domain.model.Subscription
import dev.esbi.mizan.presentation.feature.subscriptions.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val subscriptionDao: SubscriptionDao
) : SubscriptionRepository {

    override fun observeAll(): Flow<List<Subscription>> {
        return subscriptionDao.observeAll().map { entities ->
            if (entities.isEmpty()) {
                val mocks = generateMockSubscriptions()
                mocks.forEach { subscriptionDao.insert(it) }
                mocks.map { it.toDomain() }
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun insert(subscription: Subscription): Long {
        return subscriptionDao.insert(subscription.toEntity())
    }

    override suspend fun update(subscription: Subscription) {
        subscriptionDao.update(subscription.toEntity())
    }

    override suspend fun delete(subscriptionId: Long) {
        subscriptionDao.deleteById(subscriptionId)
    }

    private fun generateMockSubscriptions(): List<SubscriptionEntity> = listOf(
        SubscriptionEntity(
            name = "Netflix",
            amount = java.math.BigDecimal("120000"),
            billingCycle = "MONTHLY",
            nextRenewalDate = System.currentTimeMillis() + 5L * 86_400_000,
            icon = "tv",
            color = "#E50914",
            category = "Entertainment"
        ),
        SubscriptionEntity(
            name = "Spotify",
            amount = java.math.BigDecimal("80000"),
            billingCycle = "MONTHLY",
            nextRenewalDate = System.currentTimeMillis() + 9L * 86_400_000,
            icon = "music",
            color = "#1DB954",
            category = "Music"
        ),
        SubscriptionEntity(
            name = "YouTube Premium",
            amount = java.math.BigDecimal("95000"),
            billingCycle = "MONTHLY",
            nextRenewalDate = System.currentTimeMillis() + 14L * 86_400_000,
            icon = "play",
            color = "#FF0000",
            category = "Entertainment"
        ),
        SubscriptionEntity(
            name = "iCloud Storage",
            amount = java.math.BigDecimal("50000"),
            billingCycle = "MONTHLY",
            nextRenewalDate = System.currentTimeMillis() + 19L * 86_400_000,
            icon = "cloud",
            color = "#007AFF",
            category = "Cloud Storage"
        ),
        SubscriptionEntity(
            name = "Adobe Creative",
            amount = java.math.BigDecimal("1200000"),
            billingCycle = "YEARLY",
            nextRenewalDate = System.currentTimeMillis() + 180L * 86_400_000,
            icon = "film",
            color = "#FF0000",
            category = "Productivity"
        ),
        SubscriptionEntity(
            name = "ChatGPT Plus",
            amount = java.math.BigDecimal("150000"),
            billingCycle = "MONTHLY",
            nextRenewalDate = System.currentTimeMillis() + 6L * 86_400_000,
            icon = "zap",
            color = "#10A37F",
            category = "AI Tools"
        )
    )
}

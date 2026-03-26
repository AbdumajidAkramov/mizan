package dev.esbi.mizan.feature.subscriptions.domain.repository

import dev.esbi.mizan.feature.subscriptions.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun observeAll(): Flow<List<Subscription>>
    suspend fun insert(subscription: Subscription): Long
    suspend fun update(subscription: Subscription)
    suspend fun delete(subscriptionId: Long)
}

package dev.esbi.mizan.feature.subscriptions.data.mapper

import dev.esbi.mizan.data.local.entity.SubscriptionEntity
import dev.esbi.mizan.feature.subscriptions.domain.model.BillingCycle
import dev.esbi.mizan.feature.subscriptions.domain.model.Subscription

fun SubscriptionEntity.toDomain(): Subscription = Subscription(
    id = id,
    name = name,
    amount = amount,
    billingCycle = try { BillingCycle.valueOf(billingCycle) } catch (_: Exception) { BillingCycle.MONTHLY },
    nextRenewalDate = nextRenewalDate,
    icon = icon,
    color = color,
    category = category
)

fun Subscription.toEntity(): SubscriptionEntity = SubscriptionEntity(
    id = id,
    name = name,
    amount = amount,
    billingCycle = billingCycle.name,
    nextRenewalDate = nextRenewalDate,
    icon = icon,
    color = color,
    category = category
)

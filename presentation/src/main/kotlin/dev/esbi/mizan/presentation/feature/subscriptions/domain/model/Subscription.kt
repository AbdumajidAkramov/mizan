package dev.esbi.mizan.presentation.feature.subscriptions.domain.model

import java.math.BigDecimal

data class Subscription(
    val id: Long = 0,
    val name: String,
    val amount: BigDecimal,
    val billingCycle: BillingCycle,
    val nextRenewalDate: Long,
    val icon: String,
    val color: String,
    val category: String = ""
) {
    val monthlyAmount: BigDecimal
        get() = when (billingCycle) {
            BillingCycle.MONTHLY -> amount
            BillingCycle.YEARLY -> amount.divide(BigDecimal("12"), 2, java.math.RoundingMode.HALF_EVEN)
        }
}

enum class BillingCycle {
    MONTHLY, YEARLY
}

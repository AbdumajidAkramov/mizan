package dev.esbi.mizan.feature.subscriptions.domain.model

data class Subscription(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val billingCycle: BillingCycle,
    val nextRenewalDate: Long,
    val icon: String,
    val color: String,
    val category: String = ""
) {
    val monthlyAmount: Double
        get() = when (billingCycle) {
            BillingCycle.MONTHLY -> amount
            BillingCycle.YEARLY -> amount / 12.0
        }
}

enum class BillingCycle {
    MONTHLY, YEARLY
}

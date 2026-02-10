package dev.esbi.mizan.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute() {
    @Serializable
    data object Dashboard : NavRoute()

    @Serializable
    data object FinancialMirror : NavRoute()

    @Serializable
    data object Budget : NavRoute()

    @Serializable
    data object Transactions : NavRoute()

    @Serializable
    data object Statistics : NavRoute()

    @Serializable
    data object Profile : NavRoute()

    @Serializable
    data class CategoryDetail(val categoryId: String) : NavRoute()

    @Serializable
    data object AmountInput : NavRoute()

    @Serializable
    data class CategorySelect(val transactionType: String) : NavRoute()

    @Serializable
    data object ManageCategories : NavRoute()

    @Serializable
    data object AccountManagement : NavRoute()

    @Serializable
    data object FinancialGoals : NavRoute()

    @Serializable
    data object Subscriptions : NavRoute()
}

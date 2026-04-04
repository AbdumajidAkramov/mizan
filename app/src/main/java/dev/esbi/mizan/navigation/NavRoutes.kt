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
    data object AccountSelector : NavRoute()

    @Serializable
    data object FinancialGoals : NavRoute()

    @Serializable
    data object Subscriptions : NavRoute()

    @Serializable
    data object AccountGroupManagement : NavRoute()

    @Serializable
    data class AddNewAccount(val accountId: Long? = null) : NavRoute()

    @Serializable
    data object SubCurrencyList : NavRoute()

    @Serializable
    data class SubCurrencySetting(val currencyCode: String) : NavRoute()

    @Serializable
    data object CurrencyPicker : NavRoute()

    @Serializable
    data object UserDefinedCurrency : NavRoute()

}

package dev.esbi.mizan.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
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
}

package dev.esbi.mizan.navigation

import dev.esbi.mizan.design.utils.IconRes
import dev.esbi.mizan.design.utils.Strings

data class BottomNavItem(
    val route: NavRoute,
    val labelResId: Int,
    val icon: Int,
    val isSpecial: Boolean = false
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = NavRoute.Dashboard,
        labelResId = Strings.nav_home,
        icon = IconRes.ic_home
    ),
    BottomNavItem(
        route = NavRoute.FinancialMirror,
        labelResId = Strings.nav_mirror,
        icon = IconRes.ic_ai_insight
    ),
    BottomNavItem(
        route = NavRoute.AddTransaction(),
        labelResId = Strings.add_new_transactions,
        icon = IconRes.ic_add,
        isSpecial = true
    ),
    BottomNavItem(
        route = NavRoute.Statistics,
        labelResId = Strings.nav_statistics,
        icon = IconRes.ic_trend_up
    ),
    BottomNavItem(
        route = NavRoute.Profile,
        labelResId = Strings.nav_profile,
        icon = IconRes.ic_profile
    )
)

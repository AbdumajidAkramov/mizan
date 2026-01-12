package dev.esbi.mizan.navigation

import dev.esbi.mizan.R
import dev.esbi.mizan.ui.utils.Icons

data class BottomNavItem(
    val route: NavRoute,
    val labelResId: Int,
    val icon: Int,
    val isSpecial: Boolean = false
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = NavRoute.Dashboard,
        labelResId = R.string.nav_home,
        icon = Icons.ic_home
    ),
    BottomNavItem(
        route = NavRoute.FinancialMirror,
        labelResId = R.string.nav_mirror,
        icon = Icons.ic_ai_insight
    ),
    BottomNavItem(
        route = NavRoute.Budget,
        labelResId = R.string.nav_budget,
        icon = Icons.ic_trend_up,
        isSpecial = true
    ),
    BottomNavItem(
        route = NavRoute.Statistics,
        labelResId = R.string.nav_statistics,
        icon = Icons.ic_trend_up
    ),
    BottomNavItem(
        route = NavRoute.Profile,
        labelResId = R.string.nav_profile,
        icon = Icons.ic_profile
    )
)

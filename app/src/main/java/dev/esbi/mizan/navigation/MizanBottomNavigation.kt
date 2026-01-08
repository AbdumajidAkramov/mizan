package dev.esbi.mizan.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.esbi.mizan.R
import dev.esbi.mizan.ui.theme.PremiumColors

data class BottomNavItem(
    val route: NavRoute,
    val labelResId: Int,
    val icon: Int
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = NavRoute.Dashboard,
        labelResId = R.string.nav_dashboard,
        icon = android.R.drawable.ic_menu_view
    ),
    BottomNavItem(
        route = NavRoute.Transactions,
        labelResId = R.string.nav_transactions,
        icon = android.R.drawable.ic_menu_recent_history
    ),
    BottomNavItem(
        route = NavRoute.Budget,
        labelResId = R.string.nav_budget,
        icon = android.R.drawable.ic_menu_sort_by_size
    ),
    BottomNavItem(
        route = NavRoute.Statistics,
        labelResId = R.string.nav_statistics,
        icon = android.R.drawable.ic_menu_compass
    ),
    BottomNavItem(
        route = NavRoute.Profile,
        labelResId = R.string.nav_profile,
        icon = android.R.drawable.ic_menu_myplaces
    )
)

@Composable
fun MizanBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(PremiumColors.Surface1),
        containerColor = PremiumColors.BgSecondary,
        tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute?.contains(item.route::class.simpleName ?: "") == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(NavRoute.Dashboard) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = stringResource(item.labelResId),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.labelResId),
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF667EEA),
                    selectedTextColor = Color(0xFF667EEA),
                    unselectedIconColor = PremiumColors.TextMuted,
                    unselectedTextColor = PremiumColors.TextMuted,
                    indicatorColor = Color(0xFF667EEA).copy(alpha = 0.1f)
                )
            )
        }
    }
}

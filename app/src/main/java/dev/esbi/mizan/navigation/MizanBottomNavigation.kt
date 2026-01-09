package dev.esbi.mizan.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.esbi.mizan.R
import dev.esbi.mizan.ui.kit.icon.Icon
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.MizanTheme
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
        labelResId = R.string.nav_dashboard,
        icon = Icons.ic_home
    ),
    BottomNavItem(
        route = NavRoute.Transactions,
        labelResId = R.string.nav_transactions,
        icon = Icons.ic_ai_insight
    ),
    BottomNavItem(
        route = NavRoute.Budget,
        labelResId = R.string.nav_budget,
        icon = Icons.ic_add,
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

@Composable
fun MizanBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    onAddExpense: () -> Unit = {}
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = modifier) {
        PremiumBottomBar(
            currentRoute = currentRoute,
            onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(NavRoute.Dashboard) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onAddExpense = onAddExpense
        )
    }
}


@Composable
private fun PremiumBottomBar(
    currentRoute: String?,
    onNavigate: (NavRoute) -> Unit,
    onAddExpense: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(32.dp)
                ),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFF1A1A2E).copy(alpha = 0.95f),
            shadowElevation = 16.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.08f),
                                Color.White.copy(alpha = 0.02f)
                            )
                        )
                    )
                    .padding(
                        horizontal = 4.dp,
                        vertical = 12.dp
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bottomNavItems.forEachIndexed { index, item ->
                        if (item.isSpecial) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val isSelected =
                                currentRoute?.contains(item.route::class.simpleName ?: "") == true
                            BottomNavItemView(
                                modifier = Modifier.weight(1f),
                                item = item,
                                isSelected = isSelected,
                                onClick = { onNavigate(item.route) }
                            )
                        }
                    }
                }
            }
        }

        PremiumFAB(
            onClick = onAddExpense,
            modifier = Modifier.offset(y = (-24).dp)
        )
    }
}

@Composable
private fun BottomNavItemView(
    modifier: Modifier,
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "nav_item_scale"
    )

    Column(
        modifier = modifier
            .clickable(
                indication = ripple(
                    bounded = false,
                    radius = 24.dp
                ),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.scale(scale),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(item.icon),
                contentDescription = stringResource(item.labelResId),
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) Color(0xFF667EEA) else Color(0xFF718096).copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(item.labelResId),
            fontSize = 10.sp,
            color = if (isSelected) Color(0xFF667EEA) else Color(0xFF718096).copy(alpha = 0.7f),
            modifier = Modifier.drawBehind {
                if (isSelected) {
                    drawCircle(
                        color = Color(0xFF667EEA),
                        radius = 2.dp.toPx(),
                        center = Offset(size.width / 2, size.height + 6.dp.toPx())
                    )
                }
            }
        )
    }
}

@Composable
private fun PremiumFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "fab_scale"
    )

    val pulseScale = remember { Animatable(1f) }
    val pulseAlpha = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        while (true) {
            pulseScale.animateTo(
                targetValue = 1.3f,
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
            pulseAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
            )
            pulseScale.snapTo(1f)
            pulseAlpha.snapTo(0.5f)
        }
    }

    Box(
        modifier = modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(60.dp)) {
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    )
                ),
                radius = 30.dp.toPx() * pulseScale.value,
                alpha = pulseAlpha.value
            )
        }

        Surface(
            modifier = Modifier
                .size(60.dp)
                .scale(scale)
                .clickable(
                    onClick = {
                        isPressed = true
                        onClick()
                        isPressed = false
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            shape = CircleShape,
            color = Color.Transparent,
            shadowElevation = 12.dp
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF667EEA),
                                Color(0xFF764BA2)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    contentDescription = "Add Expense",
                    icon = IconValue(Icons.ic_add),
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun PremiumBottomBarPreview() {
    MizanTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        Box(modifier = Modifier.background(color = Color.Blue)) {
            MizanBottomNavigation(
                navController = navController,
                modifier = Modifier.background(color = Color.Transparent),
                onAddExpense = {

                }
            )
        }
    }
}

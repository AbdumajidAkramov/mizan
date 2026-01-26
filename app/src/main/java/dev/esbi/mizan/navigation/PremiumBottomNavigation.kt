package dev.esbi.mizan.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.theme.shadows.premiumShadow

@Composable
fun PremiumBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Pastki qismdan joy tashlash va markazlashtirish
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Glass Container
        Box(
            modifier = Modifier
                .widthIn(max = 500.dp) // max-w-lg
                .fillMaxWidth()
                .background(
                    color = MizanTheme.premium.background.primary.copy(0.8f),
                    shape = RoundedCornerShape(MizanTheme.premium.radius.xxl)
                )
                .height(88.dp)
                .blur(30.dp),
        )
        Row(
            modifier = Modifier
                .widthIn(max = 500.dp) // max-w-lg
                .fillMaxWidth()
                // Shadow XL
                .premiumShadow(
                    shadowInfo = MizanTheme.premium.shadows.xl,
                    borderRadius = MizanTheme.premium.radius.xxl
                )
                // Glass Background & Border
                .background(
                    color = MizanTheme.premium.glass.bg,
                    shape = RoundedCornerShape(MizanTheme.premium.radius.xxl)
                )
                .border(
                    width = 1.dp,
                    color = MizanTheme.premium.glass.border,
                    shape = RoundedCornerShape(MizanTheme.premium.radius.xxl)
                )
                .padding(
                    horizontal = MizanTheme.premium.spacing.lg,
                    vertical = MizanTheme.premium.spacing.sm
                )
                .height(72.dp), // Balandlikni fix qildik, elementlar sig'ishi uchun
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEachIndexed { index, item ->
                if (item.isSpecial) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    val isSelected =
                        currentRoute?.contains(item.route::class.simpleName ?: "") == true
                    NavTabItem(
                        modifier = Modifier.weight(1f),
                        isActive = isSelected,
                        item = item,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(NavRoute.Dashboard) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }

        // --- FLOATING ACTION BUTTON (Markazda alohida qatlamda) ---
        PremiumFab(
            onClick = {
                navController.navigate(NavRoute.AmountInput)
            },
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-20).dp) // Yuqoriga ko'tarish
        )
    }
}

@Composable
private fun NavTabItem(
    modifier: Modifier,
    isActive: Boolean,
    item: BottomNavItem,
    onClick: () -> Unit
) {
    // Bosilganda "Ripple" effekti bo'lmasligi uchun interactionSource ishlatamiz
    val interactionSource = remember { MutableInteractionSource() }

    // Animatsiya: Active bo'lganda kattalashadi (scale-110)
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.2f else 1.0f,
        label = "scale"
    )

    val contentColor =
        if (isActive) MizanTheme.premium.colors.primary else MizanTheme.premium.text.tertiary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Ripple yo'q
                onClick = onClick
            )
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.scale(scale)
        ) {
            Icon(
                painter = painterResource(item.icon),
                contentDescription = stringResource(item.labelResId),
                modifier = Modifier.size(24.dp),
                tint = contentColor
            )
        }

        // Label
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(item.labelResId),
            style = MizanTheme.premium.typography.labelSm, // body-xs font-medium
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .drawBehind {
                    if (isActive) {
                        drawCircle(
                            color = contentColor,
                            radius = 2.dp.toPx(),
                            center = Offset(size.width / 2, size.height + 6.dp.toPx())
                        )
                    }
                }
                .alpha(if (isActive) 1f else 0.7f)
        )
    }
}

@Composable
private fun PremiumFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulse Animatsiyasi (ping)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(64.dp) // 60px ga yaqin
    ) {
        // 1. Orqa fon animatsiyasi (Pulse Effect)
        Box(
            modifier = Modifier
                .size(60.dp)
                .scale(pulseScale)
                .alpha(pulseAlpha)
                .background(
                    brush = MizanTheme.premium.gradients.primary,
                    shape = CircleShape
                )
        )

        // 2. Asosiy Tugma
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                // Glow Shadow
                .premiumShadow(
                    shadowInfo = MizanTheme.premium.shadows.glowPrimary,
                    borderRadius = 50.dp
                )
                // Gradient Background
                .background(
                    brush = MizanTheme.premium.gradients.primary,
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
        ) {
            dev.esbi.mizan.ui.kit.icon.MizanIcon(
                icon = IconValue(dev.esbi.mizan.ui.utils.Icons.ic_add),
                contentDescription = "Add Expense",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Preview(
    name = "Light Mode",
    showBackground = true,
    group = "Premium UI"
)
@Composable
fun PreviewPremiumNavigationLight() {
    // Holatni (State) saqlash, shunda Previewda bosib ko'rsa bo'ladi
    var currentTab by remember { mutableStateOf("Dashboard") }
    val navController = rememberNavController()
    MizanTheme(darkTheme = false) {
        // Butun ekran foni (Glass effekt ko'rinishi uchun)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MizanTheme.premium.background.primary),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Ekranning o'rtasida shunchaki matn (Fon borligini bilish uchun)
            Text(
                text = "Mizan Asosiy Ekran",
                modifier = Modifier.align(Alignment.Center),
                color = MizanTheme.premium.text.tertiary
            )

            // Bizning komponent
            PremiumBottomNavigation(
                navController = navController,
                modifier = Modifier.background(color = Color.Transparent),
            )
        }
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    backgroundColor = 0xFF0F0F23, // Dark fon rangi
    group = "Premium UI"
)
@Composable
fun PreviewPremiumNavigationDark() {
    var currentTab by remember { mutableStateOf("Profile") }
    val navController = rememberNavController()
    MizanTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MizanTheme.premium.background.primary),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Tungi Rejim",
                modifier = Modifier.align(Alignment.Center),
                color = MizanTheme.premium.text.tertiary
            )

            PremiumBottomNavigation(
                navController = navController,
                modifier = Modifier.background(color = Color.Transparent),
            )
        }
    }
}
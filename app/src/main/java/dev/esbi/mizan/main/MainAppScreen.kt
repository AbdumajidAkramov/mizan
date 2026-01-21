package dev.esbi.mizan.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import dev.esbi.mizan.feature.newtransaction.di.NewTransactionStoreProvider
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.esbi.mizan.navigation.MizanNavHost
import dev.esbi.mizan.navigation.PremiumBottomNavigation
import dev.esbi.mizan.navigation.bottomNavItems
import dev.esbi.mizan.ui.theme.colors.MizanTheme

@Composable
internal fun MainAppScreen(
    viewModelFactory: ViewModelProvider.Factory,
    newTransactionStoreProvider: NewTransactionStoreProvider,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    // Hozirgi ekran holatini kuzatish
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // 1. Biz faqat "Asosiy Tablar"ni ajratib olamiz (AddTransaction'dan tashqari)
    // 2. Joriy ekran shu tablardan biri ekanligini 'hasRoute' yoki 'hierarchy' orqali tekshiramiz.
    val showBottomBar = remember(currentDestination) {
        bottomNavItems.any { item ->
            // Agar bu maxsus tugma bo'lsa (AddTransaction), unda bar ko'rinmasligi kerak
            if (item.isSpecial) return@any false

            // Type-Safe tekshiruv (Navigation 2.8.0+)
            // Itemning classi (route) joriy destination ierarxiyasida bormi?
            currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        // Scaffoldning o'z bottomBar'ini ishlatmaymiz (Box ichida manual joylaymiz)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. Asosiy Content
            MizanNavHost(
                // Pastki qism paddingini olib tashlaymiz, chunki bar content ustida turadi
                // yoki innerPadding.calculateBottomPadding() ishlatmang.
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                navController = navController,
                viewModelFactory = viewModelFactory,
                newTransactionStoreProvider = newTransactionStoreProvider
            )

            // 2. Bottom Navigation (Overlay)
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it }, // Pastdan chiqadi
                exit = slideOutVertically { it }, // Pastga tushib ketadi
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                // Konteyner (Paddinglar shu yerda bo'lishi kerak)
                Box(
                    modifier = Modifier
                        .padding(horizontal = MizanTheme.premium.spacing.md)
                        .padding(bottom = MizanTheme.premium.spacing.md)
                ) {
                    PremiumBottomNavigation(
                        navController = navController
                        // modifier bu yerda kerak emas, chunki PremiumBottomNavigation ichida o'rnatilgan bo'lishi mumkin
                    )
                }
            }
        }
    }
}
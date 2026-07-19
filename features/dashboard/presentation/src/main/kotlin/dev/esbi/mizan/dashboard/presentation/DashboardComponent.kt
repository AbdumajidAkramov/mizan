package dev.esbi.mizan.dashboard.presentation

import kotlinx.coroutines.flow.StateFlow

interface DashboardComponent {

    val state: StateFlow<DashboardState>

    fun onEvent(event: DashboardEvent)

    // Tashqi dunyoga (Glavniy Navigatorga) signal
    sealed interface Output {
        object OpenAddTransaction : Output
        object OpenAllTransactions : Output
        object OpenProfile : Output
    }
}
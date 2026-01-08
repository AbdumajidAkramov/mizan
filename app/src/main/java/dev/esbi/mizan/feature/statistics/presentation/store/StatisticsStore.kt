package dev.esbi.mizan.feature.statistics.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.feature.statistics.domain.model.StatisticsSummary
import dev.esbi.mizan.feature.statistics.domain.model.TimePeriod

/**
 * MVIKotlin Store for Statistics Screen
 * Manages period selection and statistics display
 */
interface StatisticsStore : Store<StatisticsStore.Intent, StatisticsStore.State, StatisticsStore.Label> {

    sealed interface Action {
        data object Init : Action
    }

    sealed interface Intent {
        data object Refresh : Intent
        data object Retry : Intent
        data class SelectPeriod(val period: TimePeriod) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val summary: StatisticsSummary? = null,
        val error: String? = null,
        val selectedPeriod: TimePeriod = TimePeriod.MONTH
    )

    sealed interface Label {
        data class ShowError(val message: String) : Label
    }
}

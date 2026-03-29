package dev.esbi.mizan.presentation.feature.dashboard.domain.repository

import dev.esbi.mizan.presentation.feature.dashboard.domain.model.DashboardSummary
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    fun observeDashboardSummary(): Flow<DashboardSummary?>

    suspend fun refreshDashboard()
}

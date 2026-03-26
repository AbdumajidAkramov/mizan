package dev.esbi.mizan.feature.dashboard.domain.repository

import dev.esbi.mizan.feature.dashboard.domain.model.DashboardSummary
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    fun observeDashboardSummary(): Flow<DashboardSummary?>

    suspend fun refreshDashboard()
}

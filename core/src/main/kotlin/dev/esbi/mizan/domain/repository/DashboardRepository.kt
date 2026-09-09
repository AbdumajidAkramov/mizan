package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.dashboard.DashboardSummary
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {

    fun observeDashboardSummary(): Flow<DashboardSummary?>

    suspend fun refreshDashboard()
}
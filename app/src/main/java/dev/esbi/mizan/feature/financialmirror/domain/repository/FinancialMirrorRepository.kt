package dev.esbi.mizan.feature.financialmirror.domain.repository

import dev.esbi.mizan.feature.financialmirror.domain.model.FinancialMirrorSummary
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Financial Mirror feature
 * Provides access to projections, risk analysis, scenarios, and AI recommendations
 */
interface FinancialMirrorRepository {
    
    /**
     * Observe complete financial mirror data
     */
    fun observeFinancialMirror(): Flow<FinancialMirrorSummary>
    
    /**
     * Refresh financial mirror data
     * Recalculates projections and risk analysis based on current financial state
     */
    suspend fun refreshFinancialMirror()
}

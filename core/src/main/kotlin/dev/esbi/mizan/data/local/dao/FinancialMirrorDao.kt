package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.esbi.mizan.data.local.entity.FinancialProjectionEntity
import dev.esbi.mizan.data.local.entity.InvestmentOpportunityEntity
import dev.esbi.mizan.data.local.entity.RiskFactorEntity
import dev.esbi.mizan.data.local.entity.TimeMachineScenarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialMirrorDao {
    
    @Query("SELECT * FROM financial_projections ORDER BY year ASC")
    fun observeProjections(): Flow<List<FinancialProjectionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjections(projections: List<FinancialProjectionEntity>)
    
    @Query("DELETE FROM financial_projections")
    suspend fun clearProjections()
    
    @Query("SELECT * FROM risk_factors")
    fun observeRiskFactors(): Flow<List<RiskFactorEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRiskFactors(riskFactors: List<RiskFactorEntity>)
    
    @Query("DELETE FROM risk_factors")
    suspend fun clearRiskFactors()
    
    @Query("SELECT * FROM time_machine_scenarios")
    fun observeScenarios(): Flow<List<TimeMachineScenarioEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenarios(scenarios: List<TimeMachineScenarioEntity>)
    
    @Query("DELETE FROM time_machine_scenarios")
    suspend fun clearScenarios()
    
    @Query("SELECT * FROM investment_opportunities")
    fun observeOpportunities(): Flow<List<InvestmentOpportunityEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpportunities(opportunities: List<InvestmentOpportunityEntity>)
    
    @Query("DELETE FROM investment_opportunities")
    suspend fun clearOpportunities()
}

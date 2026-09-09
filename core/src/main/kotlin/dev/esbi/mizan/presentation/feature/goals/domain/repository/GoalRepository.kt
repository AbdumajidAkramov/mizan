package dev.esbi.mizan.presentation.feature.goals.domain.repository

import dev.esbi.mizan.presentation.feature.goals.domain.model.Goal
import kotlinx.coroutines.flow.Flow

import java.math.BigDecimal

interface GoalRepository {
    fun observeAll(): Flow<List<Goal>>
    suspend fun insert(goal: Goal): Long
    suspend fun update(goal: Goal)
    suspend fun delete(goalId: Long)
    suspend fun addAmount(goalId: Long, amount: BigDecimal)
}

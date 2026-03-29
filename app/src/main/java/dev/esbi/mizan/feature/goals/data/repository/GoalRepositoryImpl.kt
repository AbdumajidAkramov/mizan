package dev.esbi.mizan.feature.goals.data.repository

import dev.esbi.mizan.data.local.dao.GoalDao
import dev.esbi.mizan.data.local.entity.GoalEntity
import dev.esbi.mizan.feature.goals.data.mapper.toDomain
import dev.esbi.mizan.feature.goals.data.mapper.toEntity
import dev.esbi.mizan.presentation.feature.goals.domain.model.Goal
import dev.esbi.mizan.presentation.feature.goals.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {

    override fun observeAll(): Flow<List<Goal>> {
        return goalDao.observeAll().map { entities ->
            if (entities.isEmpty()) {
                val mocks = generateMockGoals()
                mocks.forEach { goalDao.insert(it) }
                mocks.map { it.toDomain() }
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun insert(goal: Goal): Long {
        return goalDao.insert(goal.toEntity())
    }

    override suspend fun update(goal: Goal) {
        goalDao.update(goal.toEntity())
    }

    override suspend fun delete(goalId: Long) {
        goalDao.deleteById(goalId)
    }

    override suspend fun addAmount(goalId: Long, amount: Double) {
        goalDao.addAmount(goalId, amount)
    }

    private fun generateMockGoals(): List<GoalEntity> = listOf(
        GoalEntity(
            name = "Dream Home Down Payment",
            targetAmount = 30_000_000.0,
            currentAmount = 22_500_000.0,
            deadline = 1830297600000L,
            icon = "home",
            color = "#0EA5E9"
        ),
        GoalEntity(
            name = "New Car",
            targetAmount = 20_000_000.0,
            currentAmount = 8_000_000.0,
            deadline = 1782691200000L,
            icon = "car",
            color = "#8B5CF6"
        ),
        GoalEntity(
            name = "Europe Vacation",
            targetAmount = 5_000_000.0,
            currentAmount = 4_500_000.0,
            deadline = 1786665600000L,
            icon = "plane",
            color = "#F59E0B"
        ),
        GoalEntity(
            name = "Emergency Fund",
            targetAmount = 15_000_000.0,
            currentAmount = 15_000_000.0,
            deadline = 1767225600000L,
            icon = "trophy",
            color = "#10B981"
        )
    )
}

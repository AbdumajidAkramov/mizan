package dev.esbi.mizan.feature.goals.data.mapper

import dev.esbi.mizan.data.local.entity.GoalEntity
import dev.esbi.mizan.feature.goals.domain.model.Goal

fun GoalEntity.toDomain(): Goal = Goal(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    icon = icon,
    color = color
)

fun Goal.toEntity(): GoalEntity = GoalEntity(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    icon = icon,
    color = color
)

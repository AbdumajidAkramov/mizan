package dev.esbi.mizan.feature.financialmirror.data.mapper

import dev.esbi.mizan.data.local.entity.FinancialProjectionEntity
import dev.esbi.mizan.data.local.entity.InvestmentOpportunityEntity
import dev.esbi.mizan.data.local.entity.RiskFactorEntity
import dev.esbi.mizan.data.local.entity.TimeMachineScenarioEntity
import dev.esbi.mizan.feature.financialmirror.domain.model.FinancialProjection
import dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentOpportunity
import dev.esbi.mizan.feature.financialmirror.domain.model.InvestmentType
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskFactor
import dev.esbi.mizan.feature.financialmirror.domain.model.RiskStatus
import dev.esbi.mizan.feature.financialmirror.domain.model.ScenarioIconType
import dev.esbi.mizan.feature.financialmirror.domain.model.TimeMachineScenario

fun FinancialProjectionEntity.toDomain(): FinancialProjection {
    return FinancialProjection(
        year = year,
        conservative = conservative,
        realistic = realistic,
        optimistic = optimistic
    )
}

fun FinancialProjection.toEntity(): FinancialProjectionEntity {
    return FinancialProjectionEntity(
        year = year,
        conservative = conservative,
        realistic = realistic,
        optimistic = optimistic
    )
}

fun RiskFactorEntity.toDomain(): RiskFactor {
    return RiskFactor(
        category = category,
        score = score,
        status = when (status.uppercase()) {
            "GOOD" -> RiskStatus.GOOD
            "FAIR" -> RiskStatus.FAIR
            "WARNING" -> RiskStatus.WARNING
            else -> RiskStatus.FAIR
        },
        description = description
    )
}

fun RiskFactor.toEntity(): RiskFactorEntity {
    return RiskFactorEntity(
        category = category,
        score = score,
        status = status.name,
        description = description
    )
}

fun TimeMachineScenarioEntity.toDomain(): TimeMachineScenario {
    return TimeMachineScenario(
        id = id,
        title = title,
        timeline = timeline,
        impact = impact,
        description = description,
        iconType = when (iconType.uppercase()) {
            "TARGET" -> ScenarioIconType.TARGET
            "TRENDING_UP" -> ScenarioIconType.TRENDING_UP
            "SPARKLES" -> ScenarioIconType.SPARKLES
            "DOLLAR_SIGN" -> ScenarioIconType.DOLLAR_SIGN
            "PIGGY_BANK" -> ScenarioIconType.PIGGY_BANK
            else -> ScenarioIconType.TARGET
        }
    )
}

fun TimeMachineScenario.toEntity(): TimeMachineScenarioEntity {
    return TimeMachineScenarioEntity(
        id = id,
        title = title,
        timeline = timeline,
        impact = impact,
        description = description,
        iconType = iconType.name
    )
}

fun InvestmentOpportunityEntity.toDomain(): InvestmentOpportunity {
    return InvestmentOpportunity(
        id = id,
        title = title,
        type = when (type.uppercase()) {
            "LOW_RISK" -> InvestmentType.LOW_RISK
            "MEDIUM_RISK" -> InvestmentType.MEDIUM_RISK
            "HIGH_RISK" -> InvestmentType.HIGH_RISK
            "LONG_TERM" -> InvestmentType.LONG_TERM
            else -> InvestmentType.MEDIUM_RISK
        },
        apy = apy,
        minAmount = minAmount,
        description = description,
        colorHex = colorHex
    )
}

fun InvestmentOpportunity.toEntity(): InvestmentOpportunityEntity {
    return InvestmentOpportunityEntity(
        id = id,
        title = title,
        type = type.name,
        apy = apy,
        minAmount = minAmount,
        description = description,
        colorHex = colorHex
    )
}

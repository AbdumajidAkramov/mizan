package dev.esbi.mizan.domain.model.dashboard

import dev.esbi.mizan.domain.model.Currency
import java.math.BigDecimal

/**
 * Aggregated portfolio total computed by [dev.esbi.mizan.domain.usecase.GetDashboardSummaryUseCase].
 *
 * [totalBalance] is kept at a high internal scale (see use case) so downstream layers can
 * round to [mainCurrency].decimalDigits only at the final UI formatting stage.
 */
data class DashboardTotal(
    val totalBalance: BigDecimal,
    val mainCurrency: Currency
)
package dev.esbi.mizan.domain.usecase

import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.dashboard.DashboardTotal
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.CurrencyRepository
import dev.esbi.mizan.domain.usecase.GetDashboardSummaryUseCase.Companion.INTERNAL_SCALE
import dev.esbi.mizan.domain.util.CurrencyConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Computes the Grand Total balance across every active, non-excluded account,
 * converting each balance into the Main Currency via [CurrencyConverter].
 *
 * The flow re-emits whenever either the account list or the currency list changes,
 * so UI consumers get a live portfolio value as balances and exchange rates update.
 *
 * Internal arithmetic is performed at [INTERNAL_SCALE] decimal places to avoid
 * precision loss during chained multiply/divide operations. Rounding to the
 * currency's display precision is the responsibility of the UI (CurrencyFormatter).
 */
class GetDashboardSummaryUseCase(
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository,
    private val currencyConverter: CurrencyConverter
) {

    operator fun invoke(): Flow<DashboardTotal> {
        return combine(
            accountRepository.observeAccounts(),
            currencyRepository.observeCurrencies()
        ) { accounts, currencies ->
            val mainCurrency = currencies.firstOrNull { it.isMainCurrency }
                ?: Currency.UZS

            val total = accounts
                .asSequence()
                .filter { !it.isDeleted }
                .filter { !it.isArchived }
                .filter { !it.excludeFromTotal }
                .fold(BigDecimal.ZERO) { acc, account ->
                    val amountInBase = currencyConverter.convertToBase(
                        amount = account.balance,
                        fromCurrencyCode = account.currency.code
                    )
                    acc.add(amountInBase)
                }
                .setScale(INTERNAL_SCALE, RoundingMode.HALF_EVEN)

            DashboardTotal(
                totalBalance = total,
                mainCurrency = mainCurrency
            )
        }
    }

    companion object {
        const val INTERNAL_SCALE: Int = 12
    }
}

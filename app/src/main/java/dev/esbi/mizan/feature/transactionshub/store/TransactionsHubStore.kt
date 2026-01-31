package dev.esbi.mizan.feature.transactionshub.store

import com.arkivanov.mvikotlin.core.store.Store
import dev.esbi.mizan.domain.model.Account
import dev.esbi.mizan.domain.model.Category
import dev.esbi.mizan.domain.model.Transaction
import java.time.LocalDate
import java.time.YearMonth

/**
 * MVI Store for TransactionsHub screen
 * Manages state for multi-view transaction center with Daily, Calendar, Monthly, Summary tabs
 */
interface TransactionsHubStore :
    Store<TransactionsHubStore.Intent, TransactionsHubStore.State, TransactionsHubStore.Label> {

    /**
     * Available tabs in the Transactions Hub
     */
    enum class Tab {
        Daily,
        Calendar,
        Monthly,
        Summary,
        Description
    }

    /**
     * Summary data for income/expense/total
     */
    data class MonthlySummary(
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0,
        val balance: Double = 0.0
    )

    /**
     * Grouped transactions by date for Daily view
     */
    data class DailyGroup(
        val date: Long,
        val dateFormatted: String,
        val transactions: List<Transaction>,
        val dayTotal: Double
    )

    /**
     * Calendar day summary for Calendar view
     */
    data class CalendarDaySummary(
        val date: LocalDate,
        val income: Double = 0.0,
        val expense: Double = 0.0,
        val balance: Double = 0.0,
        val transactionCount: Int = 0
    )

    /**
     * Weekly summary for Monthly view
     */
    data class WeeklySummary(
        val weekNumber: Int,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val dateRangeFormatted: String,
        val income: Double = 0.0,
        val expense: Double = 0.0,
        val balance: Double = 0.0,
        val transactions: List<Transaction> = emptyList(),
        val isExpanded: Boolean = true
    )

    /**
     * Category summary for Summary view
     */
    data class CategorySummary(
        val categoryId: Long?,
        val categoryName: String,
        val categoryColor: String,
        val categoryIcon: String?,
        val totalAmount: Double,
        val percentage: Float,
        val transactionCount: Int
    )

    /**
     * Account summary for Summary view
     */
    data class AccountSummary(
        val accountId: Long?,
        val accountName: String,
        val totalAmount: Double,
        val percentage: Float,
        val transactionCount: Int
    )

    /**
     * Description group for Description view
     */
    data class DescriptionGroup(
        val description: String,
        val transactions: List<Transaction>,
        val netAmount: Double,
        val incomeAmount: Double,
        val expenseAmount: Double,
        val transactionCount: Int,
        val dateRange: String,
        val isExpanded: Boolean = false
    )

    /**
     * State for the Transactions Hub
     */
    data class State(
        val selectedTab: Tab = Tab.Daily,
        val transactions: List<Transaction> = emptyList(),
        val categories: List<Category> = emptyList(),
        val accounts: List<Account> = emptyList(),
        val summary: MonthlySummary = MonthlySummary(),
        val currentMonth: YearMonth = YearMonth.now(),
        val dailyGroups: List<DailyGroup> = emptyList(),
        val selectedDate: LocalDate = LocalDate.now(),
        val calendarDays: List<CalendarDaySummary?> = emptyList(),
        val daysWithTransactions: Int = 0,
        val weeklySummaries: List<WeeklySummary> = emptyList(),
        val expenseCategorySummaries: List<CategorySummary> = emptyList(),
        val incomeCategorySummaries: List<CategorySummary> = emptyList(),
        val savingsRate: Float = 0f,
        val expenseAccountSummaries: List<AccountSummary> = emptyList(),
        val incomeAccountSummaries: List<AccountSummary> = emptyList(),
        val descriptionSearchQuery: String = "",
        val descriptionGroups: List<DescriptionGroup> = emptyList(),
        val isAllDescriptionsExpanded: Boolean = false,
        val isLoading: Boolean = true,
        val error: String? = null
    )

    /**
     * User intents
     */
    sealed interface Intent {
        data class SelectTab(val tab: Tab) : Intent
        data class ChangeMonth(val month: YearMonth) : Intent
        data object LoadData : Intent
        data object PreviousMonth : Intent
        data object NextMonth : Intent
        data class TransactionClicked(val transaction: Transaction) : Intent
        data object AddTransactionClicked : Intent
        data object BackClicked : Intent
        data class SelectDate(val date: LocalDate) : Intent
        data class ToggleWeekExpansion(val weekNumber: Int) : Intent
        data class SearchDescription(val query: String) : Intent
        data class ToggleDescriptionGroup(val description: String) : Intent
        data object ToggleExpandAllDescriptions : Intent
    }

    /**
     * Internal messages for reducer
     */
    sealed interface Message {
        data class TabSelected(val tab: Tab) : Message
        data class MonthChanged(val month: YearMonth) : Message
        data class TransactionsLoaded(val transactions: List<Transaction>) : Message
        data class CategoriesLoaded(val categories: List<Category>) : Message
        data class AccountsLoaded(val accounts: List<Account>) : Message
        data class SummaryCalculated(val summary: MonthlySummary) : Message
        data class DailyGroupsCalculated(val groups: List<DailyGroup>) : Message
        data class LoadingChanged(val isLoading: Boolean) : Message
        data class ErrorOccurred(val error: String?) : Message
        data class DateSelected(val date: LocalDate) : Message
        data class CalendarDataCalculated(
            val calendarDays: List<CalendarDaySummary?>,
            val daysWithTransactions: Int
        ) : Message
        data class WeeklySummariesCalculated(val summaries: List<WeeklySummary>) : Message
        data class WeekExpansionToggled(val weekNumber: Int) : Message
        data class CategorySummariesCalculated(
            val expenseSummaries: List<CategorySummary>,
            val incomeSummaries: List<CategorySummary>,
            val savingsRate: Float
        ) : Message
        data class AccountSummariesCalculated(
            val expenseAccountSummaries: List<AccountSummary>,
            val incomeAccountSummaries: List<AccountSummary>
        ) : Message
        data class DescriptionGroupsCalculated(val groups: List<DescriptionGroup>) : Message
        data class DescriptionSearchQueryChanged(val query: String) : Message
        data class DescriptionGroupToggled(val description: String) : Message
        data class AllDescriptionsExpandedToggled(val isExpanded: Boolean) : Message
    }

    /**
     * Labels for side effects (navigation, etc.)
     */
    sealed interface Label {
        data object NavigateBack : Label
        data object NavigateToAddTransaction : Label
        data class NavigateToEditTransaction(val transaction: Transaction) : Label
    }
}

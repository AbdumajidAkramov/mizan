package dev.esbi.mizan.feature.transactionshub.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dev.esbi.mizan.domain.model.Transaction
import dev.esbi.mizan.domain.repository.AccountRepository
import dev.esbi.mizan.domain.repository.TransactionRepository
import dev.esbi.mizan.feature.addtransaction.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Executor for TransactionsHub - handles business logic and side effects
 */
internal class TransactionsHubExecutor(
    private val mainDispatcher: CoroutineDispatcher,
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) : CoroutineExecutor<
        TransactionsHubStore.Intent,
        Unit,
        TransactionsHubStore.State,
        TransactionsHubStore.Message,
        TransactionsHubStore.Label>(
    mainContext = mainDispatcher
) {

    override fun executeAction(action: Unit) {
        loadData()
    }

    override fun executeIntent(intent: TransactionsHubStore.Intent) {
        when (intent) {
            is TransactionsHubStore.Intent.SelectTab -> {
                dispatch(TransactionsHubStore.Message.TabSelected(intent.tab))
            }

            is TransactionsHubStore.Intent.ChangeMonth -> {
                dispatch(TransactionsHubStore.Message.MonthChanged(intent.month))
                recalculateDataForMonth(intent.month)
            }

            is TransactionsHubStore.Intent.PreviousMonth -> {
                val newMonth = state().currentMonth.minusMonths(1)
                dispatch(TransactionsHubStore.Message.MonthChanged(newMonth))
                recalculateDataForMonth(newMonth)
            }

            is TransactionsHubStore.Intent.NextMonth -> {
                val newMonth = state().currentMonth.plusMonths(1)
                dispatch(TransactionsHubStore.Message.MonthChanged(newMonth))
                recalculateDataForMonth(newMonth)
            }

            is TransactionsHubStore.Intent.LoadData -> {
                loadData()
            }

            is TransactionsHubStore.Intent.TransactionClicked -> {
                publish(TransactionsHubStore.Label.NavigateToEditTransaction(intent.transaction))
            }

            is TransactionsHubStore.Intent.AddTransactionClicked -> {
                publish(TransactionsHubStore.Label.NavigateToAddTransaction)
            }

            is TransactionsHubStore.Intent.BackClicked -> {
                publish(TransactionsHubStore.Label.NavigateBack)
            }

            is TransactionsHubStore.Intent.SelectDate -> {
                dispatch(TransactionsHubStore.Message.DateSelected(intent.date))
            }

            is TransactionsHubStore.Intent.ToggleWeekExpansion -> {
                dispatch(TransactionsHubStore.Message.WeekExpansionToggled(intent.weekNumber))
            }

            is TransactionsHubStore.Intent.SearchDescription -> {
                dispatch(TransactionsHubStore.Message.DescriptionSearchQueryChanged(intent.query))
                calculateDescriptionGroups(state().transactions, intent.query)
            }

            is TransactionsHubStore.Intent.ToggleDescriptionGroup -> {
                dispatch(TransactionsHubStore.Message.DescriptionGroupToggled(intent.description))
            }

            is TransactionsHubStore.Intent.ToggleExpandAllDescriptions -> {
                val newExpanded = !state().isAllDescriptionsExpanded
                dispatch(TransactionsHubStore.Message.AllDescriptionsExpandedToggled(newExpanded))
            }
        }
    }

    private fun loadData() {
        dispatch(TransactionsHubStore.Message.LoadingChanged(true))

        // Observe transactions
        transactionRepository.observeTransactions()
            .onEach { transactions ->
                dispatch(TransactionsHubStore.Message.TransactionsLoaded(transactions))
                recalculateDataForMonth(state().currentMonth, transactions)
            }
            .launchIn(scope)

        // Observe accounts
        accountRepository.observeAccounts()
            .onEach { accounts ->
                dispatch(TransactionsHubStore.Message.AccountsLoaded(accounts))
            }
            .launchIn(scope)

        // Load categories
        categoryRepository.getCategoriesByType("EXPENSE")
            .onEach { categories ->
                dispatch(TransactionsHubStore.Message.CategoriesLoaded(categories))
                dispatch(TransactionsHubStore.Message.LoadingChanged(false))
            }
            .launchIn(scope)
    }

    private fun recalculateDataForMonth(
        month: YearMonth,
        transactions: List<Transaction> = state().transactions
    ) {
        // Filter transactions for the selected month
        val monthTransactions = transactions.filter { txn ->
            val txnDate = Instant.ofEpochMilli(txn.date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            YearMonth.from(txnDate) == month
        }

        // Calculate summary
        val totalIncome = monthTransactions
            .filter { it.type == Transaction.Type.INCOME }
            .sumOf { it.amount }

        val totalExpense = monthTransactions
            .filter { it.type == Transaction.Type.EXPENSE }
            .sumOf { it.amount }

        val balance = totalIncome - totalExpense

        dispatch(
            TransactionsHubStore.Message.SummaryCalculated(
                TransactionsHubStore.MonthlySummary(
                    totalIncome = totalIncome,
                    totalExpense = totalExpense,
                    balance = balance
                )
            )
        )

        // Group by date for Daily view
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val grouped = monthTransactions
            .groupBy { txn ->
                // Get start of day
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = txn.date
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                calendar.timeInMillis
            }
            .map { (dateMillis, txns) ->
                val dayTotal = txns.sumOf { txn ->
                    when (txn.type) {
                        Transaction.Type.INCOME -> txn.amount
                        Transaction.Type.EXPENSE -> -txn.amount
                        Transaction.Type.TRANSFER -> 0.0
                    }
                }
                TransactionsHubStore.DailyGroup(
                    date = dateMillis,
                    dateFormatted = dateFormat.format(Date(dateMillis)),
                    transactions = txns.sortedByDescending { it.date },
                    dayTotal = dayTotal
                )
            }
            .sortedByDescending { it.date }

        dispatch(TransactionsHubStore.Message.DailyGroupsCalculated(grouped))

        // Generate calendar data
        calculateCalendarData(month, transactions)

        // Generate weekly summaries for Monthly view
        calculateWeeklySummaries(month, monthTransactions)

        // Generate category summaries for Summary view
        calculateCategorySummaries(monthTransactions)

        // Generate description groups for Description view
        calculateDescriptionGroups(monthTransactions, state().descriptionSearchQuery)
    }

    private fun calculateCalendarData(
        month: YearMonth,
        transactions: List<Transaction>
    ) {
        val calendarDays = mutableListOf<TransactionsHubStore.CalendarDaySummary?>()

        // Get first day of month
        val firstDayOfMonth = month.atDay(1)
        val lastDayOfMonth = month.atEndOfMonth()

        // Get day of week for first day (Monday = 1, Sunday = 7)
        var firstDayOfWeek = firstDayOfMonth.dayOfWeek.value - 1 // Convert to 0-indexed (Mon=0)

        // Add empty cells for days before month starts
        for (i in 0 until firstDayOfWeek) {
            calendarDays.add(null)
        }

        // Add days of the month
        var daysWithTransactions = 0
        var currentDate = firstDayOfMonth

        while (!currentDate.isAfter(lastDayOfMonth)) {
            // Filter transactions for this day
            val dayTransactions = transactions.filter { txn ->
                val txnDate = Instant.ofEpochMilli(txn.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                txnDate == currentDate
            }

            val income = dayTransactions
                .filter { it.type == Transaction.Type.INCOME }
                .sumOf { it.amount }

            val expense = dayTransactions
                .filter { it.type == Transaction.Type.EXPENSE }
                .sumOf { it.amount }

            val balance = income - expense
            val transactionCount = dayTransactions.size

            if (transactionCount > 0) {
                daysWithTransactions++
            }

            calendarDays.add(
                TransactionsHubStore.CalendarDaySummary(
                    date = currentDate,
                    income = income,
                    expense = expense,
                    balance = balance,
                    transactionCount = transactionCount
                )
            )

            currentDate = currentDate.plusDays(1)
        }

        dispatch(
            TransactionsHubStore.Message.CalendarDataCalculated(
                calendarDays = calendarDays,
                daysWithTransactions = daysWithTransactions
            )
        )
    }

    private fun calculateWeeklySummaries(
        month: YearMonth,
        monthTransactions: List<Transaction>
    ) {
        val weeklySummaries = mutableListOf<TransactionsHubStore.WeeklySummary>()

        val firstDayOfMonth = month.atDay(1)
        val lastDayOfMonth = month.atEndOfMonth()

        var weekNumber = 1
        var currentWeekStart = firstDayOfMonth

        while (!currentWeekStart.isAfter(lastDayOfMonth)) {
            // Calculate week end (6 days later or end of month)
            var currentWeekEnd = currentWeekStart.plusDays(6)
            if (currentWeekEnd.isAfter(lastDayOfMonth)) {
                currentWeekEnd = lastDayOfMonth
            }

            // Filter transactions for this week
            val weekTransactions = monthTransactions.filter { txn ->
                val txnDate = Instant.ofEpochMilli(txn.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                !txnDate.isBefore(currentWeekStart) && !txnDate.isAfter(currentWeekEnd)
            }

            // Calculate totals
            val income = weekTransactions
                .filter { it.type == Transaction.Type.INCOME }
                .sumOf { it.amount }

            val expense = weekTransactions
                .filter { it.type == Transaction.Type.EXPENSE }
                .sumOf { it.amount }

            val balance = income - expense

            // Format date range
            val dateRangeFormatted = formatWeekDateRange(currentWeekStart, currentWeekEnd)

            // Only add weeks that have transactions
            if (weekTransactions.isNotEmpty()) {
                weeklySummaries.add(
                    TransactionsHubStore.WeeklySummary(
                        weekNumber = weekNumber,
                        startDate = currentWeekStart,
                        endDate = currentWeekEnd,
                        dateRangeFormatted = dateRangeFormatted,
                        income = income,
                        expense = expense,
                        balance = balance,
                        transactions = weekTransactions.sortedByDescending { it.date },
                        isExpanded = true
                    )
                )
            }

            // Move to next week
            currentWeekStart = currentWeekEnd.plusDays(1)
            weekNumber++
        }

        dispatch(TransactionsHubStore.Message.WeeklySummariesCalculated(weeklySummaries))
    }

    private fun formatWeekDateRange(start: LocalDate, end: LocalDate): String {
        val startMonth = start.monthValue.toString().padStart(2, '0')
        val startDay = start.dayOfMonth.toString().padStart(2, '0')
        val endMonth = end.monthValue.toString().padStart(2, '0')
        val endDay = end.dayOfMonth.toString().padStart(2, '0')

        return if (start.month == end.month) {
            "$startMonth/$startDay ~ $endDay"
        } else {
            "$startMonth/$startDay ~ $endMonth/$endDay"
        }
    }

    private fun calculateCategorySummaries(monthTransactions: List<Transaction>) {
        val categories = state().categories

        // Calculate expense category summaries
        val expenseTransactions = monthTransactions.filter { it.type == Transaction.Type.EXPENSE }
        val totalExpense = expenseTransactions.sumOf { it.amount }

        val expenseSummaries = expenseTransactions
            .groupBy { it.categoryId }
            .map { (categoryId, transactions) ->
                val category = categories.find { it.id == categoryId }
                val totalAmount = transactions.sumOf { it.amount }
                val percentage = if (totalExpense > 0) (totalAmount / totalExpense * 100).toFloat() else 0f

                TransactionsHubStore.CategorySummary(
                    categoryId = categoryId,
                    categoryName = category?.name ?: "Uncategorized",
                    categoryColor = category?.color ?: "#FF6B9D",
                    categoryIcon = category?.iconName,
                    totalAmount = totalAmount,
                    percentage = percentage,
                    transactionCount = transactions.size
                )
            }
            .sortedByDescending { it.totalAmount }

        // Calculate income category summaries
        val incomeTransactions = monthTransactions.filter { it.type == Transaction.Type.INCOME }
        val totalIncome = incomeTransactions.sumOf { it.amount }

        val incomeSummaries = incomeTransactions
            .groupBy { it.categoryId }
            .map { (categoryId, transactions) ->
                val category = categories.find { it.id == categoryId }
                val totalAmount = transactions.sumOf { it.amount }
                val percentage = if (totalIncome > 0) (totalAmount / totalIncome * 100).toFloat() else 0f

                TransactionsHubStore.CategorySummary(
                    categoryId = categoryId,
                    categoryName = category?.name ?: "Income",
                    categoryColor = category?.color ?: "#10B981",
                    categoryIcon = category?.iconName,
                    totalAmount = totalAmount,
                    percentage = percentage,
                    transactionCount = transactions.size
                )
            }
            .sortedByDescending { it.totalAmount }

        // Calculate savings rate
        val netSavings = totalIncome - totalExpense
        val savingsRate = if (totalIncome > 0) (netSavings / totalIncome * 100).toFloat() else 0f

        dispatch(
            TransactionsHubStore.Message.CategorySummariesCalculated(
                expenseSummaries = expenseSummaries,
                incomeSummaries = incomeSummaries,
                savingsRate = savingsRate
            )
        )
    }

    private fun calculateDescriptionGroups(
        monthTransactions: List<Transaction>,
        searchQuery: String
    ) {
        // Filter by search query if present
        val filteredTransactions = if (searchQuery.isNotBlank()) {
            monthTransactions.filter { txn ->
                txn.note?.contains(searchQuery, ignoreCase = true) == true
            }
        } else {
            monthTransactions
        }

        // Group by description (note)
        val groupMap = filteredTransactions.groupBy { it.note?.trim() ?: "" }

        // Convert to DescriptionGroup list
        val groups = groupMap.map { (description, transactions) ->
            val sortedTxns = transactions.sortedByDescending { it.date }

            val incomeAmount = transactions
                .filter { it.type == Transaction.Type.INCOME }
                .sumOf { it.amount }

            val expenseAmount = transactions
                .filter { it.type == Transaction.Type.EXPENSE }
                .sumOf { it.amount }

            val netAmount = incomeAmount - expenseAmount

            // Calculate date range
            val dates = transactions.map { it.date }
            val oldestDate = Instant.ofEpochMilli(dates.minOrNull() ?: 0L)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val newestDate = Instant.ofEpochMilli(dates.maxOrNull() ?: 0L)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val dateRange = formatDescriptionDateRange(oldestDate, newestDate)

            // Preserve expansion state from existing groups
            val existingGroup = state().descriptionGroups.find { it.description == description }
            val isExpanded = existingGroup?.isExpanded ?: false

            TransactionsHubStore.DescriptionGroup(
                description = description,
                transactions = sortedTxns,
                netAmount = netAmount,
                incomeAmount = incomeAmount,
                expenseAmount = expenseAmount,
                transactionCount = transactions.size,
                dateRange = dateRange,
                isExpanded = isExpanded
            )
        }.sortedByDescending { txns ->
            // Sort by most recent date
            txns.transactions.maxOfOrNull { it.date } ?: 0L
        }

        dispatch(TransactionsHubStore.Message.DescriptionGroupsCalculated(groups))
    }

    private fun formatDescriptionDateRange(start: LocalDate, end: LocalDate): String {
        val startMonth = start.month.name.take(3).lowercase()
            .replaceFirstChar { it.uppercase() }
        val endMonth = end.month.name.take(3).lowercase()
            .replaceFirstChar { it.uppercase() }

        return if (start == end) {
            "$startMonth ${start.dayOfMonth}"
        } else if (start.month == end.month && start.year == end.year) {
            "$startMonth ${start.dayOfMonth} - ${end.dayOfMonth}"
        } else {
            "$startMonth ${start.dayOfMonth} - $endMonth ${end.dayOfMonth}"
        }
    }
}

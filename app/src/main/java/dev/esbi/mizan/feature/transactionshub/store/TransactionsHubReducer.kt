package dev.esbi.mizan.feature.transactionshub.store

import com.arkivanov.mvikotlin.core.store.Reducer

/**
 * Reducer for TransactionsHub - updates state based on messages
 */
internal class TransactionsHubReducer : Reducer<TransactionsHubStore.State, TransactionsHubStore.Message> {

    override fun TransactionsHubStore.State.reduce(
        msg: TransactionsHubStore.Message
    ): TransactionsHubStore.State = when (msg) {
        is TransactionsHubStore.Message.TabSelected -> 
            copy(selectedTab = msg.tab)

        is TransactionsHubStore.Message.MonthChanged -> 
            copy(currentMonth = msg.month)

        is TransactionsHubStore.Message.TransactionsLoaded -> 
            copy(transactions = msg.transactions)

        is TransactionsHubStore.Message.CategoriesLoaded -> 
            copy(categories = msg.categories)

        is TransactionsHubStore.Message.AccountsLoaded -> 
            copy(accounts = msg.accounts)

        is TransactionsHubStore.Message.SummaryCalculated -> 
            copy(summary = msg.summary)

        is TransactionsHubStore.Message.DailyGroupsCalculated -> 
            copy(dailyGroups = msg.groups)

        is TransactionsHubStore.Message.LoadingChanged -> 
            copy(isLoading = msg.isLoading)

        is TransactionsHubStore.Message.ErrorOccurred -> 
            copy(error = msg.error, isLoading = false)

        is TransactionsHubStore.Message.DateSelected ->
            copy(selectedDate = msg.date)

        is TransactionsHubStore.Message.CalendarDataCalculated ->
            copy(
                calendarDays = msg.calendarDays,
                daysWithTransactions = msg.daysWithTransactions
            )

        is TransactionsHubStore.Message.WeeklySummariesCalculated ->
            copy(weeklySummaries = msg.summaries)

        is TransactionsHubStore.Message.WeekExpansionToggled ->
            copy(
                weeklySummaries = weeklySummaries.map { week ->
                    if (week.weekNumber == msg.weekNumber) {
                        week.copy(isExpanded = !week.isExpanded)
                    } else week
                }
            )

        is TransactionsHubStore.Message.CategorySummariesCalculated ->
            copy(
                expenseCategorySummaries = msg.expenseSummaries,
                incomeCategorySummaries = msg.incomeSummaries,
                savingsRate = msg.savingsRate
            )

        is TransactionsHubStore.Message.DescriptionGroupsCalculated ->
            copy(descriptionGroups = msg.groups)

        is TransactionsHubStore.Message.DescriptionSearchQueryChanged ->
            copy(descriptionSearchQuery = msg.query)

        is TransactionsHubStore.Message.DescriptionGroupToggled ->
            copy(
                descriptionGroups = descriptionGroups.map { group ->
                    if (group.description == msg.description) {
                        group.copy(isExpanded = !group.isExpanded)
                    } else group
                }
            )

        is TransactionsHubStore.Message.AllDescriptionsExpandedToggled ->
            copy(
                isAllDescriptionsExpanded = msg.isExpanded,
                descriptionGroups = descriptionGroups.map { it.copy(isExpanded = msg.isExpanded) }
            )
    }
}

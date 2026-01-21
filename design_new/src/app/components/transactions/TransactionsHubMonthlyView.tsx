/**
 * Transactions Hub - Monthly View
 * Displays transactions grouped by weeks with aggregated totals
 */

import { PremiumCard } from '../premium/PremiumCard';
import { Calendar, TrendingUp, TrendingDown, DollarSign } from 'lucide-react';
import type { Transaction } from '../../../types/domain';

export interface TransactionsHubMonthlyViewProps {
  transactions: Transaction[];
  currentDate: Date;
  onTransactionClick?: (transaction: Transaction) => void;
}

interface WeekGroup {
  weekNumber: number;
  weekRange: string;
  startDate: Date;
  endDate: Date;
  transactions: Transaction[];
  income: number;
  expense: number;
  total: number;
}

export function TransactionsHubMonthlyView({
  transactions,
  currentDate,
  onTransactionClick,
}: TransactionsHubMonthlyViewProps) {
  // Get week number for a date
  const getWeekNumber = (date: Date): number => {
    const firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
    const dayOfMonth = date.getDate();
    return Math.ceil((dayOfMonth + firstDayOfMonth.getDay()) / 7);
  };

  // Filter transactions for current month
  const filteredTransactions = transactions.filter(txn => {
    const txnDate = new Date(txn.timestamp);
    return (
      txnDate.getMonth() === currentDate.getMonth() &&
      txnDate.getFullYear() === currentDate.getFullYear()
    );
  });

  // Group transactions by week
  const weeklyGroups = filteredTransactions.reduce((groups, txn) => {
    const txnDate = new Date(txn.timestamp);
    const weekNum = getWeekNumber(txnDate);
    
    if (!groups[weekNum]) {
      // Calculate week start and end dates
      const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
      const weekStartDay = 1 + (weekNum - 1) * 7 - firstDayOfMonth.getDay();
      const weekStart = new Date(currentDate.getFullYear(), currentDate.getMonth(), Math.max(1, weekStartDay));
      const weekEnd = new Date(weekStart);
      weekEnd.setDate(weekStart.getDate() + 6);
      
      // Cap end date to last day of month
      const lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();
      if (weekEnd.getDate() > lastDayOfMonth) {
        weekEnd.setDate(lastDayOfMonth);
      }
      
      groups[weekNum] = {
        weekNumber: weekNum,
        weekRange: `${weekStart.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })} - ${weekEnd.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}`,
        startDate: weekStart,
        endDate: weekEnd,
        transactions: [],
        income: 0,
        expense: 0,
        total: 0,
      };
    }
    
    groups[weekNum].transactions.push(txn);
    
    if (txn.type === 'income') {
      groups[weekNum].income += txn.amount;
      groups[weekNum].total += txn.amount;
    } else if (txn.type === 'expense') {
      groups[weekNum].expense += txn.amount;
      groups[weekNum].total -= txn.amount;
    }
    
    return groups;
  }, {} as Record<number, WeekGroup>);

  // Sort by week number
  const sortedWeeks = Object.values(weeklyGroups).sort((a, b) => a.weekNumber - b.weekNumber);

  // Calculate month totals
  const monthTotals = {
    income: filteredTransactions
      .filter(t => t.type === 'income')
      .reduce((sum, t) => sum + t.amount, 0),
    expense: filteredTransactions
      .filter(t => t.type === 'expense')
      .reduce((sum, t) => sum + t.amount, 0),
  };
  monthTotals.total = monthTotals.income - monthTotals.expense;

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Month Overview */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
          <Calendar size={20} className="text-[var(--premium-emerald)]" />
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            {currentDate.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
          </h3>
        </div>

        <div className="grid grid-cols-3 gap-[var(--premium-space-md)]">
          {/* Total Income */}
          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <TrendingUp size={14} className="text-[var(--premium-success)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Income</p>
            </div>
            <p className="heading-md text-[var(--premium-success)]">
              ${monthTotals.income.toFixed(2)}
            </p>
          </div>

          {/* Total Expense */}
          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <TrendingDown size={14} className="text-[var(--premium-error)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Expense</p>
            </div>
            <p className="heading-md text-[var(--premium-error)]">
              ${monthTotals.expense.toFixed(2)}
            </p>
          </div>

          {/* Net Total */}
          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <DollarSign size={14} className="text-[var(--premium-primary)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Net</p>
            </div>
            <p className={`heading-md ${monthTotals.total >= 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
              ${Math.abs(monthTotals.total).toFixed(2)}
            </p>
          </div>
        </div>
      </PremiumCard>

      {/* Weekly Groups */}
      <div className="space-y-[var(--premium-space-lg)]">
        {sortedWeeks.map(week => (
          <div key={week.weekNumber} className="space-y-[var(--premium-space-md)]">
            {/* Week Header */}
            <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
              <div className="flex items-center justify-between mb-[var(--premium-space-sm)]">
                <div>
                  <h4 className="body-md text-[var(--premium-text-primary)] font-medium">
                    Week {week.weekNumber}
                  </h4>
                  <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                    {week.weekRange}
                  </p>
                </div>

                {/* Week Totals */}
                <div className="flex items-center gap-[var(--premium-space-sm)]">
                  {week.income > 0 && (
                    <div className="text-right">
                      <p className="body-xs text-[var(--premium-text-tertiary)]">Income</p>
                      <p className="body-sm text-[var(--premium-success)] font-medium">
                        +${week.income.toFixed(2)}
                      </p>
                    </div>
                  )}
                  {week.expense > 0 && (
                    <div className="text-right">
                      <p className="body-xs text-[var(--premium-text-tertiary)]">Expense</p>
                      <p className="body-sm text-[var(--premium-error)] font-medium">
                        -${week.expense.toFixed(2)}
                      </p>
                    </div>
                  )}
                  <div className="text-right">
                    <p className="body-xs text-[var(--premium-text-tertiary)]">Total</p>
                    <p className={`body-sm font-bold ${week.total >= 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
                      ${Math.abs(week.total).toFixed(2)}
                    </p>
                  </div>
                </div>
              </div>

              {/* Progress Bar */}
              <div className="w-full h-[6px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
                <div 
                  className="h-full bg-gradient-to-r from-[var(--premium-success)] to-[var(--premium-emerald)] transition-all duration-300"
                  style={{ width: `${Math.min((week.income / (week.income + week.expense)) * 100, 100)}%` }}
                />
              </div>
            </PremiumCard>

            {/* Week Transactions */}
            <div className="space-y-[8px] pl-[var(--premium-space-md)]">
              {week.transactions
                .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime())
                .map(transaction => (
                  <PremiumCard
                    key={transaction.id}
                    variant="glass"
                    hover
                    onClick={() => onTransactionClick?.(transaction)}
                    className="p-[var(--premium-space-md)]"
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex-1">
                        <p className="body-md text-[var(--premium-text-primary)] font-medium">
                          {transaction.title}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                          {new Date(transaction.timestamp).toLocaleDateString('en-US', { 
                            month: 'short', 
                            day: 'numeric' 
                          })} • {transaction.category}
                        </p>
                      </div>

                      <div className="text-right">
                        <p className={`
                          heading-sm
                          ${transaction.type === 'income' 
                            ? 'text-[var(--premium-success)]' 
                            : 'text-[var(--premium-error)]'
                          }
                        `}>
                          {transaction.type === 'income' ? '+' : '-'}${transaction.amount.toFixed(2)}
                        </p>
                      </div>
                    </div>
                  </PremiumCard>
                ))}
            </div>
          </div>
        ))}
      </div>

      {/* Empty State */}
      {sortedWeeks.length === 0 && (
        <div className="flex flex-col items-center justify-center py-[var(--premium-space-4xl)]">
          <div className="
            w-[80px] h-[80px]
            bg-[var(--premium-surface-2)]
            rounded-[var(--premium-radius-xl)]
            flex items-center justify-center
            mb-[var(--premium-space-lg)]
          ">
            <Calendar size={40} className="text-[var(--premium-text-muted)]" />
          </div>
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-sm)]">
            No transactions this month
          </h3>
          <p className="body-md text-[var(--premium-text-tertiary)] text-center">
            Add your first transaction to get started
          </p>
        </div>
      )}
    </div>
  );
}

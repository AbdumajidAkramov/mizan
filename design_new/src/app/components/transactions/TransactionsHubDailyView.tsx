/**
 * Transactions Hub - Daily View
 * Displays transactions grouped by date with daily totals (Income, Expense, Total)
 */

import { PremiumCard } from '../premium/PremiumCard';
import { TrendingUp, TrendingDown, DollarSign } from 'lucide-react';
import type { Transaction } from '../../../types/domain';

export interface TransactionsHubDailyViewProps {
  transactions: Transaction[];
  currentDate: Date;
  onTransactionClick?: (transaction: Transaction) => void;
}

interface DailyGroup {
  date: string;
  dateDisplay: string;
  weekday: string;
  transactions: Transaction[];
  income: number;
  expense: number;
  total: number;
}

export function TransactionsHubDailyView({
  transactions,
  currentDate,
  onTransactionClick,
}: TransactionsHubDailyViewProps) {
  // Filter transactions for current month
  const filteredTransactions = transactions.filter(txn => {
    const txnDate = new Date(txn.timestamp);
    return (
      txnDate.getMonth() === currentDate.getMonth() &&
      txnDate.getFullYear() === currentDate.getFullYear()
    );
  });

  // Group transactions by date
  const dailyGroups = filteredTransactions.reduce((groups, txn) => {
    const txnDate = new Date(txn.timestamp);
    const dateKey = txnDate.toISOString().split('T')[0];
    
    if (!groups[dateKey]) {
      groups[dateKey] = {
        date: dateKey,
        dateDisplay: txnDate.toLocaleDateString('en-US', { 
          month: 'short', 
          day: 'numeric' 
        }),
        weekday: txnDate.toLocaleDateString('en-US', { weekday: 'long' }),
        transactions: [],
        income: 0,
        expense: 0,
        total: 0,
      };
    }
    
    groups[dateKey].transactions.push(txn);
    
    if (txn.type === 'income') {
      groups[dateKey].income += txn.amount;
      groups[dateKey].total += txn.amount;
    } else if (txn.type === 'expense') {
      groups[dateKey].expense += txn.amount;
      groups[dateKey].total -= txn.amount;
    }
    
    return groups;
  }, {} as Record<string, DailyGroup>);

  // Sort by date descending
  const sortedGroups = Object.values(dailyGroups).sort((a, b) => 
    new Date(b.date).getTime() - new Date(a.date).getTime()
  );

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
      {/* Month Summary Header */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="grid grid-cols-3 gap-[var(--premium-space-md)]">
          {/* Income */}
          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <div className="w-[6px] h-[6px] rounded-full bg-[var(--premium-success)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Income</p>
            </div>
            <p className="heading-md text-[var(--premium-success)]">
              ${monthTotals.income.toFixed(2)}
            </p>
          </div>

          {/* Expense */}
          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <div className="w-[6px] h-[6px] rounded-full bg-[var(--premium-error)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Expense</p>
            </div>
            <p className="heading-md text-[var(--premium-error)]">
              ${monthTotals.expense.toFixed(2)}
            </p>
          </div>

          {/* Total */}
          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <div className="w-[6px] h-[6px] rounded-full bg-[var(--premium-primary)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Total</p>
            </div>
            <p className={`heading-md ${monthTotals.total >= 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
              ${Math.abs(monthTotals.total).toFixed(2)}
            </p>
          </div>
        </div>
      </PremiumCard>

      {/* Daily Groups */}
      <div className="space-y-[var(--premium-space-lg)]">
        {sortedGroups.map(group => (
          <div key={group.date} className="space-y-[var(--premium-space-md)]">
            {/* Date Header */}
            <div className="flex items-center justify-between">
              <div>
                <h3 className="heading-sm text-[var(--premium-text-primary)]">
                  {group.dateDisplay}
                </h3>
                <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                  {group.weekday}
                </p>
              </div>

              {/* Daily Summary */}
              <div className="flex items-center gap-[var(--premium-space-md)]">
                {group.income > 0 && (
                  <div className="flex items-center gap-[4px]">
                    <TrendingUp size={14} className="text-[var(--premium-success)]" />
                    <p className="body-sm text-[var(--premium-success)] font-medium">
                      ${group.income.toFixed(2)}
                    </p>
                  </div>
                )}
                {group.expense > 0 && (
                  <div className="flex items-center gap-[4px]">
                    <TrendingDown size={14} className="text-[var(--premium-error)]" />
                    <p className="body-sm text-[var(--premium-error)] font-medium">
                      ${group.expense.toFixed(2)}
                    </p>
                  </div>
                )}
                <div className="flex items-center gap-[4px]">
                  <DollarSign size={14} className={group.total >= 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'} />
                  <p className={`body-sm font-bold ${group.total >= 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
                    ${Math.abs(group.total).toFixed(2)}
                  </p>
                </div>
              </div>
            </div>

            {/* Transactions List */}
            <div className="space-y-[8px]">
              {group.transactions.map(transaction => (
                <PremiumCard
                  key={transaction.id}
                  variant="glass"
                  hover
                  onClick={() => onTransactionClick?.(transaction)}
                  className="p-[var(--premium-space-md)]"
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-[var(--premium-space-md)]">
                      {/* Category Icon */}
                      <div className={`
                        w-[40px] h-[40px]
                        rounded-[var(--premium-radius-sm)]
                        flex items-center justify-center
                        ${transaction.type === 'income' 
                          ? 'bg-[var(--premium-success)]/10' 
                          : 'bg-[var(--premium-error)]/10'
                        }
                      `}>
                        {transaction.type === 'income' ? (
                          <TrendingUp size={20} className="text-[var(--premium-success)]" />
                        ) : (
                          <TrendingDown size={20} className="text-[var(--premium-error)]" />
                        )}
                      </div>

                      {/* Transaction Info */}
                      <div>
                        <p className="body-md text-[var(--premium-text-primary)] font-medium">
                          {transaction.title}
                        </p>
                        <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                          {transaction.category} • {new Date(transaction.timestamp).toLocaleTimeString('en-US', { 
                            hour: '2-digit', 
                            minute: '2-digit' 
                          })}
                        </p>
                      </div>
                    </div>

                    {/* Amount */}
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
      {sortedGroups.length === 0 && (
        <div className="flex flex-col items-center justify-center py-[var(--premium-space-4xl)]">
          <div className="
            w-[80px] h-[80px]
            bg-[var(--premium-surface-2)]
            rounded-[var(--premium-radius-xl)]
            flex items-center justify-center
            mb-[var(--premium-space-lg)]
          ">
            <DollarSign size={40} className="text-[var(--premium-text-muted)]" />
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

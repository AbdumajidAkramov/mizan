/**
 * Transactions Hub - Summary View
 * Shows aggregated data: Accounts breakdown, Budget progress, Export functionality
 */

import { PremiumCard } from '../premium/PremiumCard';
import { 
  Wallet, 
  CreditCard, 
  PiggyBank, 
  TrendingUp, 
  TrendingDown,
  Download,
  Target,
  BarChart3,
  DollarSign
} from 'lucide-react';
import type { Transaction } from '../../../types/domain';

export interface TransactionsHubSummaryViewProps {
  transactions: Transaction[];
  currentDate: Date;
}

interface AccountSummary {
  id: string;
  name: string;
  type: 'cash' | 'card' | 'savings';
  balance: number;
  percentage: number;
}

export function TransactionsHubSummaryView({
  transactions,
  currentDate,
}: TransactionsHubSummaryViewProps) {
  // Filter transactions for current month
  const filteredTransactions = transactions.filter(txn => {
    const txnDate = new Date(txn.timestamp);
    return (
      txnDate.getMonth() === currentDate.getMonth() &&
      txnDate.getFullYear() === currentDate.getFullYear()
    );
  });

  // Calculate totals
  const totalIncome = filteredTransactions
    .filter(t => t.type === 'income')
    .reduce((sum, t) => sum + t.amount, 0);
  
  const totalExpense = filteredTransactions
    .filter(t => t.type === 'expense')
    .reduce((sum, t) => sum + t.amount, 0);

  const netTotal = totalIncome - totalExpense;

  // Mock account data (in a real app, this would come from props or API)
  const accounts: AccountSummary[] = [
    { id: '1', name: 'Cash Account', type: 'cash', balance: 2500.00, percentage: 35 },
    { id: '2', name: 'Credit Card', type: 'card', balance: 3200.50, percentage: 45 },
    { id: '3', name: 'Savings', type: 'savings', balance: 1450.00, percentage: 20 },
  ];

  const totalBalance = accounts.reduce((sum, acc) => sum + acc.balance, 0);

  // Budget data (mock)
  const budgetLimit = 5000.00;
  const budgetUsed = totalExpense;
  const budgetRemaining = budgetLimit - budgetUsed;
  const budgetPercentage = (budgetUsed / budgetLimit) * 100;

  // Category breakdown
  const categoryBreakdown = filteredTransactions
    .filter(t => t.type === 'expense')
    .reduce((acc, txn) => {
      if (!acc[txn.category]) {
        acc[txn.category] = 0;
      }
      acc[txn.category] += txn.amount;
      return acc;
    }, {} as Record<string, number>);

  const topCategories = Object.entries(categoryBreakdown)
    .sort(([, a], [, b]) => b - a)
    .slice(0, 5);

  const getAccountIcon = (type: string) => {
    switch (type) {
      case 'cash': return Wallet;
      case 'card': return CreditCard;
      case 'savings': return PiggyBank;
      default: return Wallet;
    }
  };

  const handleExport = () => {
    // In a real app, this would generate and download an Excel file
    console.log('Exporting data to Excel...');
    alert('Export functionality would generate an Excel file with all transaction data for this month.');
  };

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Month Summary Header */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-md)]">
          {currentDate.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })} Summary
        </h3>

        <div className="grid grid-cols-3 gap-[var(--premium-space-md)]">
          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <TrendingUp size={14} className="text-[var(--premium-success)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Income</p>
            </div>
            <p className="heading-md text-[var(--premium-success)]">
              ${totalIncome.toFixed(2)}
            </p>
          </div>

          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <TrendingDown size={14} className="text-[var(--premium-error)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Expense</p>
            </div>
            <p className="heading-md text-[var(--premium-error)]">
              ${totalExpense.toFixed(2)}
            </p>
          </div>

          <div>
            <div className="flex items-center gap-[6px] mb-[4px]">
              <DollarSign size={14} className="text-[var(--premium-primary)]" />
              <p className="body-xs text-[var(--premium-text-tertiary)]">Net</p>
            </div>
            <p className={`heading-md ${netTotal >= 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
              ${Math.abs(netTotal).toFixed(2)}
            </p>
          </div>
        </div>
      </PremiumCard>

      {/* Accounts Breakdown */}
      <div className="space-y-[var(--premium-space-md)]">
        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <Wallet size={20} className="text-[var(--premium-emerald)]" />
          <h3 className="heading-sm text-[var(--premium-text-primary)]">Accounts</h3>
        </div>

        <div className="space-y-[8px]">
          {accounts.map(account => {
            const Icon = getAccountIcon(account.type);
            return (
              <PremiumCard
                key={account.id}
                variant="glass"
                className="p-[var(--premium-space-md)]"
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-[var(--premium-space-md)]">
                    <div className="
                      w-[40px] h-[40px]
                      rounded-[var(--premium-radius-sm)]
                      bg-gradient-to-br from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)]
                      flex items-center justify-center
                    ">
                      <Icon size={20} className="text-white" />
                    </div>

                    <div className="flex-1">
                      <p className="body-md text-[var(--premium-text-primary)] font-medium">
                        {account.name}
                      </p>
                      <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                        {account.percentage}% of total
                      </p>
                    </div>
                  </div>

                  <p className="heading-sm text-[var(--premium-text-primary)]">
                    ${account.balance.toFixed(2)}
                  </p>
                </div>

                {/* Progress Bar */}
                <div className="mt-[var(--premium-space-sm)] w-full h-[4px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
                  <div 
                    className="h-full bg-gradient-to-r from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)] transition-all duration-300"
                    style={{ width: `${account.percentage}%` }}
                  />
                </div>
              </PremiumCard>
            );
          })}
        </div>

        {/* Total Balance Card */}
        <PremiumCard 
          variant="gradient" 
          className="p-[var(--premium-space-md)] bg-gradient-to-br from-[var(--premium-primary)] to-[var(--premium-primary-dark)]"
        >
          <div className="flex items-center justify-between">
            <p className="body-md text-white/80">Total Balance</p>
            <p className="heading-lg text-white">
              ${totalBalance.toFixed(2)}
            </p>
          </div>
        </PremiumCard>
      </div>

      {/* Budget Progress */}
      <div className="space-y-[var(--premium-space-md)]">
        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <Target size={20} className="text-[var(--premium-emerald)]" />
          <h3 className="heading-sm text-[var(--premium-text-primary)]">Budget</h3>
        </div>

        <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
          <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
            <div>
              <p className="body-sm text-[var(--premium-text-tertiary)]">Monthly Budget</p>
              <p className="heading-lg text-[var(--premium-text-primary)] mt-[4px]">
                ${budgetLimit.toFixed(2)}
              </p>
            </div>
            <div className="text-right">
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                {budgetPercentage.toFixed(0)}%
              </p>
              <p className={`
                heading-md mt-[4px]
                ${budgetPercentage > 100 
                  ? 'text-[var(--premium-error)]' 
                  : budgetPercentage > 80 
                    ? 'text-[var(--premium-warning)]' 
                    : 'text-[var(--premium-success)]'
                }
              `}>
                ${budgetRemaining.toFixed(2)}
              </p>
            </div>
          </div>

          {/* Budget Bar */}
          <div className="relative w-full h-[12px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
            <div 
              className={`
                h-full transition-all duration-300
                ${budgetPercentage > 100 
                  ? 'bg-gradient-to-r from-[var(--premium-error)] to-[var(--premium-error-dark)]' 
                  : budgetPercentage > 80 
                    ? 'bg-gradient-to-r from-[var(--premium-warning)] to-[var(--premium-warning-dark)]' 
                    : 'bg-gradient-to-r from-[var(--premium-success)] to-[var(--premium-emerald)]'
                }
              `}
              style={{ width: `${Math.min(budgetPercentage, 100)}%` }}
            />
          </div>

          <div className="flex items-center justify-between mt-[var(--premium-space-sm)]">
            <p className="body-xs text-[var(--premium-text-tertiary)]">
              Used: ${budgetUsed.toFixed(2)}
            </p>
            <p className="body-xs text-[var(--premium-text-tertiary)]">
              Limit: ${budgetLimit.toFixed(2)}
            </p>
          </div>
        </PremiumCard>
      </div>

      {/* Top Categories */}
      <div className="space-y-[var(--premium-space-md)]">
        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <BarChart3 size={20} className="text-[var(--premium-emerald)]" />
          <h3 className="heading-sm text-[var(--premium-text-primary)]">Top Categories</h3>
        </div>

        <div className="space-y-[8px]">
          {topCategories.map(([category, amount], index) => {
            const percentage = (amount / totalExpense) * 100;
            return (
              <PremiumCard
                key={category}
                variant="glass"
                className="p-[var(--premium-space-md)]"
              >
                <div className="flex items-center justify-between mb-[var(--premium-space-sm)]">
                  <div className="flex items-center gap-[var(--premium-space-sm)]">
                    <div className="
                      w-[24px] h-[24px]
                      rounded-full
                      bg-[var(--premium-surface-3)]
                      flex items-center justify-center
                    ">
                      <p className="body-xs text-[var(--premium-text-primary)] font-bold">
                        {index + 1}
                      </p>
                    </div>
                    <p className="body-md text-[var(--premium-text-primary)] font-medium capitalize">
                      {category}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="body-md text-[var(--premium-text-primary)] font-medium">
                      ${amount.toFixed(2)}
                    </p>
                    <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                      {percentage.toFixed(1)}%
                    </p>
                  </div>
                </div>

                <div className="w-full h-[4px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
                  <div 
                    className="h-full bg-gradient-to-r from-[var(--premium-primary)] to-[var(--premium-primary-dark)] transition-all duration-300"
                    style={{ width: `${percentage}%` }}
                  />
                </div>
              </PremiumCard>
            );
          })}
        </div>
      </div>

      {/* Export Data */}
      <PremiumCard 
        variant="glass" 
        hover
        onClick={handleExport}
        className="p-[var(--premium-space-lg)]"
      >
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-[var(--premium-space-md)]">
            <div className="
              w-[48px] h-[48px]
              rounded-[var(--premium-radius-md)]
              bg-gradient-to-br from-[var(--premium-emerald)] to-[var(--premium-emerald-dark)]
              flex items-center justify-center
            ">
              <Download size={24} className="text-white" />
            </div>
            <div>
              <p className="body-md text-[var(--premium-text-primary)] font-medium">
                Export data to Excel
              </p>
              <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">
                Download complete transaction report
              </p>
            </div>
          </div>
        </div>
      </PremiumCard>
    </div>
  );
}

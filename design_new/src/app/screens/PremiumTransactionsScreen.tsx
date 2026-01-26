/**
 * Premium Transactions History Screen
 * Complete transaction list with filters and search
 */

import { useState } from 'react';
import { PremiumCard } from '../components/premium/PremiumCard';
import { PremiumButton } from '../components/premium/PremiumButton';
import { PremiumTransactionItem } from '../components/premium/PremiumTransactionItem';
import { Search, Filter, Calendar, TrendingUp, TrendingDown, Wallet } from 'lucide-react';
import type { Transaction, UiState } from '../../types/domain';
import { MOCK_TRANSACTIONS } from '../../mocks/data';

export interface PremiumTransactionsScreenProps {
  transactionsState: UiState<Transaction[]>;
  onFilterClick?: () => void;
  onTransactionClick?: (transaction: Transaction) => void;
}

export function PremiumTransactionsScreen({
  transactionsState,
  onFilterClick,
  onTransactionClick,
}: PremiumTransactionsScreenProps) {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedFilter, setSelectedFilter] = useState<'all' | 'expense' | 'income'>('all');

  const transactions = transactionsState.data || MOCK_TRANSACTIONS;

  // Filter transactions
  const filteredTransactions = transactions.filter(txn => {
    const matchesSearch = txn.title.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesType = selectedFilter === 'all' || txn.type === selectedFilter;
    return matchesSearch && matchesType;
  });

  // Group by date
  const groupedTransactions = filteredTransactions.reduce((groups, txn) => {
    const date = new Date(txn.timestamp).toLocaleDateString('en-US', {
      weekday: 'long',
      month: 'long',
      day: 'numeric',
    });
    if (!groups[date]) groups[date] = [];
    groups[date].push(txn);
    return groups;
  }, {} as Record<string, Transaction[]>);

  // Calculate totals
  const totalIncome = filteredTransactions
    .filter(t => t.type === 'income')
    .reduce((sum, t) => sum + t.amount, 0);
  
  const totalExpense = filteredTransactions
    .filter(t => t.type === 'expense')
    .reduce((sum, t) => sum + t.amount, 0);

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="heading-xl text-[var(--premium-text-primary)]">
          Transactions
        </h1>
        <button
          onClick={onFilterClick}
          className="
            w-[40px] h-[40px]
            bg-[var(--premium-surface-2)]
            rounded-full
            flex items-center justify-center
            hover:bg-[var(--premium-surface-3)]
            transition-all
          "
        >
          <Filter size={20} className="text-[var(--premium-text-secondary)]" />
        </button>
      </div>

      {/* Search Bar */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <Search size={20} className="text-[var(--premium-text-tertiary)]" />
          <input
            type="text"
            placeholder="Search transactions..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="
              flex-1 bg-transparent
              body-md text-[var(--premium-text-primary)]
              outline-none
              placeholder:text-[var(--premium-text-muted)]
            "
          />
        </div>
      </PremiumCard>

      {/* Filter Chips */}
      <div className="flex gap-[var(--premium-space-sm)]">
        <button
          onClick={() => setSelectedFilter('all')}
          className={`
            px-[var(--premium-space-lg)] py-[var(--premium-space-sm)]
            rounded-[var(--premium-radius-full)]
            body-md font-medium
            transition-all duration-200
            ${selectedFilter === 'all'
              ? 'bg-gradient-to-r from-[#667eea] to-[#764ba2] text-white'
              : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
            }
          `}
        >
          All
        </button>
        <button
          onClick={() => setSelectedFilter('expense')}
          className={`
            px-[var(--premium-space-lg)] py-[var(--premium-space-sm)]
            rounded-[var(--premium-radius-full)]
            body-md font-medium
            transition-all duration-200
            flex items-center gap-[var(--premium-space-xs)]
            ${selectedFilter === 'expense'
              ? 'bg-gradient-to-r from-[#f093fb] to-[#f5576c] text-white'
              : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
            }
          `}
        >
          <TrendingDown size={16} />
          Expenses
        </button>
        <button
          onClick={() => setSelectedFilter('income')}
          className={`
            px-[var(--premium-space-lg)] py-[var(--premium-space-sm)]
            rounded-[var(--premium-radius-full)]
            body-md font-medium
            transition-all duration-200
            flex items-center gap-[var(--premium-space-xs)]
            ${selectedFilter === 'income'
              ? 'bg-gradient-to-r from-[#4facfe] to-[#00f2fe] text-white'
              : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
            }
          `}
        >
          <TrendingUp size={16} />
          Income
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-2 gap-[var(--premium-space-md)]">
        <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-xs)]">
            <div className="w-[8px] h-[8px] rounded-full bg-[var(--premium-success)]" />
            <p className="body-sm text-[var(--premium-text-tertiary)]">Total Income</p>
          </div>
          <p className="heading-lg text-[var(--premium-success)]">
            +${totalIncome.toFixed(2)}
          </p>
        </PremiumCard>

        <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-xs)]">
            <div className="w-[8px] h-[8px] rounded-full bg-[var(--premium-error)]" />
            <p className="body-sm text-[var(--premium-text-tertiary)]">Total Expense</p>
          </div>
          <p className="heading-lg text-[var(--premium-text-primary)]">
            -${totalExpense.toFixed(2)}
          </p>
        </PremiumCard>
      </div>

      {/* Transaction List */}
      <div className="space-y-[var(--premium-space-lg)]">
        {Object.entries(groupedTransactions).map(([date, txns]) => (
          <div key={date}>
            <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
              <Calendar size={16} className="text-[var(--premium-text-muted)]" />
              <p className="body-sm text-[var(--premium-text-tertiary)] font-medium">
                {date}
              </p>
              <div className="flex-1 h-[1px] bg-[var(--premium-surface-2)]" />
            </div>

            <div className="space-y-[var(--premium-space-sm)]">
              {txns.map(transaction => (
                <PremiumTransactionItem
                  key={transaction.id}
                  transaction={transaction}
                  onClick={onTransactionClick}
                />
              ))}
            </div>
          </div>
        ))}
      </div>

      {/* Empty State */}
      {filteredTransactions.length === 0 && (
        <div className="flex flex-col items-center justify-center py-[var(--premium-space-4xl)]">
          <div className="
            w-[80px] h-[80px]
            bg-[var(--premium-surface-2)]
            rounded-[var(--premium-radius-xl)]
            flex items-center justify-center
            mb-[var(--premium-space-lg)]
          ">
            <Wallet size={40} className="text-[var(--premium-text-muted)]" />
          </div>
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-sm)]">
            No transactions found
          </h3>
          <p className="body-md text-[var(--premium-text-tertiary)] text-center">
            Try adjusting your search or filter
          </p>
        </div>
      )}
    </div>
  );
}

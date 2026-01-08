/**
 * TransactionList Organism Component
 * Displays list of transactions with filtering
 */

import { useState } from 'react';
import { Receipt } from 'lucide-react';
import { TransactionListItem } from '../molecules/TransactionListItem';
import { EmptyState } from '../molecules/EmptyState';
import { ErrorState } from '../molecules/ErrorState';
import { LoadingSkeleton } from '../atoms/LoadingSkeleton';
import { Button } from '../atoms/Button';
import type { Transaction, UiState, TransactionType } from '../../../types/domain';

export interface TransactionListProps {
  /** UI state wrapper with transaction data */
  transactionsState: UiState<Transaction[]>;
  /** Optional filter by type */
  initialFilter?: TransactionType | 'all';
  /** Callback when transaction is clicked */
  onTransactionClick?: (transaction: Transaction) => void;
}

/**
 * Material 3 Transaction List Organism
 * Full transaction listing with filters and states
 */
export function TransactionList({
  transactionsState,
  initialFilter = 'all',
  onTransactionClick,
}: TransactionListProps) {
  const [filterType, setFilterType] = useState<TransactionType | 'all'>(initialFilter);

  // Handle loading state
  if (transactionsState.status === 'loading') {
    return (
      <div className="flex flex-col gap-[var(--spacing-md)]">
        {[...Array(5)].map((_, i) => (
          <div key={i} className="flex items-center gap-[var(--spacing-md)] p-[var(--spacing-md)]">
            <LoadingSkeleton width={48} height={48} radius="full" />
            <div className="flex-1 space-y-[var(--spacing-sm)]">
              <LoadingSkeleton width="60%" height={16} />
              <LoadingSkeleton width="40%" height={12} />
            </div>
            <LoadingSkeleton width={80} height={20} />
          </div>
        ))}
      </div>
    );
  }

  // Handle error state
  if (transactionsState.status === 'error') {
    return (
      <ErrorState
        message={transactionsState.error || 'Failed to load transactions'}
      />
    );
  }

  // Handle empty state
  if (transactionsState.status === 'empty' || !transactionsState.data?.length) {
    return (
      <EmptyState
        icon={<Receipt size={40} />}
        title="No Transactions Yet"
        description="Start tracking your expenses by adding your first transaction"
      />
    );
  }

  // Filter transactions
  const transactions = transactionsState.data;
  const filteredTransactions = filterType === 'all' 
    ? transactions 
    : transactions.filter(t => t.type === filterType);

  return (
    <div className="flex flex-col gap-[var(--spacing-md)]">
      {/* Filter Buttons */}
      <div className="flex gap-[var(--spacing-sm)] overflow-x-auto pb-[var(--spacing-sm)]">
        <Button
          variant={filterType === 'all' ? 'filled' : 'outlined'}
          size="small"
          onClick={() => setFilterType('all')}
        >
          All
        </Button>
        <Button
          variant={filterType === 'expense' ? 'filled' : 'outlined'}
          size="small"
          onClick={() => setFilterType('expense')}
        >
          Expenses
        </Button>
        <Button
          variant={filterType === 'income' ? 'filled' : 'outlined'}
          size="small"
          onClick={() => setFilterType('income')}
        >
          Income
        </Button>
      </div>

      {/* Transaction List */}
      <div className="space-y-[var(--spacing-sm)]">
        {filteredTransactions.map((transaction) => (
          <TransactionListItem
            key={transaction.id}
            transaction={transaction}
            onClick={onTransactionClick}
          />
        ))}
      </div>
    </div>
  );
}

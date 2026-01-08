/**
 * M3 TransactionsScreen
 * Material Design 3 screen displaying transaction history
 * Maps to Jetpack Compose LazyColumn with FilterChips
 */

import { useState } from 'react';
import { Receipt } from 'lucide-react';
import { M3TransactionListItem } from '../components/molecules/M3TransactionListItem';
import { M3Button } from '../components/atoms/M3Button';
import { EmptyState } from '../components/molecules/EmptyState';
import { ErrorState } from '../components/molecules/ErrorState';
import { LoadingSkeleton } from '../components/atoms/LoadingSkeleton';
import type { Transaction, UiState, TransactionType } from '../../types/domain';

export interface M3TransactionsScreenProps {
  /** Transactions data state (MVI pattern) */
  transactionsState: UiState<Transaction[]>;
  /** Callback when transaction is clicked */
  onTransactionClick?: (transaction: Transaction) => void;
}

/**
 * Material 3 Transactions Screen
 * Full screen transaction history with M3 filter chips
 * 
 * @example
 * <M3TransactionsScreen
 *   transactionsState={{ status: 'success', data: transactions }}
 *   onTransactionClick={handleClick}
 * />
 */
export function M3TransactionsScreen({
  transactionsState,
  onTransactionClick,
}: M3TransactionsScreenProps) {
  const [filterType, setFilterType] = useState<TransactionType | 'all'>('all');

  // MVI State: Loading
  if (transactionsState.status === 'loading') {
    return (
      <div className="flex flex-col gap-[var(--md-sys-spacing-md)]">
        <h1 className="headline-medium text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-lg)]">
          Transactions
        </h1>
        {[...Array(5)].map((_, i) => (
          <div key={i} className="flex items-center gap-[var(--md-sys-spacing-md)] p-[var(--md-sys-spacing-md)]">
            <LoadingSkeleton width={48} height={48} radius="full" />
            <div className="flex-1 space-y-[var(--md-sys-spacing-sm)]">
              <LoadingSkeleton width="60%" height={16} />
              <LoadingSkeleton width="40%" height={12} />
            </div>
            <LoadingSkeleton width={80} height={20} />
          </div>
        ))}
      </div>
    );
  }

  // MVI State: Error
  if (transactionsState.status === 'error') {
    return (
      <div>
        <h1 className="headline-medium text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-lg)]">
          Transactions
        </h1>
        <ErrorState
          message={transactionsState.error || 'Failed to load transactions'}
        />
      </div>
    );
  }

  // MVI State: Empty
  if (transactionsState.status === 'empty' || !transactionsState.data?.length) {
    return (
      <div>
        <h1 className="headline-medium text-[var(--md-sys-color-on-surface)] mb-[var(--md-sys-spacing-lg)]">
          Transactions
        </h1>
        <EmptyState
          icon={<Receipt size={40} />}
          title="No Transactions Yet"
          description="Start tracking your expenses by adding your first transaction"
        />
      </div>
    );
  }

  // MVI State: Success
  const transactions = transactionsState.data;
  const filteredTransactions = filterType === 'all' 
    ? transactions 
    : transactions.filter(t => t.type === filterType);

  return (
    <div className="flex flex-col gap-[var(--md-sys-spacing-md)]">
      {/* Header */}
      <h1 className="headline-medium text-[var(--md-sys-color-on-surface)]">
        Transactions
      </h1>

      {/* M3 Filter Chips */}
      <div className="flex gap-[var(--md-sys-spacing-sm)] overflow-x-auto pb-[var(--md-sys-spacing-sm)]">
        <M3Button
          variant={filterType === 'all' ? 'filled' : 'outlined'}
          size="default"
          onClick={() => setFilterType('all')}
        >
          All
        </M3Button>
        <M3Button
          variant={filterType === 'expense' ? 'filled' : 'outlined'}
          size="default"
          onClick={() => setFilterType('expense')}
        >
          Expenses
        </M3Button>
        <M3Button
          variant={filterType === 'income' ? 'filled' : 'outlined'}
          size="default"
          onClick={() => setFilterType('income')}
        >
          Income
        </M3Button>
      </div>

      {/* Transaction List */}
      <div className="space-y-[var(--md-sys-spacing-sm)]">
        {filteredTransactions.map((transaction) => (
          <M3TransactionListItem
            key={transaction.id}
            transaction={transaction}
            onClick={onTransactionClick}
          />
        ))}
      </div>
    </div>
  );
}

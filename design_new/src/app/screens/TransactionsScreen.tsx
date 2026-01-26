/**
 * TransactionsScreen
 * Screen displaying all transactions with filters
 */

import { TransactionList } from '../components/organisms/TransactionList';
import type { UiState, Transaction } from '../../types/domain';

export interface TransactionsScreenProps {
  /** Transactions data state */
  transactionsState: UiState<Transaction[]>;
  /** Callback when transaction is clicked */
  onTransactionClick?: (transaction: Transaction) => void;
}

/**
 * Transactions Screen Component
 * Full screen transaction history
 */
export function TransactionsScreen({
  transactionsState,
  onTransactionClick,
}: TransactionsScreenProps) {
  return (
    <div className="pb-[var(--spacing-3xl)]">
      {/* Header */}
      <div className="flex items-center justify-between mb-[var(--spacing-lg)]">
        <h1 className="text-[var(--color-on-surface)]">Transactions</h1>
      </div>

      {/* Transaction List */}
      <TransactionList
        transactionsState={transactionsState}
        onTransactionClick={onTransactionClick}
      />
    </div>
  );
}

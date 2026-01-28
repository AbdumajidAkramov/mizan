/**
 * Premium Description View - Money Manager Style
 * Groups transactions by description/note with collapsible sections
 * 
 * @design Money Manager Description Grouping Pattern
 * @architecture Collapsible groups with search and sort
 * @interactions Expand/collapse, search, sort toggles
 */

import { useState, useMemo } from 'react';
import {
  Search,
  ChevronDown,
  ChevronRight,
  Calendar,
  TrendingUp,
  TrendingDown,
  X,
  SlidersHorizontal,
} from 'lucide-react';
import type { Transaction, TransactionCategory } from '../../../types/domain';
import { CATEGORY_ICONS, CATEGORY_LABELS } from '../../../constants/categories';

export interface PremiumDescriptionViewProps {
  /** Array of transactions to group */
  transactions: Transaction[];
  /** Callback when transaction is tapped */
  onTransactionTap?: (transaction: Transaction) => void;
}

type SortMode = 'frequency' | 'total-amount' | 'recent-date';

interface DescriptionGroup {
  description: string;
  transactions: Transaction[];
  totalAmount: number;
  netAmount: number;
  count: number;
  mostRecentDate: Date;
  oldestDate: Date;
  incomeAmount: number;
  expenseAmount: number;
}

/**
 * Premium Description View Component
 */
export function PremiumDescriptionView({
  transactions,
  onTransactionTap,
}: PremiumDescriptionViewProps) {
  const [searchQuery, setSearchQuery] = useState('');
  const [sortMode, setSortMode] = useState<SortMode>('recent-date');
  const [expandedGroups, setExpandedGroups] = useState<Set<string>>(new Set());
  const [showSortMenu, setShowSortMenu] = useState(false);

  // Group transactions by description
  const descriptionGroups = useMemo(() => {
    return groupTransactionsByDescription(transactions, searchQuery, sortMode);
  }, [transactions, searchQuery, sortMode]);

  // Toggle group expansion
  const toggleGroup = (description: string) => {
    setExpandedGroups((prev) => {
      const newSet = new Set(prev);
      if (newSet.has(description)) {
        newSet.delete(description);
      } else {
        newSet.add(description);
      }
      return newSet;
    });
  };

  // Expand all groups
  const expandAll = () => {
    const allDescriptions = descriptionGroups.map((g) => g.description);
    setExpandedGroups(new Set(allDescriptions));
  };

  // Collapse all groups
  const collapseAll = () => {
    setExpandedGroups(new Set());
  };

  // Clear search
  const clearSearch = () => {
    setSearchQuery('');
  };

  return (
    <div className="w-full min-h-screen bg-[var(--premium-bg-primary)] pb-[var(--premium-space-2xl)]">
      {/* Sticky Header - Search & Controls */}
      <div
        className="
          sticky top-0 z-20
          bg-[var(--premium-bg-primary)]
          border-b border-[var(--premium-glass-border)]
          px-[var(--premium-space-md)]
          py-[var(--premium-space-md)]
        "
      >
        {/* Search Bar */}
        <div className="relative mb-[var(--premium-space-sm)]">
          <div
            className="
              flex items-center gap-[var(--premium-space-sm)]
              px-[var(--premium-space-md)]
              py-[var(--premium-space-sm)]
              rounded-[var(--premium-radius-lg)]
              bg-[var(--premium-surface-2)]
              border border-[var(--premium-glass-border)]
            "
          >
            <Search size={20} className="text-[var(--premium-text-tertiary)]" />
            <input
              type="text"
              placeholder="Search in descriptions..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="
                flex-1
                bg-transparent
                body-md text-[var(--premium-text-primary)]
                placeholder:text-[var(--premium-text-tertiary)]
                outline-none
              "
            />
            {searchQuery && (
              <button
                onClick={clearSearch}
                className="
                  w-[32px] h-[32px]
                  rounded-full
                  bg-[var(--premium-surface-3)]
                  hover:bg-[var(--premium-surface-4)]
                  active:scale-95
                  flex items-center justify-center
                  transition-all duration-200
                "
              >
                <X size={16} className="text-[var(--premium-text-secondary)]" />
              </button>
            )}
          </div>
        </div>

        {/* Controls Row */}
        <div className="flex items-center justify-between">
          {/* Results Count */}
          <div className="flex items-center gap-[var(--premium-space-xs)]">
            <p className="body-sm text-[var(--premium-text-secondary)]">
              {descriptionGroups.length} group{descriptionGroups.length !== 1 ? 's' : ''}
            </p>
            <span className="body-xs text-[var(--premium-text-tertiary)]">•</span>
            <p className="body-sm text-[var(--premium-text-secondary)]">
              {transactions.length} total
            </p>
          </div>

          {/* Action Buttons */}
          <div className="flex items-center gap-[var(--premium-space-xs)]">
            {/* Expand/Collapse All */}
            <button
              onClick={expandedGroups.size > 0 ? collapseAll : expandAll}
              className="
                px-[var(--premium-space-sm)]
                py-[var(--premium-space-xs)]
                rounded-[var(--premium-radius-md)]
                bg-[var(--premium-surface-2)]
                hover:bg-[var(--premium-surface-3)]
                active:scale-95
                body-xs font-medium text-[var(--premium-text-secondary)]
                transition-all duration-200
              "
            >
              {expandedGroups.size > 0 ? 'Collapse' : 'Expand'} All
            </button>

            {/* Sort Toggle */}
            <div className="relative">
              <button
                onClick={() => setShowSortMenu(!showSortMenu)}
                className="
                  flex items-center gap-[4px]
                  px-[var(--premium-space-sm)]
                  py-[var(--premium-space-xs)]
                  rounded-[var(--premium-radius-md)]
                  bg-[var(--premium-surface-2)]
                  hover:bg-[var(--premium-surface-3)]
                  active:scale-95
                  body-xs font-medium text-[var(--premium-text-secondary)]
                  transition-all duration-200
                "
              >
                <SlidersHorizontal size={14} />
                Sort
              </button>

              {/* Sort Menu Dropdown */}
              {showSortMenu && (
                <>
                  {/* Backdrop */}
                  <div
                    className="fixed inset-0 z-30"
                    onClick={() => setShowSortMenu(false)}
                  />

                  {/* Menu */}
                  <div
                    className="
                      absolute right-0 top-[calc(100%+8px)] z-40
                      w-[180px]
                      p-[var(--premium-space-xs)]
                      rounded-[var(--premium-radius-lg)]
                      bg-[var(--premium-surface-2)]
                      border border-[var(--premium-glass-border)]
                      shadow-[var(--premium-shadow-lg)]
                    "
                  >
                    <button
                      onClick={() => {
                        setSortMode('recent-date');
                        setShowSortMenu(false);
                      }}
                      className={`
                        w-full
                        px-[var(--premium-space-sm)]
                        py-[var(--premium-space-sm)]
                        rounded-[var(--premium-radius-md)]
                        text-left
                        body-sm font-medium
                        transition-all duration-200
                        ${
                          sortMode === 'recent-date'
                            ? 'bg-[var(--premium-emerald)]/20 text-[var(--premium-emerald)]'
                            : 'text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
                        }
                      `}
                    >
                      Recent Date
                    </button>
                    <button
                      onClick={() => {
                        setSortMode('total-amount');
                        setShowSortMenu(false);
                      }}
                      className={`
                        w-full
                        px-[var(--premium-space-sm)]
                        py-[var(--premium-space-sm)]
                        rounded-[var(--premium-radius-md)]
                        text-left
                        body-sm font-medium
                        transition-all duration-200
                        ${
                          sortMode === 'total-amount'
                            ? 'bg-[var(--premium-emerald)]/20 text-[var(--premium-emerald)]'
                            : 'text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
                        }
                      `}
                    >
                      Total Amount
                    </button>
                    <button
                      onClick={() => {
                        setSortMode('frequency');
                        setShowSortMenu(false);
                      }}
                      className={`
                        w-full
                        px-[var(--premium-space-sm)]
                        py-[var(--premium-space-sm)]
                        rounded-[var(--premium-radius-md)]
                        text-left
                        body-sm font-medium
                        transition-all duration-200
                        ${
                          sortMode === 'frequency'
                            ? 'bg-[var(--premium-emerald)]/20 text-[var(--premium-emerald)]'
                            : 'text-[var(--premium-text-secondary)] hover:bg-[var(--premium-surface-3)]'
                        }
                      `}
                    >
                      Frequency
                    </button>
                  </div>
                </>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Description Groups List */}
      <div className="px-[var(--premium-space-md)] pt-[var(--premium-space-md)]">
        {descriptionGroups.length > 0 ? (
          <div className="space-y-[var(--premium-space-sm)]">
            {descriptionGroups.map((group) => (
              <DescriptionGroupCard
                key={group.description}
                group={group}
                isExpanded={expandedGroups.has(group.description)}
                onToggle={() => toggleGroup(group.description)}
                onTransactionTap={onTransactionTap}
              />
            ))}
          </div>
        ) : (
          <div className="text-center py-[var(--premium-space-4xl)]">
            <Search size={48} className="mx-auto mb-[var(--premium-space-md)] text-[var(--premium-text-tertiary)] opacity-40" />
            <p className="body-lg font-medium text-[var(--premium-text-secondary)] mb-[4px]">
              {searchQuery ? 'No descriptions found' : 'No transactions'}
            </p>
            {searchQuery && (
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                Try a different search term
              </p>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

/**
 * Description Group Card Component
 */
interface DescriptionGroupCardProps {
  group: DescriptionGroup;
  isExpanded: boolean;
  onToggle: () => void;
  onTransactionTap?: (transaction: Transaction) => void;
}

function DescriptionGroupCard({
  group,
  isExpanded,
  onToggle,
  onTransactionTap,
}: DescriptionGroupCardProps) {
  const isPositive = group.netAmount >= 0;

  return (
    <div
      className="
        rounded-[var(--premium-radius-xl)]
        bg-[var(--premium-surface-2)]
        border border-[var(--premium-glass-border)]
        overflow-hidden
      "
    >
      {/* Group Header - Tappable */}
      <button
        onClick={onToggle}
        className="
          w-full
          p-[var(--premium-space-lg)]
          text-left
          hover:bg-[var(--premium-surface-3)]
          active:bg-[var(--premium-surface-4)]
          transition-all duration-200
        "
      >
        <div className="flex items-start justify-between gap-[var(--premium-space-sm)]">
          {/* Left: Chevron + Description Info */}
          <div className="flex items-start gap-[var(--premium-space-sm)] flex-1 min-w-0">
            {/* Chevron */}
            <div className="pt-[2px]">
              {isExpanded ? (
                <ChevronDown size={20} className="text-[var(--premium-text-secondary)]" />
              ) : (
                <ChevronRight size={20} className="text-[var(--premium-text-secondary)]" />
              )}
            </div>

            {/* Description Details */}
            <div className="flex-1 min-w-0">
              {/* Description Title */}
              <h3 className="heading-md text-[var(--premium-text-primary)] mb-[4px] truncate">
                {group.description || '(No description)'}
              </h3>

              {/* Meta Info Row */}
              <div className="flex items-center gap-[var(--premium-space-xs)] flex-wrap">
                {/* Transaction Count */}
                <div className="flex items-center gap-[4px]">
                  <div
                    className="
                      w-[6px] h-[6px]
                      rounded-full
                      bg-[var(--premium-text-tertiary)]
                    "
                  />
                  <p className="body-xs text-[var(--premium-text-tertiary)]">
                    {group.count} transaction{group.count !== 1 ? 's' : ''}
                  </p>
                </div>

                <span className="body-xs text-[var(--premium-text-tertiary)]">•</span>

                {/* Date Range */}
                <div className="flex items-center gap-[4px]">
                  <Calendar size={12} className="text-[var(--premium-text-tertiary)]" />
                  <p className="body-xs text-[var(--premium-text-tertiary)]">
                    {formatDateRange(group.oldestDate, group.mostRecentDate)}
                  </p>
                </div>
              </div>

              {/* Income/Expense Breakdown (if both exist) */}
              {group.incomeAmount > 0 && group.expenseAmount > 0 && (
                <div className="flex items-center gap-[var(--premium-space-sm)] mt-[8px]">
                  <div className="flex items-center gap-[4px]">
                    <TrendingUp size={12} className="text-[var(--premium-emerald)]" />
                    <p className="body-xs text-[var(--premium-emerald)]">
                      ${formatAmount(group.incomeAmount)}
                    </p>
                  </div>
                  <div className="flex items-center gap-[4px]">
                    <TrendingDown size={12} className="text-[#f5576c]" />
                    <p className="body-xs text-[#f5576c]">
                      ${formatAmount(group.expenseAmount)}
                    </p>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Right: Total Amount */}
          <div className="text-right flex-shrink-0">
            <p
              className={`
                heading-lg font-bold
                ${isPositive ? 'text-[var(--premium-emerald)]' : 'text-[#f5576c]'}
              `}
            >
              {isPositive ? '+' : '-'}${formatAmount(Math.abs(group.netAmount))}
            </p>
            <p className="body-xs text-[var(--premium-text-tertiary)]">
              Total {group.incomeAmount > 0 && group.expenseAmount > 0 ? 'net' : ''}
            </p>
          </div>
        </div>
      </button>

      {/* Expanded Transactions List */}
      {isExpanded && (
        <div
          className="
            border-t border-[var(--premium-glass-border)]
            bg-[var(--premium-surface-1)]
          "
        >
          <div className="p-[var(--premium-space-sm)]">
            {group.transactions.map((txn, index) => (
              <TransactionRow
                key={txn.id}
                transaction={txn}
                onTap={onTransactionTap}
                isLast={index === group.transactions.length - 1}
              />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

/**
 * Individual Transaction Row Component
 */
interface TransactionRowProps {
  transaction: Transaction;
  onTap?: (transaction: Transaction) => void;
  isLast: boolean;
}

function TransactionRow({ transaction, onTap, isLast }: TransactionRowProps) {
  const CategoryIcon = transaction.category
    ? CATEGORY_ICONS[transaction.category]
    : null;

  const isExpense = transaction.type === 'expense';

  return (
    <button
      onClick={() => onTap?.(transaction)}
      className={`
        w-full
        p-[var(--premium-space-sm)]
        rounded-[var(--premium-radius-md)]
        hover:bg-[var(--premium-surface-2)]
        active:bg-[var(--premium-surface-3)]
        transition-all duration-200
        ${!isLast ? 'mb-[4px]' : ''}
      `}
    >
      <div className="flex items-center gap-[var(--premium-space-sm)]">
        {/* Category Icon */}
        <div
          className="
            w-[36px] h-[36px]
            rounded-[var(--premium-radius-md)]
            bg-[var(--premium-surface-3)]
            flex items-center justify-center
            flex-shrink-0
          "
        >
          {CategoryIcon ? (
            <CategoryIcon size={18} className="text-[var(--premium-text-secondary)]" />
          ) : (
            <div className="w-[8px] h-[8px] rounded-full bg-[var(--premium-text-tertiary)]" />
          )}
        </div>

        {/* Transaction Details */}
        <div className="flex-1 min-w-0 text-left">
          {/* Date + Category */}
          <div className="flex items-center gap-[var(--premium-space-xs)] mb-[2px]">
            <p className="body-sm font-medium text-[var(--premium-text-primary)]">
              {new Date(transaction.timestamp).toLocaleDateString('en-US', {
                month: 'short',
                day: 'numeric',
              })}
            </p>
            {transaction.category && (
              <>
                <span className="body-xs text-[var(--premium-text-tertiary)]">•</span>
                <p className="body-xs text-[var(--premium-text-tertiary)] truncate">
                  {CATEGORY_LABELS[transaction.category]}
                </p>
              </>
            )}
          </div>

          {/* Account */}
          <p className="body-xs text-[var(--premium-text-tertiary)] truncate">
            {transaction.accountLabel || 'Unknown Account'}
          </p>
        </div>

        {/* Amount */}
        <p
          className={`
            body-lg font-bold flex-shrink-0
            ${isExpense ? 'text-[#f5576c]' : 'text-[var(--premium-emerald)]'}
          `}
        >
          {isExpense ? '-' : '+'}${formatAmount(transaction.amount)}
        </p>
      </div>
    </button>
  );
}

/**
 * Group transactions by description
 */
function groupTransactionsByDescription(
  transactions: Transaction[],
  searchQuery: string,
  sortMode: SortMode
): DescriptionGroup[] {
  // Filter by search query
  const filteredTransactions = searchQuery
    ? transactions.filter((txn) =>
        txn.note?.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : transactions;

  // Group by description
  const groupMap = new Map<string, Transaction[]>();

  filteredTransactions.forEach((txn) => {
    const description = txn.note?.trim() || '';
    const existing = groupMap.get(description) || [];
    groupMap.set(description, [...existing, txn]);
  });

  // Convert to array and calculate aggregates
  const groups: DescriptionGroup[] = Array.from(groupMap.entries()).map(
    ([description, txns]) => {
      const sortedTxns = [...txns].sort(
        (a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
      );

      const incomeAmount = txns
        .filter((t) => t.type === 'income')
        .reduce((sum, t) => sum + t.amount, 0);

      const expenseAmount = txns
        .filter((t) => t.type === 'expense')
        .reduce((sum, t) => sum + t.amount, 0);

      const netAmount = incomeAmount - expenseAmount;
      const totalAmount = incomeAmount + expenseAmount;

      const dates = txns.map((t) => new Date(t.timestamp));
      const mostRecentDate = new Date(Math.max(...dates.map((d) => d.getTime())));
      const oldestDate = new Date(Math.min(...dates.map((d) => d.getTime())));

      return {
        description,
        transactions: sortedTxns,
        totalAmount,
        netAmount,
        count: txns.length,
        mostRecentDate,
        oldestDate,
        incomeAmount,
        expenseAmount,
      };
    }
  );

  // Sort groups
  groups.sort((a, b) => {
    switch (sortMode) {
      case 'recent-date':
        return b.mostRecentDate.getTime() - a.mostRecentDate.getTime();
      case 'total-amount':
        return Math.abs(b.netAmount) - Math.abs(a.netAmount);
      case 'frequency':
        return b.count - a.count;
      default:
        return 0;
    }
  });

  return groups;
}

/**
 * Format amount for display
 */
function formatAmount(amount: number): string {
  if (amount >= 1000000) {
    return (amount / 1000000).toFixed(1) + 'M';
  } else if (amount >= 10000) {
    return (amount / 1000).toFixed(1) + 'k';
  } else if (amount >= 1000) {
    return amount.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
  }
  return amount.toFixed(0);
}

/**
 * Format date range
 */
function formatDateRange(startDate: Date, endDate: Date): string {
  const start = new Date(startDate);
  const end = new Date(endDate);

  // Same day
  if (
    start.getDate() === end.getDate() &&
    start.getMonth() === end.getMonth() &&
    start.getFullYear() === end.getFullYear()
  ) {
    return start.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  }

  // Same month
  if (start.getMonth() === end.getMonth() && start.getFullYear() === end.getFullYear()) {
    return `${start.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
    })} - ${end.getDate()}`;
  }

  // Different months
  return `${start.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
  })} - ${end.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}`;
}
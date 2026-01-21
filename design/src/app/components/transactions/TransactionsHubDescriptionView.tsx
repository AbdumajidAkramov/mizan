/**
 * Transactions Hub - Description View
 * Groups transactions by user-added descriptions or AI-generated insights
 */

import { PremiumCard } from '../premium/PremiumCard';
import { FileText, Tag, TrendingUp, TrendingDown, Sparkles } from 'lucide-react';
import type { Transaction } from '../../../types/domain';

export interface TransactionsHubDescriptionViewProps {
  transactions: Transaction[];
  currentDate: Date;
  onTransactionClick?: (transaction: Transaction) => void;
}

interface DescriptionGroup {
  description: string;
  transactions: Transaction[];
  totalAmount: number;
  type: 'expense' | 'income' | 'mixed';
  isAIGenerated?: boolean;
}

export function TransactionsHubDescriptionView({
  transactions,
  currentDate,
  onTransactionClick,
}: TransactionsHubDescriptionViewProps) {
  // Filter transactions for current month
  const filteredTransactions = transactions.filter(txn => {
    const txnDate = new Date(txn.timestamp);
    return (
      txnDate.getMonth() === currentDate.getMonth() &&
      txnDate.getFullYear() === currentDate.getFullYear()
    );
  });

  // AI-generated insight groups (mock data)
  const aiInsights: DescriptionGroup[] = [
    {
      description: 'Weekly Grocery Shopping',
      transactions: filteredTransactions.filter(t => 
        t.category === 'food' && t.title.toLowerCase().includes('market')
      ),
      totalAmount: 0,
      type: 'expense',
      isAIGenerated: true,
    },
    {
      description: 'Transportation & Commute',
      transactions: filteredTransactions.filter(t => 
        t.category === 'transport'
      ),
      totalAmount: 0,
      type: 'expense',
      isAIGenerated: true,
    },
    {
      description: 'Salary & Income',
      transactions: filteredTransactions.filter(t => 
        t.type === 'income'
      ),
      totalAmount: 0,
      type: 'income',
      isAIGenerated: true,
    },
  ];

  // Group by notes/descriptions
  const descriptionGroups = filteredTransactions.reduce((groups, txn) => {
    const key = txn.notes || 'No description';
    
    if (!groups[key]) {
      groups[key] = {
        description: key,
        transactions: [],
        totalAmount: 0,
        type: 'mixed' as const,
        isAIGenerated: false,
      };
    }
    
    groups[key].transactions.push(txn);
    groups[key].totalAmount += txn.type === 'expense' ? txn.amount : -txn.amount;
    
    return groups;
  }, {} as Record<string, DescriptionGroup>);

  // Calculate totals for AI groups
  const processedAIInsights = aiInsights
    .map(group => ({
      ...group,
      totalAmount: group.transactions.reduce(
        (sum, t) => sum + (t.type === 'expense' ? t.amount : -t.amount), 
        0
      ),
    }))
    .filter(group => group.transactions.length > 0);

  // Combine AI insights with user descriptions
  const allGroups = [
    ...processedAIInsights,
    ...Object.values(descriptionGroups).filter(g => g.description !== 'No description'),
  ];

  // Sort by total amount
  const sortedGroups = allGroups.sort((a, b) => 
    Math.abs(b.totalAmount) - Math.abs(a.totalAmount)
  );

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Header */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-sm)]">
          <Sparkles size={20} className="text-[var(--premium-emerald)]" />
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Smart Grouping
          </h3>
        </div>
        <p className="body-sm text-[var(--premium-text-tertiary)]">
          Transactions grouped by descriptions and AI-detected patterns
        </p>
      </PremiumCard>

      {/* Description Groups */}
      <div className="space-y-[var(--premium-space-lg)]">
        {sortedGroups.map((group, index) => {
          const isExpense = group.type === 'expense' || group.totalAmount > 0;
          
          return (
            <div key={`${group.description}-${index}`} className="space-y-[var(--premium-space-md)]">
              {/* Group Header */}
              <PremiumCard 
                variant={group.isAIGenerated ? 'gradient' : 'glass'} 
                className="p-[var(--premium-space-md)]"
              >
                <div className="flex items-start justify-between mb-[var(--premium-space-sm)]">
                  <div className="flex items-start gap-[var(--premium-space-sm)] flex-1">
                    {group.isAIGenerated ? (
                      <div className="
                        w-[32px] h-[32px]
                        rounded-[var(--premium-radius-sm)]
                        bg-gradient-to-br from-[var(--premium-primary)] to-[var(--premium-primary-dark)]
                        flex items-center justify-center
                        flex-shrink-0
                      ">
                        <Sparkles size={16} className="text-white" />
                      </div>
                    ) : (
                      <div className="
                        w-[32px] h-[32px]
                        rounded-[var(--premium-radius-sm)]
                        bg-[var(--premium-surface-3)]
                        flex items-center justify-center
                        flex-shrink-0
                      ">
                        <FileText size={16} className="text-[var(--premium-text-primary)]" />
                      </div>
                    )}

                    <div className="flex-1">
                      <div className="flex items-center gap-[var(--premium-space-xs)] mb-[4px]">
                        <h4 className="body-md text-[var(--premium-text-primary)] font-medium">
                          {group.description}
                        </h4>
                        {group.isAIGenerated && (
                          <span className="
                            px-[8px] py-[2px]
                            rounded-[var(--premium-radius-xs)]
                            bg-[var(--premium-primary)]/20
                            body-xs text-[var(--premium-primary)]
                            font-medium
                          ">
                            AI
                          </span>
                        )}
                      </div>
                      <p className="body-xs text-[var(--premium-text-tertiary)]">
                        {group.transactions.length} transaction{group.transactions.length !== 1 ? 's' : ''}
                      </p>
                    </div>
                  </div>

                  {/* Total Amount */}
                  <div className="text-right">
                    <p className={`
                      heading-md
                      ${isExpense 
                        ? 'text-[var(--premium-error)]' 
                        : 'text-[var(--premium-success)]'
                      }
                    `}>
                      {isExpense ? '-' : '+'}${Math.abs(group.totalAmount).toFixed(2)}
                    </p>
                  </div>
                </div>

                {/* Progress indicator */}
                <div className="w-full h-[4px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
                  <div 
                    className={`
                      h-full transition-all duration-300
                      ${isExpense 
                        ? 'bg-gradient-to-r from-[var(--premium-error)] to-[var(--premium-error-dark)]' 
                        : 'bg-gradient-to-r from-[var(--premium-success)] to-[var(--premium-emerald)]'
                      }
                    `}
                    style={{ 
                      width: `${Math.min((group.transactions.length / filteredTransactions.length) * 100, 100)}%` 
                    }}
                  />
                </div>
              </PremiumCard>

              {/* Transactions in Group */}
              <div className="space-y-[8px] pl-[var(--premium-space-md)]">
                {group.transactions
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
                        <div className="flex items-center gap-[var(--premium-space-md)]">
                          {/* Type Icon */}
                          <div className={`
                            w-[36px] h-[36px]
                            rounded-[var(--premium-radius-sm)]
                            flex items-center justify-center
                            ${transaction.type === 'income' 
                              ? 'bg-[var(--premium-success)]/10' 
                              : 'bg-[var(--premium-error)]/10'
                            }
                          `}>
                            {transaction.type === 'income' ? (
                              <TrendingUp size={18} className="text-[var(--premium-success)]" />
                            ) : (
                              <TrendingDown size={18} className="text-[var(--premium-error)]" />
                            )}
                          </div>

                          {/* Transaction Info */}
                          <div>
                            <p className="body-md text-[var(--premium-text-primary)] font-medium">
                              {transaction.title}
                            </p>
                            <div className="flex items-center gap-[var(--premium-space-xs)] mt-[2px]">
                              <Tag size={12} className="text-[var(--premium-text-muted)]" />
                              <p className="body-xs text-[var(--premium-text-tertiary)]">
                                {transaction.category}
                              </p>
                              <span className="text-[var(--premium-text-muted)]">•</span>
                              <p className="body-xs text-[var(--premium-text-tertiary)]">
                                {new Date(transaction.timestamp).toLocaleDateString('en-US', { 
                                  month: 'short', 
                                  day: 'numeric' 
                                })}
                              </p>
                            </div>
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
          );
        })}
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
            <FileText size={40} className="text-[var(--premium-text-muted)]" />
          </div>
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-sm)]">
            No descriptions available
          </h3>
          <p className="body-md text-[var(--premium-text-tertiary)] text-center max-w-[280px]">
            Add descriptions to your transactions to see smart grouping
          </p>
        </div>
      )}

      {/* AI Insights Info Card */}
      {processedAIInsights.length > 0 && (
        <PremiumCard variant="glass" className="p-[var(--premium-space-md)]">
          <div className="flex items-start gap-[var(--premium-space-sm)]">
            <Sparkles size={16} className="text-[var(--premium-emerald)] mt-[2px]" />
            <div>
              <p className="body-sm text-[var(--premium-text-primary)] font-medium">
                AI-Powered Insights
              </p>
              <p className="body-xs text-[var(--premium-text-tertiary)] mt-[4px]">
                Smart grouping automatically detects spending patterns and categorizes similar transactions together.
              </p>
            </div>
          </div>
        </PremiumCard>
      )}
    </div>
  );
}

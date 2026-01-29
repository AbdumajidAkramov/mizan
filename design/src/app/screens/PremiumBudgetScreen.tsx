/**
 * Premium Budget Setting Screen
 * Manage budgets by category with visual progress
 */

import { useState } from 'react';
import { PremiumCard } from '../components/premium/PremiumCard';
import { PremiumButton } from '../components/premium/PremiumButton';
import { PremiumBudgetModal, type BudgetData } from '../components/premium/PremiumBudgetModal';
import { CategoryIcon } from '../components/atoms/CategoryIcon';
import { DollarSign, Plus, Edit2, AlertCircle, Trash2 } from 'lucide-react';
import type { TransactionCategory } from '../../types/domain';
import { getCategoryMetadata } from '../../mocks/data';

interface CategoryBudget {
  id: string;
  category: TransactionCategory;
  budgetAmount: number;
  spentAmount: number;
  percentage: number;
  period: 'weekly' | 'monthly' | 'yearly';
  alertEnabled: boolean;
  alertThreshold: number;
}

const MOCK_BUDGETS: CategoryBudget[] = [
  { 
    id: 'budget-1',
    category: 'food', 
    budgetAmount: 500, 
    spentAmount: 387, 
    percentage: 77,
    period: 'monthly',
    alertEnabled: true,
    alertThreshold: 80,
  },
  { 
    id: 'budget-2',
    category: 'transport', 
    budgetAmount: 300, 
    spentAmount: 245, 
    percentage: 82,
    period: 'monthly',
    alertEnabled: true,
    alertThreshold: 80,
  },
  { 
    id: 'budget-3',
    category: 'shopping', 
    budgetAmount: 400, 
    spentAmount: 299, 
    percentage: 75,
    period: 'monthly',
    alertEnabled: true,
    alertThreshold: 75,
  },
  { 
    id: 'budget-4',
    category: 'bills', 
    budgetAmount: 250, 
    spentAmount: 204, 
    percentage: 82,
    period: 'monthly',
    alertEnabled: false,
    alertThreshold: 80,
  },
  { 
    id: 'budget-5',
    category: 'entertainment', 
    budgetAmount: 200, 
    spentAmount: 156, 
    percentage: 78,
    period: 'monthly',
    alertEnabled: true,
    alertThreshold: 90,
  },
  { 
    id: 'budget-6',
    category: 'health', 
    budgetAmount: 150, 
    spentAmount: 89, 
    percentage: 59,
    period: 'monthly',
    alertEnabled: true,
    alertThreshold: 80,
  },
];

// Mock category spending history for smart suggestions
const MOCK_SPENDING_HISTORY: Record<TransactionCategory, number[]> = {
  'food': [420, 450, 387, 410, 395],
  'transport': [280, 245, 260, 270, 255],
  'shopping': [350, 299, 380, 320, 340],
  'bills': [210, 204, 215, 200, 205],
  'entertainment': [180, 156, 170, 160, 175],
  'health': [95, 89, 85, 90, 92],
} as any;

export function PremiumBudgetScreen() {
  const [budgets, setBudgets] = useState<CategoryBudget[]>(MOCK_BUDGETS);
  const [showBudgetModal, setShowBudgetModal] = useState(false);
  const [editingBudget, setEditingBudget] = useState<BudgetData | undefined>();

  const totalBudget = budgets.reduce((sum, b) => sum + b.budgetAmount, 0);
  const totalSpent = budgets.reduce((sum, b) => sum + b.spentAmount, 0);
  const overallPercentage = (totalSpent / totalBudget) * 100;

  const handleAddBudget = () => {
    setEditingBudget(undefined);
    setShowBudgetModal(true);
  };

  const handleEditBudget = (budget: CategoryBudget) => {
    setEditingBudget({
      id: budget.id,
      category: budget.category,
      amount: budget.budgetAmount,
      period: budget.period,
      alert: {
        enabled: budget.alertEnabled,
        threshold: budget.alertThreshold,
      },
    });
    setShowBudgetModal(true);
  };

  const handleSaveBudget = (budgetData: BudgetData) => {
    if (budgetData.id) {
      // Update existing budget
      setBudgets((prevBudgets) =>
        prevBudgets.map((b) =>
          b.id === budgetData.id
            ? {
                ...b,
                budgetAmount: budgetData.amount,
                period: budgetData.period,
                alertEnabled: budgetData.alert.enabled,
                alertThreshold: budgetData.alert.threshold,
                percentage: Math.round((b.spentAmount / budgetData.amount) * 100),
              }
            : b
        )
      );
    } else {
      // Add new budget
      const newBudget: CategoryBudget = {
        id: `budget-${Date.now()}`,
        category: budgetData.category,
        budgetAmount: budgetData.amount,
        spentAmount: 0,
        percentage: 0,
        period: budgetData.period,
        alertEnabled: budgetData.alert.enabled,
        alertThreshold: budgetData.alert.threshold,
      };
      setBudgets((prevBudgets) => [...prevBudgets, newBudget]);
    }
    
    setShowBudgetModal(false);
    setEditingBudget(undefined);
  };

  const handleDeleteBudget = (budgetId: string) => {
    setBudgets((prevBudgets) => prevBudgets.filter((b) => b.id !== budgetId));
  };

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="heading-xl text-[var(--premium-text-primary)] mb-[4px]">
            Budget
          </h1>
          <p className="body-sm text-[var(--premium-text-tertiary)]">
            Manage your spending limits
          </p>
        </div>
        <button
          onClick={handleAddBudget}
          className="
            w-[44px] h-[44px]
            bg-gradient-to-r from-[#10b981] to-[#059669]
            rounded-full
            flex items-center justify-center
            shadow-[0_8px_32px_rgba(16,185,129,0.4)]
            hover:shadow-[0_12px_40px_rgba(16,185,129,0.6)]
            transition-all
          "
        >
          <Plus size={24} className="text-white" />
        </button>
      </div>

      {/* Overall Budget Card */}
      <div className="
        relative
        rounded-[var(--premium-radius-2xl)]
        p-[var(--premium-space-xl)]
        bg-gradient-to-br from-[#10b981] via-[#059669] to-[#047857]
        shadow-[var(--premium-shadow-xl)]
        overflow-hidden
      ">
        {/* Background orbs */}
        <div className="absolute top-0 right-0 w-[150px] h-[150px] bg-white/10 rounded-full blur-[50px]" />
        <div className="absolute bottom-0 left-0 w-[120px] h-[120px] bg-black/10 rounded-full blur-[40px]" />
        
        <div className="relative z-10">
          <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
            <DollarSign size={24} className="text-white" />
            <p className="body-md text-white/80">Total Monthly Budget</p>
          </div>

          <div className="mb-[var(--premium-space-lg)]">
            <div className="flex items-baseline gap-[var(--premium-space-sm)] mb-[var(--premium-space-xs)]">
              <h2 className="display-md text-white">
                ${totalSpent.toLocaleString()}
              </h2>
              <span className="heading-sm text-white/70">
                / ${totalBudget.toLocaleString()}
              </span>
            </div>
            <p className="body-sm text-white/70">
              {overallPercentage.toFixed(0)}% of budget used
            </p>
          </div>

          {/* Progress Bar */}
          <div className="w-full h-[10px] bg-white/20 rounded-full overflow-hidden">
            <div
              className="h-full bg-white rounded-full transition-all duration-500"
              style={{ width: `${Math.min(overallPercentage, 100)}%` }}
            />
          </div>

          {/* Remaining */}
          <div className="mt-[var(--premium-space-md)] flex justify-between items-center">
            <p className="body-sm text-white/70">Remaining</p>
            <p className="heading-md text-white">
              ${(totalBudget - totalSpent).toLocaleString()}
            </p>
          </div>
        </div>
      </div>

      {/* Category Budgets */}
      <div>
        <h3 className="heading-md text-[var(--premium-text-primary)] mb-[var(--premium-space-md)]">
          Category Budgets
        </h3>

        <div className="space-y-[var(--premium-space-md)]">
          {budgets.map(budget => {
            const categoryMeta = getCategoryMetadata(budget.category);
            const isOverBudget = budget.percentage >= 100;
            const isNearLimit = budget.percentage >= 80 && budget.percentage < 100;

            return (
              <PremiumCard
                key={budget.id}
                variant="glass"
                className="p-[var(--premium-space-lg)]"
              >
                <div className="flex items-center gap-[var(--premium-space-md)] mb-[var(--premium-space-md)]">
                  {/* Icon */}
                  <div
                    className="
                      w-[52px] h-[52px]
                      rounded-[var(--premium-radius-md)]
                      flex items-center justify-center
                      flex-shrink-0
                    "
                    style={{
                      background: `linear-gradient(135deg, ${categoryMeta?.colorToken}40, ${categoryMeta?.colorToken}20)`,
                    }}
                  >
                    <CategoryIcon
                      category={budget.category}
                      size={24}
                      color={categoryMeta?.colorToken}
                    />
                  </div>

                  {/* Info */}
                  <div className="flex-1 min-w-0">
                    <h4 className="heading-sm text-[var(--premium-text-primary)] mb-[4px]">
                      {categoryMeta?.label}
                    </h4>
                    <div className="flex items-center gap-[var(--premium-space-sm)]">
                      <p className="body-md text-[var(--premium-text-secondary)]">
                        ${budget.spentAmount} / ${budget.budgetAmount}
                      </p>
                      {isOverBudget && (
                        <div className="flex items-center gap-[4px] px-[8px] py-[2px] bg-[var(--premium-error)]/20 rounded-full">
                          <AlertCircle size={12} className="text-[var(--premium-error)]" />
                          <span className="body-xs text-[var(--premium-error)] font-medium">
                            Over budget
                          </span>
                        </div>
                      )}
                      {isNearLimit && !isOverBudget && (
                        <div className="flex items-center gap-[4px] px-[8px] py-[2px] bg-[var(--premium-warning)]/20 rounded-full">
                          <AlertCircle size={12} className="text-[var(--premium-warning)]" />
                          <span className="body-xs text-[var(--premium-warning)] font-medium">
                            Near limit
                          </span>
                        </div>
                      )}
                    </div>
                  </div>

                  {/* Edit & Delete Buttons */}
                  <div className="flex items-center gap-[var(--premium-space-xs)]">
                    <button
                      onClick={() => handleEditBudget(budget)}
                      className="
                        w-[36px] h-[36px]
                        bg-[var(--premium-surface-2)]
                        rounded-full
                        flex items-center justify-center
                        hover:bg-[var(--premium-accent)]/20
                        hover:text-[var(--premium-accent)]
                        transition-all
                        flex-shrink-0
                      "
                    >
                      <Edit2 size={16} className="text-[var(--premium-text-secondary)]" />
                    </button>
                    
                    <button
                      onClick={() => handleDeleteBudget(budget.id)}
                      className="
                        w-[36px] h-[36px]
                        bg-[var(--premium-surface-2)]
                        rounded-full
                        flex items-center justify-center
                        hover:bg-[var(--premium-error)]/20
                        hover:text-[var(--premium-error)]
                        transition-all
                        flex-shrink-0
                      "
                    >
                      <Trash2 size={16} className="text-[var(--premium-text-secondary)]" />
                    </button>
                  </div>
                </div>

                {/* Progress Bar */}
                <div className="relative">
                  <div className="w-full h-[8px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
                    <div
                      className="h-full rounded-full transition-all duration-500"
                      style={{
                        width: `${Math.min(budget.percentage, 100)}%`,
                        background: isOverBudget
                          ? 'var(--premium-error)'
                          : isNearLimit
                          ? 'var(--premium-warning)'
                          : `linear-gradient(to right, ${categoryMeta?.colorToken}, ${categoryMeta?.colorToken}80)`,
                      }}
                    />
                  </div>
                  
                  {/* Percentage Label */}
                  <div className="flex justify-between items-center mt-[var(--premium-space-xs)]">
                    <p className="body-xs text-[var(--premium-text-muted)]">
                      {budget.percentage}% used
                    </p>
                    <p className="body-xs text-[var(--premium-text-muted)]">
                      ${budget.budgetAmount - budget.spentAmount} left
                    </p>
                  </div>
                </div>
              </PremiumCard>
            );
          })}
        </div>
      </div>

      {/* Add More Categories */}
      <PremiumCard
        variant="glass"
        hover
        onClick={handleAddBudget}
        className="p-[var(--premium-space-lg)] cursor-pointer"
      >
        <div className="flex items-center justify-center gap-[var(--premium-space-sm)] text-[var(--premium-primary)]">
          <Plus size={20} />
          <p className="body-md font-medium">Add Budget for Category</p>
        </div>
      </PremiumCard>

      {/* Tips Card */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="flex gap-[var(--premium-space-md)]">
          <div className="text-[32px]">💡</div>
          <div>
            <h4 className="heading-sm text-[var(--premium-text-primary)] mb-[var(--premium-space-xs)]">
              Budget Tips
            </h4>
            <ul className="space-y-[var(--premium-space-xs)]">
              <li className="body-sm text-[var(--premium-text-tertiary)]">
                • Set realistic budgets based on your spending patterns
              </li>
              <li className="body-sm text-[var(--premium-text-tertiary)]">
                • Review and adjust budgets monthly
              </li>
              <li className="body-sm text-[var(--premium-text-tertiary)]">
                • Use the 50/30/20 rule: 50% needs, 30% wants, 20% savings
              </li>
            </ul>
          </div>
        </div>
      </PremiumCard>

      {/* Budget Modal */}
      <PremiumBudgetModal
        isOpen={showBudgetModal}
        onClose={() => {
          setShowBudgetModal(false);
          setEditingBudget(undefined);
        }}
        onSave={handleSaveBudget}
        existingBudget={editingBudget}
        monthlyIncome={5000}
        categorySpendingHistory={MOCK_SPENDING_HISTORY}
      />
    </div>
  );
}
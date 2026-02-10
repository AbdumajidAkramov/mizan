/**
 * Premium Create Budget Screen
 * Set spending limits for categories with period selection and notifications
 * 
 * @features
 * - Large amount input with UZS formatting
 * - Category selection with glassmorphism card
 * - Period selection (Monthly/Weekly/Custom)
 * - Notification toggle for 80% usage alert
 * - Validation and disabled states
 * 
 * @architecture Material 3 with glassmorphism
 * @design Deep dark blue/purple (#1A1A2E) background, 8dp grid system
 */

import { useState } from 'react';
import {
  ChevronLeft,
  ChevronRight,
  DollarSign,
  Calendar,
  Bell,
  Wallet,
  ShoppingBag,
  Car,
  Home,
  Utensils,
  Coffee,
} from 'lucide-react';

/**
 * Budget Category Interface
 */
export interface BudgetCategory {
  id: string;
  name: string;
  icon: typeof Wallet;
  color: string;
}

/**
 * Budget Period Type
 */
export type BudgetPeriod = 'monthly' | 'weekly' | 'custom';

/**
 * New Budget State Interface
 */
export interface NewBudgetState {
  amount: number;
  categoryId: string | null;
  categoryName: string | null;
  categoryIcon: typeof Wallet | null;
  categoryColor: string | null;
  period: BudgetPeriod;
  notifyAt80: boolean;
}

export interface PremiumCreateBudgetScreenProps {
  /** Callback when back button is pressed */
  onBack: () => void;
  /** Callback to open category selector */
  onSelectCategory?: () => void;
  /** Callback when budget is created */
  onCreate?: (budget: NewBudgetState) => void;
  /** Pre-selected category (optional) */
  preSelectedCategory?: BudgetCategory;
}

// Mock Categories for demo
const AVAILABLE_CATEGORIES: BudgetCategory[] = [
  { id: 'food-dining', name: 'Food & Dining', icon: Utensils, color: '#10b981' },
  { id: 'transportation', name: 'Transportation', icon: Car, color: '#667eea' },
  { id: 'shopping', name: 'Shopping', icon: ShoppingBag, color: '#f5576c' },
  { id: 'housing', name: 'Housing', icon: Home, color: '#4facfe' },
  { id: 'entertainment', name: 'Entertainment', icon: Coffee, color: '#c471f5' },
];

/**
 * Format number with spaces: "12 345.64"
 */
function formatAmount(amount: string): string {
  if (!amount) return '';
  
  // Handle decimal point
  const parts = amount.split('.');
  const integerPart = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
  const decimalPart = parts[1] ? `.${parts[1].slice(0, 2)}` : '';
  
  return integerPart + decimalPart;
}

/**
 * Period Chip Component
 */
function PeriodChip({
  label,
  isSelected,
  onClick,
}: {
  label: string;
  isSelected: boolean;
  onClick: () => void;
}) {
  return (
    <button
      onClick={onClick}
      className={`
        flex-1
        px-[var(--premium-space-md)]
        py-[var(--premium-space-sm)]
        rounded-[var(--premium-radius-lg)]
        transition-all duration-200
        active:scale-95
        ${
          isSelected
            ? 'bg-[#10B981] border border-[#10B981]'
            : 'bg-white/5 border border-white/10 hover:bg-white/10 hover:border-white/20'
        }
      `}
    >
      <span
        className={`body-sm font-semibold ${
          isSelected ? 'text-white' : 'text-white/60'
        }`}
      >
        {label}
      </span>
    </button>
  );
}

/**
 * Toggle Switch Component
 */
function ToggleSwitch({
  enabled,
  onChange,
}: {
  enabled: boolean;
  onChange: (enabled: boolean) => void;
}) {
  return (
    <button
      onClick={() => onChange(!enabled)}
      className={`
        relative w-[52px] h-[28px]
        rounded-full
        transition-all duration-200
        ${enabled ? 'bg-[#10B981]' : 'bg-white/10'}
      `}
    >
      <div
        className={`
          absolute top-[2px]
          w-[24px] h-[24px]
          rounded-full
          bg-white
          shadow-lg
          transition-all duration-200
          ${enabled ? 'left-[26px]' : 'left-[2px]'}
        `}
      />
    </button>
  );
}

/**
 * Premium Create Budget Screen Component
 */
export function PremiumCreateBudgetScreen({
  onBack,
  onSelectCategory,
  onCreate,
  preSelectedCategory,
}: PremiumCreateBudgetScreenProps) {
  const [amountInput, setAmountInput] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<BudgetCategory | null>(
    preSelectedCategory || null
  );
  const [period, setPeriod] = useState<BudgetPeriod>('monthly');
  const [notifyAt80, setNotifyAt80] = useState(true);

  // Parse amount as number
  const amount = parseFloat(amountInput.replace(/\s/g, '')) || 0;

  // Validation
  const isValid = amount > 0 && selectedCategory !== null;

  const handleAmountChange = (value: string) => {
    // Only allow numbers and decimal point
    const sanitized = value.replace(/[^\d.]/g, '');
    
    // Prevent multiple decimal points
    const parts = sanitized.split('.');
    if (parts.length > 2) return;
    
    setAmountInput(sanitized);
  };

  const handleCreateBudget = () => {
    if (!isValid) return;

    const newBudget: NewBudgetState = {
      amount,
      categoryId: selectedCategory!.id,
      categoryName: selectedCategory!.name,
      categoryIcon: selectedCategory!.icon,
      categoryColor: selectedCategory!.color,
      period,
      notifyAt80,
    };

    onCreate?.(newBudget);
  };

  const handleSelectCategoryClick = () => {
    // In a real implementation, this would open a category picker modal
    // For demo, we'll cycle through categories
    const currentIndex = selectedCategory
      ? AVAILABLE_CATEGORIES.findIndex((c) => c.id === selectedCategory.id)
      : -1;
    const nextIndex = (currentIndex + 1) % AVAILABLE_CATEGORIES.length;
    setSelectedCategory(AVAILABLE_CATEGORIES[nextIndex]);
    
    // Call the callback if provided
    onSelectCategory?.();
  };

  return (
    <div className="fixed inset-0 z-50 bg-[#1A1A2E] flex flex-col">
      {/* Header */}
      <div
        className="
          flex-shrink-0
          px-[var(--premium-space-lg)]
          pt-[var(--premium-space-xl)]
          pb-[var(--premium-space-md)]
        "
      >
        <div className="flex items-center gap-[var(--premium-space-md)]">
          <button
            onClick={onBack}
            className="
              w-[40px] h-[40px]
              rounded-full
              bg-white/5
              backdrop-blur-xl
              border border-white/10
              hover:bg-white/10
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
            "
          >
            <ChevronLeft size={20} className="text-white" />
          </button>
          <div>
            <h1 className="heading-lg text-white font-semibold">
              Set Budget
            </h1>
          </div>
        </div>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)]">
        <div className="space-y-[var(--premium-space-xl)] pb-[var(--premium-space-2xl)]">
          {/* Section 1: Budget Amount Input */}
          <div
            className="
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <label className="body-sm text-white/60 mb-[var(--premium-space-md)] block">
              Budget Amount
            </label>

            {/* Amount Display */}
            <div className="flex items-baseline justify-center mb-[var(--premium-space-lg)]">
              <input
                type="text"
                inputMode="decimal"
                placeholder="0"
                value={amountInput}
                onChange={(e) => handleAmountChange(e.target.value)}
                autoFocus
                className="
                  bg-transparent
                  text-[48px] font-bold text-white
                  text-center
                  outline-none
                  w-full
                  placeholder:text-white/20
                  tracking-tight
                "
              />
            </div>

            {/* Formatted Display */}
            <div className="text-center">
              <div className="flex items-baseline justify-center gap-[4px]">
                <span className="text-[24px] font-semibold text-white/80">
                  {formatAmount(amountInput) || '0'}
                </span>
                <span className="text-[16px] font-medium text-white/40">
                  UZS
                </span>
              </div>
              <p className="body-xs text-white/40 mt-[4px]">
                Maximum spending limit
              </p>
            </div>
          </div>

          {/* Section 2: Configuration Cards */}
          <div className="space-y-[var(--premium-space-md)]">
            {/* Category Selection Card */}
            <button
              onClick={handleSelectCategoryClick}
              className="
                w-full
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-white/5
                backdrop-blur-xl
                border border-white/10
                hover:bg-white/10
                hover:border-white/20
                transition-all duration-200
                active:scale-[0.98]
              "
            >
              <div className="flex items-center gap-[var(--premium-space-md)]">
                {/* Category Icon */}
                {selectedCategory ? (
                  <div
                    className="
                      w-[56px] h-[56px]
                      rounded-[var(--premium-radius-lg)]
                      flex items-center justify-center
                      flex-shrink-0
                    "
                    style={{
                      backgroundColor: `${selectedCategory.color}20`,
                      border: `1px solid ${selectedCategory.color}30`,
                    }}
                  >
                    <selectedCategory.icon
                      size={28}
                      style={{ color: selectedCategory.color }}
                    />
                  </div>
                ) : (
                  <div
                    className="
                      w-[56px] h-[56px]
                      rounded-[var(--premium-radius-lg)]
                      flex items-center justify-center
                      flex-shrink-0
                      bg-white/10
                      border border-white/20
                      border-dashed
                    "
                  >
                    <Wallet size={28} className="text-white/40" />
                  </div>
                )}

                {/* Category Info */}
                <div className="flex-1 text-left">
                  <p className="body-xs text-white/40 mb-[2px]">
                    Category
                  </p>
                  <p className="body-lg font-semibold text-white">
                    {selectedCategory ? selectedCategory.name : 'Select Category'}
                  </p>
                </div>

                {/* Chevron */}
                <ChevronRight size={20} className="text-white/30" />
              </div>
            </button>

            {/* Period Selection Card */}
            <div
              className="
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-white/5
                backdrop-blur-xl
                border border-white/10
              "
            >
              <label className="body-xs text-white/40 mb-[var(--premium-space-md)] block">
                Budget Period
              </label>

              {/* Period Chips */}
              <div className="flex gap-[8px]">
                <PeriodChip
                  label="Weekly"
                  isSelected={period === 'weekly'}
                  onClick={() => setPeriod('weekly')}
                />
                <PeriodChip
                  label="Monthly"
                  isSelected={period === 'monthly'}
                  onClick={() => setPeriod('monthly')}
                />
                <PeriodChip
                  label="Custom"
                  isSelected={period === 'custom'}
                  onClick={() => setPeriod('custom')}
                />
              </div>
            </div>
          </div>

          {/* Section 3: Notification Toggle */}
          <div
            className="
              p-[var(--premium-space-lg)]
              rounded-[var(--premium-radius-xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <div className="flex items-center justify-between">
              {/* Left: Icon and Text */}
              <div className="flex items-center gap-[var(--premium-space-md)]">
                <div
                  className="
                    w-[44px] h-[44px]
                    rounded-[var(--premium-radius-lg)]
                    flex items-center justify-center
                    bg-[#10B981]/20
                    border border-[#10B981]/30
                  "
                >
                  <Bell size={20} className="text-[#10B981]" />
                </div>
                <div>
                  <p className="body-md font-medium text-white">
                    Budget Alert
                  </p>
                  <p className="body-xs text-white/40">
                    Notify me at 80% usage
                  </p>
                </div>
              </div>

              {/* Right: Toggle Switch */}
              <ToggleSwitch enabled={notifyAt80} onChange={setNotifyAt80} />
            </div>
          </div>

          {/* Info Card */}
          <div
            className="
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-lg)]
              bg-[#10B981]/10
              border border-[#10B981]/20
            "
          >
            <p className="body-xs text-white/60 text-center">
              {period === 'monthly' && 'Your budget will reset on the 1st of each month'}
              {period === 'weekly' && 'Your budget will reset every Monday'}
              {period === 'custom' && 'You can set a custom date range for this budget'}
            </p>
          </div>
        </div>
      </div>

      {/* Bottom Action Area */}
      <div
        className="
          flex-shrink-0
          p-[var(--premium-space-lg)]
          bg-gradient-to-t from-[#1A1A2E] via-[#1A1A2E] to-transparent
          border-t border-white/5
        "
      >
        <button
          onClick={handleCreateBudget}
          disabled={!isValid}
          className={`
            w-full
            h-[56px]
            rounded-[var(--premium-radius-xl)]
            font-semibold
            transition-all duration-200
            ${
              isValid
                ? 'bg-[#10B981] text-white hover:bg-[#0ea572] active:scale-[0.98] shadow-lg shadow-[#10B981]/30'
                : 'bg-white/5 text-white/30 cursor-not-allowed border border-white/10'
            }
          `}
        >
          {isValid ? (
            'Create Budget'
          ) : (
            <span className="flex items-center justify-center gap-2">
              {amount === 0 && 'Enter amount'}
              {amount > 0 && !selectedCategory && 'Select category'}
            </span>
          )}
        </button>

        {/* Validation Hints */}
        {!isValid && (
          <div className="flex items-center justify-center gap-2 mt-[var(--premium-space-sm)]">
            {amount === 0 && (
              <div className="flex items-center gap-1">
                <div className="w-[6px] h-[6px] rounded-full bg-white/20" />
                <p className="body-xs text-white/30">Amount required</p>
              </div>
            )}
            {!selectedCategory && (
              <div className="flex items-center gap-1">
                <div className="w-[6px] h-[6px] rounded-full bg-white/20" />
                <p className="body-xs text-white/30">Category required</p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

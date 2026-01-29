/**
 * Premium Transaction Type Selector - Overlay Menu
 * Quick switcher for Expense, Income, and Transfer
 * 
 * @architecture Material 3 with glassmorphism
 * @design Emerald Green (#10B981) accents
 */

import {
  ArrowUpRight,
  ArrowDownLeft,
  ArrowLeftRight,
  Check,
} from 'lucide-react';
import type { TransactionType } from '../../types/domain';

export interface PremiumTransactionTypeSelectorProps {
  /** Whether the overlay is open */
  isOpen: boolean;
  /** Current selected type */
  selectedType: TransactionType;
  /** Callback when type is selected */
  onSelectType: (type: TransactionType) => void;
  /** Callback when overlay is closed */
  onClose: () => void;
}

export function PremiumTransactionTypeSelector({
  isOpen,
  selectedType,
  onSelectType,
  onClose,
}: PremiumTransactionTypeSelectorProps) {
  if (!isOpen) return null;

  const types: Array<{
    value: TransactionType;
    label: string;
    description: string;
    icon: typeof ArrowUpRight;
    color: string;
    bgColor: string;
  }> = [
    {
      value: 'expense',
      label: 'Expense',
      description: 'Money spent',
      icon: ArrowUpRight,
      color: '#f5576c',
      bgColor: 'rgba(245, 87, 108, 0.1)',
    },
    {
      value: 'income',
      label: 'Income',
      description: 'Money received',
      icon: ArrowDownLeft,
      color: '#4facfe',
      bgColor: 'rgba(79, 172, 254, 0.1)',
    },
    {
      value: 'transfer',
      label: 'Transfer',
      description: 'Move between accounts',
      icon: ArrowLeftRight,
      color: '#10b981',
      bgColor: 'rgba(16, 185, 129, 0.1)',
    },
  ];

  return (
    <div className="fixed inset-0 z-[110] flex items-center justify-center animate-[fadeIn_0.2s_ease-out]">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-black/40 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Overlay Menu */}
      <div
        className="
          relative
          w-full
          max-w-[360px]
          mx-[var(--premium-space-lg)]
          bg-[var(--premium-surface)]
          rounded-[var(--premium-radius-2xl)]
          shadow-[var(--premium-shadow-2xl)]
          overflow-hidden
          animate-[scaleIn_0.2s_ease-out]
        "
      >
        {/* Header */}
        <div className="px-[var(--premium-space-lg)] pt-[var(--premium-space-lg)] pb-[var(--premium-space-md)]">
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Transaction Type
          </h3>
          <p className="body-sm text-[var(--premium-text-tertiary)] mt-[4px]">
            Choose the type of transaction
          </p>
        </div>

        {/* Type Options */}
        <div className="px-[var(--premium-space-lg)] pb-[var(--premium-space-lg)] space-y-[8px]">
          {types.map((type) => {
            const Icon = type.icon;
            const isSelected = selectedType === type.value;

            return (
              <button
                key={type.value}
                onClick={() => {
                  onSelectType(type.value);
                  onClose();
                }}
                className={`
                  w-full
                  p-[var(--premium-space-md)]
                  rounded-[var(--premium-radius-xl)]
                  flex items-center gap-[var(--premium-space-md)]
                  transition-all duration-200
                  ${isSelected
                    ? 'bg-[var(--premium-emerald)]/10 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)]'
                    : 'bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)]'
                  }
                  active:scale-[0.98]
                `}
              >
                {/* Icon */}
                <div
                  className="
                    w-[48px] h-[48px]
                    rounded-[var(--premium-radius-lg)]
                    flex items-center justify-center
                    flex-shrink-0
                  "
                  style={{
                    backgroundColor: isSelected ? type.color : type.bgColor,
                  }}
                >
                  <Icon
                    size={24}
                    strokeWidth={2}
                    style={{ color: isSelected ? 'white' : type.color }}
                  />
                </div>

                {/* Label */}
                <div className="flex-1 text-left">
                  <p
                    className={`
                      body-md font-medium mb-[2px]
                      ${isSelected ? 'text-[var(--premium-emerald)]' : 'text-[var(--premium-text-primary)]'}
                    `}
                  >
                    {type.label}
                  </p>
                  <p className="body-sm text-[var(--premium-text-tertiary)]">
                    {type.description}
                  </p>
                </div>

                {/* Check Icon */}
                {isSelected && (
                  <div
                    className="
                      w-[24px] h-[24px]
                      rounded-full
                      bg-[var(--premium-emerald)]
                      flex items-center justify-center
                      flex-shrink-0
                    "
                  >
                    <Check size={14} className="text-white" strokeWidth={3} />
                  </div>
                )}
              </button>
            );
          })}
        </div>
      </div>

      <style>{`
        @keyframes scaleIn {
          from {
            opacity: 0;
            transform: scale(0.9);
          }
          to {
            opacity: 1;
            transform: scale(1);
          }
        }
      `}</style>
    </div>
  );
}

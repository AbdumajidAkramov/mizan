/**
 * Horizontal Account Card Component
 * Credit card-style design for account selection
 */

import { Wallet, Building2, Landmark, CreditCard, PiggyBank } from 'lucide-react';

export type AccountType = 'cash' | 'bank' | 'credit' | 'savings' | 'investment';

export interface Account {
  id: string;
  name: string;
  type: AccountType;
  balance: number;
  icon: typeof Wallet;
  color: string;
}

export interface HorizontalAccountCardProps {
  account: Account;
  isSelected: boolean;
  onSelect: () => void;
}

export function HorizontalAccountCard({
  account,
  isSelected,
  onSelect,
}: HorizontalAccountCardProps) {
  const Icon = account.icon;

  return (
    <button
      onClick={onSelect}
      className={`
        relative
        flex-shrink-0
        w-[280px] h-[140px]
        rounded-[var(--premium-radius-xl)]
        p-[var(--premium-space-lg)]
        backdrop-blur-[30px]
        border-2
        transition-all duration-300
        active:scale-95
        ${isSelected
          ? 'bg-[var(--premium-glass-bg)] border-[var(--premium-emerald)] shadow-[0_0_30px_rgba(16,185,129,0.3)] scale-105'
          : 'bg-[var(--premium-surface-2)] border-[var(--premium-glass-border)] hover:border-[var(--premium-surface-4)] hover:scale-[1.02]'
        }
      `}
    >
      {/* Background Gradient Overlay */}
      <div 
        className="
          absolute inset-0
          rounded-[var(--premium-radius-xl)]
          opacity-[0.05]
          pointer-events-none
        "
        style={{
          background: `linear-gradient(135deg, ${account.color} 0%, transparent 100%)`,
        }}
      />

      {/* Card Content */}
      <div className="relative h-full flex flex-col justify-between">
        {/* Top Section - Icon & Type */}
        <div className="flex items-start justify-between">
          <div 
            className="
              w-[48px] h-[48px]
              rounded-[var(--premium-radius-md)]
              flex items-center justify-center
            "
            style={{
              background: `${account.color}20`,
            }}
          >
            <Icon size={24} strokeWidth={2} style={{ color: account.color }} />
          </div>

          {/* Selected Indicator */}
          {isSelected && (
            <div className="
              w-[24px] h-[24px]
              rounded-full
              bg-[var(--premium-emerald)]
              flex items-center justify-center
              animate-[scaleIn_0.2s_ease-out]
            ">
              <svg
                width="14"
                height="14"
                viewBox="0 0 14 14"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M2 7L5.5 10.5L12 3.5"
                  stroke="white"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
          )}
        </div>

        {/* Bottom Section - Account Info */}
        <div>
          <p className={`
            body-md font-medium mb-[4px]
            ${isSelected
              ? 'text-[var(--premium-text-primary)]'
              : 'text-[var(--premium-text-secondary)]'
            }
          `}>
            {account.name}
          </p>
          <p className={`
            heading-5
            ${isSelected
              ? 'text-[var(--premium-emerald)]'
              : 'text-[var(--premium-text-primary)]'
            }
          `}>
            ${account.balance.toLocaleString('en-US', {
              minimumFractionDigits: 2,
              maximumFractionDigits: 2,
            })}
          </p>
        </div>
      </div>

      {/* Shine Effect on Hover */}
      {!isSelected && (
        <div className="
          absolute inset-0
          rounded-[var(--premium-radius-xl)]
          opacity-0
          hover:opacity-100
          transition-opacity duration-300
          pointer-events-none
          bg-gradient-to-r from-transparent via-white/5 to-transparent
          -translate-x-full
          group-hover:translate-x-full
          duration-700
        " />
      )}
    </button>
  );
}

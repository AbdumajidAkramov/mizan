/**
 * Transfer Accounts Selection Screen
 * Horizontal carousel layout with credit card-style components
 * Full-screen experience with Material 3 Top App Bar
 */

import { useState } from 'react';
import { ArrowLeft, ArrowDown, Check } from 'lucide-react';
import { Wallet, Building2, Landmark, CreditCard, PiggyBank } from 'lucide-react';
import { HorizontalAccountCarousel } from '../components/premium/HorizontalAccountCarousel';
import type { Account } from '../components/premium/HorizontalAccountCard';

export interface TransferAccountsScreenProps {
  amount: string;
  onBack: () => void;
  onContinue: (fromAccountId: string, toAccountId: string) => void;
}

// Mock Accounts Data
const MOCK_ACCOUNTS: Account[] = [
  {
    id: 'cash',
    name: 'Cash',
    type: 'cash',
    balance: 1250.00,
    icon: Wallet,
    color: '#10b981',
  },
  {
    id: 'bank-checking',
    name: 'Bank Checking',
    type: 'bank',
    balance: 5430.50,
    icon: Building2,
    color: '#667eea',
  },
  {
    id: 'savings',
    name: 'Savings Account',
    type: 'savings',
    balance: 12500.00,
    icon: PiggyBank,
    color: '#4facfe',
  },
  {
    id: 'credit-card',
    name: 'Credit Card',
    type: 'credit',
    balance: -850.00,
    icon: CreditCard,
    color: '#f5576c',
  },
  {
    id: 'investment',
    name: 'Investment',
    type: 'investment',
    balance: 8200.00,
    icon: Landmark,
    color: '#c471f5',
  },
];

export function TransferAccountsScreen({
  amount,
  onBack,
  onContinue,
}: TransferAccountsScreenProps) {
  const [fromAccountId, setFromAccountId] = useState<string>();
  const [toAccountId, setToAccountId] = useState<string>();

  const availableToAccounts = MOCK_ACCOUNTS.filter(
    account => account.id !== fromAccountId
  );

  const canContinue = fromAccountId && toAccountId;

  const handleContinue = () => {
    if (fromAccountId && toAccountId) {
      onContinue(fromAccountId, toAccountId);
    }
  };

  return (
    <div className="
      fixed inset-0 z-[100]
      bg-[var(--premium-bg-primary)]
      flex flex-col
    ">
      {/* Material 3 Top App Bar */}
      <div className="
        sticky top-0 z-50
        bg-[var(--premium-bg-primary)]
        border-b border-[var(--premium-glass-border)]
      ">
        <div className="
          max-w-lg mx-auto
          h-[64px]
          px-[var(--premium-space-md)]
          flex items-center justify-between
        ">
          {/* Left: Back Button */}
          <button
            onClick={onBack}
            className="
              w-[40px] h-[40px]
              rounded-full
              flex items-center justify-center
              text-[var(--premium-text-primary)]
              hover:bg-[var(--premium-surface-2)]
              active:scale-95
              transition-all duration-200
            "
            aria-label="Back"
          >
            <ArrowLeft size={24} strokeWidth={2} />
          </button>

          {/* Center: Title */}
          <h1 className="heading-5 text-[var(--premium-text-primary)]">
            Select Accounts
          </h1>

          {/* Right: Continue Button (only show when ready) */}
          {canContinue ? (
            <button
              onClick={handleContinue}
              className="
                h-[40px]
                px-[var(--premium-space-md)]
                rounded-[var(--premium-radius-full)]
                bg-[var(--premium-emerald)]
                text-white
                font-medium
                flex items-center gap-[8px]
                hover:bg-[var(--premium-emerald-dark)]
                active:scale-95
                transition-all duration-200
              "
            >
              <Check size={18} strokeWidth={2.5} />
              Continue
            </button>
          ) : (
            <div className="w-[40px]" /> // Spacer
          )}
        </div>
      </div>

      {/* Zen Background - Subtle Gradient Orbs */}
      <div className="fixed inset-0 pointer-events-none overflow-hidden -z-10">
        <div 
          className="absolute top-[-10%] right-[-10%] w-[60%] h-[60%] rounded-full opacity-[0.03] blur-[150px]"
          style={{ background: 'radial-gradient(circle, var(--premium-emerald) 0%, transparent 70%)' }}
        />
        <div 
          className="absolute bottom-[-10%] left-[-10%] w-[50%] h-[50%] rounded-full opacity-[0.03] blur-[120px]"
          style={{ background: 'radial-gradient(circle, #667eea 0%, transparent 70%)' }}
        />
      </div>

      {/* Main Content */}
      <div className="
        flex-1
        overflow-y-auto
      ">
        <div className="
          max-w-lg mx-auto
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-2xl)]
        ">
          {/* Amount Summary */}
          <div className="
            text-center
            mb-[var(--premium-space-2xl)]
          ">
            <p className="body-sm text-[var(--premium-text-tertiary)] mb-[8px]">
              Transfer Amount
            </p>
            <h2 className="
              text-[56px] font-light
              text-[var(--premium-text-primary)]
              leading-none
            ">
              ${amount}
            </h2>
          </div>

          {/* Compact Transfer Layout */}
          <div className="space-y-[var(--premium-space-lg)]">
            {/* From Account Carousel */}
            <HorizontalAccountCarousel
              label="From Account"
              accounts={MOCK_ACCOUNTS}
              selectedAccountId={fromAccountId}
              onSelectAccount={setFromAccountId}
            />

            {/* Central Transfer Indicator */}
            <div className="flex justify-center py-[var(--premium-space-sm)]">
              <div className="
                relative
                w-[56px] h-[56px]
                rounded-full
                backdrop-blur-[30px]
                bg-[var(--premium-glass-bg)]
                border-2 border-[var(--premium-glass-border)]
                flex items-center justify-center
              ">
                {/* Animated Glow when both selected */}
                {canContinue && (
                  <div className="
                    absolute inset-0
                    rounded-full
                    bg-[var(--premium-emerald)]
                    opacity-20
                    animate-pulse
                  " style={{ animationDuration: '2s' }} />
                )}

                <ArrowDown 
                  size={28} 
                  strokeWidth={2}
                  className={`
                    transition-all duration-300
                    ${canContinue
                      ? 'text-[var(--premium-emerald)]'
                      : 'text-[var(--premium-text-tertiary)]'
                    }
                  `}
                />
              </div>
            </div>

            {/* To Account Carousel */}
            <HorizontalAccountCarousel
              label="To Account"
              accounts={availableToAccounts}
              selectedAccountId={toAccountId}
              onSelectAccount={setToAccountId}
            />
          </div>

          {/* Helper Text */}
          {!fromAccountId && (
            <p className="
              text-center
              body-sm text-[var(--premium-text-muted)]
              mt-[var(--premium-space-2xl)]
            ">
              Select source account to begin transfer
            </p>
          )}

          {fromAccountId && !toAccountId && (
            <p className="
              text-center
              body-sm text-[var(--premium-text-muted)]
              mt-[var(--premium-space-2xl)]
            ">
              Now select destination account
            </p>
          )}

          {canContinue && (
            <div className="
              mt-[var(--premium-space-2xl)]
              p-[var(--premium-space-lg)]
              rounded-[var(--premium-radius-xl)]
              backdrop-blur-[30px]
              bg-[var(--premium-emerald)]/5
              border border-[var(--premium-emerald)]/20
              animate-[fadeIn_0.3s_ease-out]
            ">
              <p className="body-sm text-[var(--premium-text-secondary)] text-center">
                Transferring ${amount} from{' '}
                <span className="font-medium text-[var(--premium-text-primary)]">
                  {MOCK_ACCOUNTS.find(a => a.id === fromAccountId)?.name}
                </span>
                {' '}to{' '}
                <span className="font-medium text-[var(--premium-text-primary)]">
                  {MOCK_ACCOUNTS.find(a => a.id === toAccountId)?.name}
                </span>
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

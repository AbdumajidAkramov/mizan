/**
 * Premium Account Selector Component
 * For selecting From/To accounts in transfers
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

export interface PremiumAccountSelectorProps {
  label: string;
  selectedAccountId?: string;
  onSelectAccount: (accountId: string) => void;
  excludeAccountId?: string; // Don't show this account (e.g., From account in To selector)
}

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

export function PremiumAccountSelector({
  label,
  selectedAccountId,
  onSelectAccount,
  excludeAccountId,
}: PremiumAccountSelectorProps) {
  const availableAccounts = MOCK_ACCOUNTS.filter(
    account => account.id !== excludeAccountId
  );

  return (
    <div>
      <h3 className="
        body-md font-medium
        text-[var(--premium-text-secondary)]
        mb-[var(--premium-space-md)]
      ">
        {label}
      </h3>

      <div className="space-y-[var(--premium-space-sm)]">
        {availableAccounts.map((account) => {
          const Icon = account.icon;
          const isSelected = selectedAccountId === account.id;

          return (
            <button
              key={account.id}
              onClick={() => onSelectAccount(account.id)}
              className={`
                w-full
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-lg)]
                flex items-center gap-[var(--premium-space-md)]
                transition-all duration-200
                ${isSelected
                  ? 'bg-[var(--premium-surface-3)] ring-2 ring-[var(--premium-emerald)]'
                  : 'bg-[var(--premium-surface-2)] hover:bg-[var(--premium-surface-3)]'
                }
                active:scale-[0.98]
              `}
            >
              {/* Icon */}
              <div
                className="
                  w-[48px] h-[48px]
                  rounded-[var(--premium-radius-md)]
                  flex items-center justify-center
                  flex-shrink-0
                "
                style={{
                  background: `${account.color}20`,
                }}
              >
                <Icon size={24} strokeWidth={2} style={{ color: account.color }} />
              </div>

              {/* Account Info */}
              <div className="flex-1 text-left">
                <p className="body-md font-medium text-[var(--premium-text-primary)] mb-[2px]">
                  {account.name}
                </p>
                <p className="body-sm text-[var(--premium-text-tertiary)]">
                  ${account.balance.toLocaleString('en-US', {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
                </p>
              </div>

              {/* Selection Indicator */}
              {isSelected && (
                <div className="
                  w-[24px] h-[24px]
                  rounded-full
                  bg-[var(--premium-emerald)]
                  flex items-center justify-center
                  flex-shrink-0
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
            </button>
          );
        })}
      </div>
    </div>
  );
}

// Export accounts for use in voice parsing
export { MOCK_ACCOUNTS };

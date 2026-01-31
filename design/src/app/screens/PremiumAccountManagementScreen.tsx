/**
 * Premium Account Management Screen
 * High-fidelity account management with glassmorphism design
 * 
 * @features
 * - Total balance aggregation with emerald green glow
 * - Grouped accounts (Liquid Assets, Savings, Debts)
 * - UZS currency formatting (12 345.64 UZS)
 * - Swipe to hide/archive functionality
 * - Navigation to add/edit account screens
 * 
 * @architecture Material 3 with glassmorphism
 * @design Emerald Green (#10B981) accents, 8dp grid system
 */

import { useState } from 'react';
import { useSwipeable } from 'react-swipeable';
import {
  ChevronLeft,
  Plus,
  Wallet,
  Building2,
  Landmark,
  CreditCard,
  PiggyBank,
  ChevronRight,
  Archive,
  TrendingUp,
  TrendingDown,
  Search,
  GripVertical,
} from 'lucide-react';
import type { AccountType } from '../components/premium/PremiumAccountSelector';

/**
 * Financial Account interface
 */
export interface FinancialAccount {
  id: string;
  name: string;
  type: AccountType;
  balance: number; // In UZS
  currency: string;
  icon: typeof Wallet;
  color: string;
  isHidden?: boolean;
  bankLogo?: string; // Optional bank logo URL
  lastFourDigits?: string; // For card accounts
}

/**
 * Account group for categorization
 */
export interface AccountGroup {
  id: string;
  label: string;
  accounts: FinancialAccount[];
}

export interface PremiumAccountManagementScreenProps {
  /** Callback when back button is pressed */
  onBack: () => void;
  /** Callback when user wants to add a new account */
  onAddAccount?: () => void;
  /** Callback when user wants to edit an account */
  onEditAccount?: (accountId: string) => void;
  /** Callback when user archives an account */
  onArchiveAccount?: (accountId: string) => void;
}

// Mock data - Replace with real data from context/API
const MOCK_FINANCIAL_ACCOUNTS: FinancialAccount[] = [
  {
    id: 'humo-card',
    name: 'Humo Card',
    type: 'bank',
    balance: 12345678.50,
    currency: 'UZS',
    icon: Building2,
    color: '#667eea',
    lastFourDigits: '4532',
  },
  {
    id: 'cash-wallet',
    name: 'Cash Wallet',
    type: 'cash',
    balance: 450000.00,
    currency: 'UZS',
    icon: Wallet,
    color: '#10b981',
  },
  {
    id: 'uzcard',
    name: 'UzCard',
    type: 'bank',
    balance: 5234567.25,
    currency: 'UZS',
    icon: CreditCard,
    color: '#4facfe',
    lastFourDigits: '8821',
  },
  {
    id: 'savings-deposit',
    name: 'Savings Deposit',
    type: 'savings',
    balance: 25000000.00,
    currency: 'UZS',
    icon: PiggyBank,
    color: '#10b981',
  },
  {
    id: 'credit-card',
    name: 'Visa Credit',
    type: 'credit',
    balance: -2500000.00,
    currency: 'UZS',
    icon: CreditCard,
    color: '#f5576c',
    lastFourDigits: '9123',
  },
  {
    id: 'investment-fund',
    name: 'Investment Fund',
    type: 'investment',
    balance: 18750000.00,
    currency: 'UZS',
    icon: Landmark,
    color: '#c471f5',
  },
];

/**
 * Format number in UZS style: "12 345.64 UZS"
 * Decimal and currency in smaller font
 */
function formatUZS(amount: number): {
  integer: string;
  decimal: string;
  currency: string;
} {
  const absAmount = Math.abs(amount);
  const integerPart = Math.floor(absAmount);
  const decimalPart = (absAmount % 1).toFixed(2).substring(2);

  // Format integer with spaces as thousand separator
  const formattedInteger = integerPart.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ');

  return {
    integer: formattedInteger,
    decimal: decimalPart,
    currency: 'UZS',
  };
}

/**
 * Group accounts by type
 */
function groupAccounts(accounts: FinancialAccount[]): AccountGroup[] {
  const liquidAssets = accounts.filter(
    (acc) => acc.type === 'cash' || acc.type === 'bank'
  );
  const savings = accounts.filter((acc) => acc.type === 'savings' || acc.type === 'investment');
  const debts = accounts.filter((acc) => acc.type === 'credit' && acc.balance < 0);

  return [
    {
      id: 'liquid',
      label: 'Liquid Assets',
      accounts: liquidAssets,
    },
    {
      id: 'savings',
      label: 'Savings',
      accounts: savings,
    },
    {
      id: 'debts',
      label: 'Debts',
      accounts: debts,
    },
  ].filter((group) => group.accounts.length > 0);
}

/**
 * Calculate total balance across all accounts
 */
function calculateTotalBalance(accounts: FinancialAccount[]): number {
  return accounts.reduce((total, account) => total + account.balance, 0);
}

/**
 * Account Row Component with Swipe Functionality
 */
function AccountRow({
  account,
  onTap,
  onArchive,
}: {
  account: FinancialAccount;
  onTap: () => void;
  onArchive: () => void;
}) {
  const [isSwiping, setIsSwiping] = useState(false);
  const [swipeOffset, setSwipeOffset] = useState(0);

  const handlers = useSwipeable({
    onSwiping: (eventData) => {
      if (eventData.dir === 'Left') {
        const offset = Math.max(-80, Math.min(0, eventData.deltaX));
        setSwipeOffset(offset);
        setIsSwiping(true);
      }
    },
    onSwiped: () => {
      if (swipeOffset < -40) {
        // Trigger archive
        onArchive();
      }
      setSwipeOffset(0);
      setIsSwiping(false);
    },
    trackMouse: true,
    delta: 10,
  });

  const Icon = account.icon;
  const { integer, decimal, currency } = formatUZS(account.balance);
  const isNegative = account.balance < 0;

  return (
    <div className="relative overflow-hidden">
      {/* Archive Background */}
      <div
        className="
          absolute inset-0
          bg-[#f5576c]
          flex items-center justify-end
          px-[var(--premium-space-lg)]
          rounded-[var(--premium-radius-xl)]
        "
      >
        <Archive size={24} className="text-white" />
      </div>

      {/* Account Card */}
      <div
        {...handlers}
        style={{
          transform: `translateX(${swipeOffset}px)`,
          transition: isSwiping ? 'none' : 'transform 0.3s ease-out',
        }}
        onClick={() => !isSwiping && onTap()}
        className="
          relative
          p-[var(--premium-space-md)]
          rounded-[var(--premium-radius-xl)]
          bg-[var(--premium-surface-2)]
          border border-[var(--premium-glass-border)]
          flex items-center gap-[var(--premium-space-md)]
          transition-all duration-200
          hover:bg-[var(--premium-surface-3)]
          active:scale-[0.98]
          cursor-pointer
        "
      >
        {/* Account Icon */}
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
        <div className="flex-1 min-w-0">
          <p className="body-md font-medium text-[var(--premium-text-primary)] mb-[2px] truncate">
            {account.name}
          </p>
          <p className="body-sm text-[var(--premium-text-tertiary)]">
            {account.lastFourDigits && `•••• ${account.lastFourDigits}`}
          </p>
        </div>

        {/* Balance */}
        <div className="flex flex-col items-end flex-shrink-0">
          <div className="flex items-baseline gap-[4px]">
            <span
              className={`
                heading-sm
                ${isNegative ? 'text-[#f5576c]' : 'text-[var(--premium-text-primary)]'}
              `}
            >
              {isNegative && '-'}{integer}
            </span>
            <span className="body-xs text-[var(--premium-text-tertiary)]">.{decimal}</span>
          </div>
          <p className="body-xs text-[var(--premium-text-tertiary)] mt-[2px]">{currency}</p>
        </div>

        {/* Chevron */}
        <ChevronRight size={20} className="text-[var(--premium-text-tertiary)] flex-shrink-0" />
      </div>
    </div>
  );
}

/**
 * Premium Account Management Screen Component
 */
export function PremiumAccountManagementScreen({
  onBack,
  onAddAccount,
  onEditAccount,
  onArchiveAccount,
}: PremiumAccountManagementScreenProps) {
  const [accounts] = useState<FinancialAccount[]>(MOCK_FINANCIAL_ACCOUNTS);
  const [searchQuery, setSearchQuery] = useState('');

  // Filter accounts based on search query
  const filteredAccounts = accounts.filter((account) =>
    account.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const totalBalance = calculateTotalBalance(filteredAccounts);
  const accountGroups = groupAccounts(filteredAccounts);
  const { integer, decimal, currency } = formatUZS(totalBalance);

  // Calculate balance change (mock data)
  const balanceChange = 2450000; // +2,450,000 UZS this month
  const balanceChangePercent = 12.5;

  return (
    <div className="min-h-screen bg-[var(--premium-bg-primary)] flex flex-col">
      {/* Header */}
      <div
        className="
          flex-shrink-0
          bg-[var(--premium-surface)]
          border-b border-[var(--premium-glass-border)]
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-md)]
        "
      >
        <div className="flex items-center gap-[var(--premium-space-md)]">
          <button
            onClick={onBack}
            className="
              w-[40px] h-[40px]
              rounded-[var(--premium-radius-full)]
              flex items-center justify-center
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              transition-all duration-200
              active:scale-95
            "
          >
            <ChevronLeft size={24} className="text-[var(--premium-text-primary)]" />
          </button>

          <div className="flex-1">
            <h1 className="heading-lg text-[var(--premium-text-primary)]">Accounts</h1>
            <p className="body-sm text-[var(--premium-text-tertiary)]">
              Manage your financial accounts
            </p>
          </div>
        </div>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-xl)]">
        <div className="space-y-[var(--premium-space-xl)]">
          {/* Search Bar */}
          <div
            className="
              relative
              flex items-center gap-[12px]
              px-[var(--premium-space-md)]
              py-[12px]
              rounded-[var(--premium-radius-xl)]
              bg-[var(--premium-surface-2)]
              border border-[var(--premium-glass-border)]
              transition-all duration-200
              focus-within:border-[var(--premium-emerald)]/50
              focus-within:shadow-[0_0_0_4px_rgba(16,185,129,0.1)]
            "
          >
            <Search size={20} className="text-[var(--premium-text-tertiary)] flex-shrink-0" />
            <input
              type="text"
              placeholder="Search accounts..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="
                flex-1
                bg-transparent
                body-md text-[var(--premium-text-primary)]
                outline-none
                placeholder:text-[var(--premium-text-muted)]
              "
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="
                  w-[24px] h-[24px]
                  rounded-full
                  bg-[var(--premium-surface-3)]
                  flex items-center justify-center
                  hover:bg-[var(--premium-surface-4)]
                  transition-all duration-200
                  active:scale-95
                "
              >
                <span className="text-[12px] text-[var(--premium-text-tertiary)]">✕</span>
              </button>
            )}
          </div>

          {/* Total Balance Card */}
          <div
            className="
              relative
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-2xl)]
              bg-gradient-to-br from-[var(--premium-surface-2)] to-[var(--premium-surface-3)]
              border border-[var(--premium-emerald)]/20
              shadow-[0_8px_32px_rgba(16,185,129,0.15)]
              overflow-hidden
            "
          >
            {/* Emerald Glow Effect */}
            <div
              className="
                absolute top-0 right-0
                w-[200px] h-[200px]
                bg-[var(--premium-emerald)]
                opacity-10
                blur-[80px]
                rounded-full
              "
            />

            <div className="relative z-10">
              <p className="body-sm text-[var(--premium-text-tertiary)] mb-[8px]">
                Total Balance
              </p>

              {/* Main Balance */}
              <div className="flex items-baseline gap-[6px] mb-[var(--premium-space-md)]">
                <span className="text-[32px] font-bold text-[var(--premium-text-primary)] leading-none">
                  {integer}
                </span>
                <span className="text-[18px] text-[var(--premium-text-tertiary)]">.{decimal}</span>
                <span className="text-[14px] text-[var(--premium-text-tertiary)] ml-[4px]">
                  {currency}
                </span>
              </div>

              {/* Balance Change Indicator */}
              <div className="flex items-center gap-[8px]">
                <div
                  className="
                    flex items-center gap-[4px]
                    px-[12px] py-[6px]
                    rounded-[var(--premium-radius-full)]
                    bg-[var(--premium-emerald)]/10
                    border border-[var(--premium-emerald)]/20
                  "
                >
                  <TrendingUp size={14} className="text-[var(--premium-emerald)]" />
                  <span className="body-sm font-medium text-[var(--premium-emerald)]">
                    {formatUZS(balanceChange).integer} {currency}
                  </span>
                </div>
                <p className="body-xs text-[var(--premium-text-tertiary)]">
                  +{balanceChangePercent}% this month
                </p>
              </div>
            </div>
          </div>

          {/* Account Groups */}
          {accountGroups.map((group) => (
            <div key={group.id} className="space-y-[var(--premium-space-md)]">
              {/* Group Header */}
              <div className="flex items-center justify-between">
                <h2 className="heading-sm text-[var(--premium-text-secondary)]">
                  {group.label}
                </h2>
                <p className="body-sm text-[var(--premium-text-tertiary)]">
                  {group.accounts.length} {group.accounts.length === 1 ? 'account' : 'accounts'}
                </p>
              </div>

              {/* Account List */}
              <div className="space-y-[8px]">
                {group.accounts.map((account) => (
                  <AccountRow
                    key={account.id}
                    account={account}
                    onTap={() => onEditAccount?.(account.id)}
                    onArchive={() => onArchiveAccount?.(account.id)}
                  />
                ))}
              </div>
            </div>
          ))}

          {/* Add New Account Button */}
          {onAddAccount && (
            <button
              onClick={onAddAccount}
              className="
                w-full
                p-[var(--premium-space-md)]
                rounded-[var(--premium-radius-xl)]
                flex items-center justify-center gap-[var(--premium-space-md)]
                transition-all duration-200
                bg-[var(--premium-surface-2)]/40
                border-2 border-dashed border-[var(--premium-emerald)]/30
                hover:bg-[var(--premium-emerald)]/5
                hover:border-[var(--premium-emerald)]
                active:scale-[0.98]
              "
            >
              {/* Plus Icon */}
              <div
                className="
                  w-[32px] h-[32px]
                  rounded-full
                  bg-[var(--premium-emerald)]/10
                  border border-[var(--premium-emerald)]/30
                  flex items-center justify-center
                  flex-shrink-0
                "
              >
                <Plus size={18} className="text-[var(--premium-emerald)]" strokeWidth={2.5} />
              </div>

              {/* Text */}
              <div className="text-left">
                <p className="body-md font-medium text-[var(--premium-emerald)] mb-[2px]">
                  Add New Account
                </p>
                <p className="body-sm text-[var(--premium-text-tertiary)]">
                  Cash, Bank, Card, or Savings
                </p>
              </div>
            </button>
          )}

          {/* Bottom Spacing */}
          <div className="h-[80px]" />
        </div>
      </div>

      {/* Floating Action Button (Alternative to inline button) */}
      {false && onAddAccount && (
        <button
          onClick={onAddAccount}
          className="
            fixed
            bottom-[24px]
            right-[24px]
            w-[56px] h-[56px]
            rounded-full
            bg-[var(--premium-emerald)]
            hover:bg-[var(--premium-emerald-dark)]
            shadow-[0_8px_24px_rgba(16,185,129,0.4)]
            flex items-center justify-center
            transition-all duration-200
            active:scale-95
            z-50
          "
        >
          <Plus size={24} className="text-white" strokeWidth={2.5} />
        </button>
      )}
    </div>
  );
}
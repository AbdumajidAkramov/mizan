/**
 * Premium Account Management Screen
 * Redesigned with Mizan's glassmorphism design system
 * 
 * @features
 * - Premium glassmorphism total balance card
 * - Categorized account groups with glassmorphism items
 * - Search functionality
 * - Monthly change indicator
 * - Emerald green accents throughout
 * 
 * @architecture Material 3 with glassmorphism
 * @design Deep dark blue/purple (#1A1A2E) background, 8dp grid system
 */

import { useState } from 'react';
import {
  ChevronLeft,
  Plus,
  Wallet,
  Building2,
  Landmark,
  CreditCard,
  PiggyBank,
  ChevronRight,
  Search,
  TrendingUp,
  Edit2,
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
  bankLogo?: string;
  lastFourDigits?: string;
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

// Mock data
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
 * Format number in UZS style: "12 345 678.99 UZS"
 */
function formatUZS(amount: number): {
  integer: string;
  decimal: string;
  currency: string;
} {
  const absAmount = Math.abs(amount);
  const integerPart = Math.floor(absAmount);
  const decimalPart = (absAmount % 1).toFixed(2).substring(2);

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

  // Mock monthly change data
  const monthlyChange = 2450000;
  const monthlyChangePercent = 12.5;

  return (
    <div className="h-screen bg-[#1A1A2E] flex flex-col overflow-hidden">
      {/* Header */}
      <div
        className="
          flex-shrink-0
          px-[var(--premium-space-lg)]
          pt-[var(--premium-space-xl)]
          pb-[var(--premium-space-md)]
        "
      >
        <div className="flex items-center justify-between mb-[var(--premium-space-lg)]">
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
                Accounts
              </h1>
              <p className="body-sm text-white/60">
                Manage your financial accounts
              </p>
            </div>
          </div>

          {/* Add Account FAB */}
          <button
            onClick={onAddAccount}
            className="
              w-[48px] h-[48px]
              rounded-full
              bg-[var(--premium-emerald)]
              hover:bg-[var(--premium-emerald-dark)]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
              shadow-[0_4px_16px_rgba(16,185,129,0.3)]
            "
          >
            <Plus size={24} className="text-white" strokeWidth={2.5} />
          </button>
        </div>

        {/* Search Bar */}
        <div
          className="
            relative
            flex items-center gap-[12px]
            px-[var(--premium-space-md)]
            py-[12px]
            rounded-[var(--premium-radius-xl)]
            bg-white/5
            backdrop-blur-xl
            border border-white/10
            transition-all duration-200
            focus-within:border-[var(--premium-emerald)]/50
            focus-within:shadow-[0_0_0_4px_rgba(16,185,129,0.1)]
          "
        >
          <Search size={20} className="text-white/40 flex-shrink-0" />
          <input
            type="text"
            placeholder="Search accounts..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="
              flex-1
              bg-transparent
              body-md text-white
              outline-none
              placeholder:text-white/40
            "
          />
          {searchQuery && (
            <button
              onClick={() => setSearchQuery('')}
              className="
                w-[24px] h-[24px]
                rounded-full
                bg-white/10
                flex items-center justify-center
                hover:bg-white/20
                transition-all duration-200
                active:scale-95
              "
            >
              <span className="text-[12px] text-white/60">✕</span>
            </button>
          )}
        </div>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pb-[var(--premium-space-xl)]">
        <div className="space-y-[var(--premium-space-xl)]">
          {/* Total Balance Card - Premium Glassmorphism */}
          <div
            className="
              relative
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-2xl)]
              bg-gradient-to-br from-white/10 to-white/5
              backdrop-blur-2xl
              border border-white/20
              overflow-hidden
            "
          >
            {/* Emerald Glow Effect */}
            <div
              className="
                absolute -top-[50%] -right-[20%]
                w-[200px] h-[200px]
                rounded-full
                bg-[var(--premium-emerald)]
                opacity-20
                blur-[80px]
              "
            />

            <div className="relative z-10">
              <p className="body-sm text-white/60 mb-[8px]">
                Total Balance
              </p>
              
              {/* Balance Display */}
              <div className="flex items-baseline gap-[4px] mb-[var(--premium-space-md)]">
                <span className="text-[32px] font-bold text-white tracking-tight">
                  {integer}
                </span>
                <span className="text-[20px] font-semibold text-white/70">
                  .{decimal}
                </span>
                <span className="text-[16px] font-medium text-white/50 ml-[4px]">
                  {currency}
                </span>
              </div>

              {/* Monthly Change Chip */}
              <div
                className="
                  inline-flex items-center gap-[6px]
                  px-[12px] py-[6px]
                  rounded-full
                  bg-[var(--premium-emerald)]/20
                  border border-[var(--premium-emerald)]/30
                "
              >
                <TrendingUp size={14} className="text-[var(--premium-emerald)]" />
                <span className="body-xs font-semibold text-[var(--premium-emerald)]">
                  +{formatUZS(monthlyChange).integer} {formatUZS(monthlyChange).currency}
                </span>
                <span className="body-xs text-white/60">
                  • +{monthlyChangePercent}% this month
                </span>
              </div>
            </div>
          </div>

          {/* Account Groups */}
          {accountGroups.map((group) => (
            <div key={group.id} className="space-y-[var(--premium-space-md)]">
              {/* Group Header */}
              <div className="flex items-center justify-between px-[4px]">
                <h3 className="heading-sm text-white/90 font-semibold">
                  {group.label}
                </h3>
                <span className="body-xs text-white/40">
                  {group.accounts.length} {group.accounts.length === 1 ? 'account' : 'accounts'}
                </span>
              </div>

              {/* Account Items */}
              <div className="space-y-[8px]">
                {group.accounts.map((account) => {
                  const Icon = account.icon;
                  const { integer, decimal, currency } = formatUZS(account.balance);
                  const isNegative = account.balance < 0;

                  return (
                    <button
                      key={account.id}
                      onClick={() => onEditAccount?.(account.id)}
                      className="
                        w-full
                        p-[var(--premium-space-md)]
                        rounded-[var(--premium-radius-xl)]
                        bg-white/5
                        backdrop-blur-xl
                        border border-white/10
                        hover:bg-white/10
                        hover:border-white/20
                        transition-all duration-200
                        active:scale-[0.98]
                        flex items-center gap-[var(--premium-space-md)]
                      "
                    >
                      {/* Account Icon */}
                      <div
                        className="
                          w-[48px] h-[48px]
                          rounded-[var(--premium-radius-lg)]
                          flex items-center justify-center
                          flex-shrink-0
                        "
                        style={{ 
                          backgroundColor: `${account.color}20`,
                          border: `1px solid ${account.color}30`
                        }}
                      >
                        <Icon size={24} style={{ color: account.color }} />
                      </div>

                      {/* Account Details */}
                      <div className="flex-1 min-w-0 text-left">
                        <p className="body-md font-medium text-white truncate">
                          {account.name}
                        </p>
                        {account.lastFourDigits && (
                          <p className="body-xs text-white/40">
                            •••• {account.lastFourDigits}
                          </p>
                        )}
                      </div>

                      {/* Balance */}
                      <div className="flex flex-col items-end flex-shrink-0">
                        <div className="flex items-baseline gap-[2px]">
                          {isNegative && <span className="text-[14px] font-semibold text-[#f5576c]">-</span>}
                          <span className={`text-[16px] font-bold ${isNegative ? 'text-[#f5576c]' : 'text-white'}`}>
                            {integer}
                          </span>
                          <span className={`text-[12px] font-medium ${isNegative ? 'text-[#f5576c]/70' : 'text-white/60'}`}>
                            .{decimal}
                          </span>
                        </div>
                        <p className="body-xs text-white/40">
                          {currency}
                        </p>
                      </div>

                      {/* Edit Icon */}
                      <ChevronRight size={20} className="text-white/30 flex-shrink-0" />
                    </button>
                  );
                })}
              </div>
            </div>
          ))}

          {/* Add New Account Button - Inline Dashed Style */}
          <button
            onClick={onAddAccount}
            className="
              w-full
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-xl)]
              bg-transparent
              border-2 border-dashed border-white/20
              hover:border-[var(--premium-emerald)]/50
              hover:bg-[var(--premium-emerald)]/5
              transition-all duration-200
              active:scale-[0.98]
              flex items-center gap-[var(--premium-space-md)]
            "
          >
            {/* Plus Icon */}
            <div
              className="
                w-[48px] h-[48px]
                rounded-full
                bg-[var(--premium-emerald)]/20
                border border-[var(--premium-emerald)]/30
                flex items-center justify-center
                flex-shrink-0
              "
            >
              <Plus size={24} className="text-[var(--premium-emerald)]" strokeWidth={2.5} />
            </div>

            {/* Text */}
            <div className="flex-1 text-left">
              <p className="body-md font-medium text-white">
                Add New Account
              </p>
              <p className="body-xs text-white/40">
                Cash, Bank, Card, or Savings
              </p>
            </div>
          </button>

          {/* Empty State */}
          {filteredAccounts.length === 0 && (
            <div className="text-center py-[var(--premium-space-2xl)]">
              <Wallet size={48} className="text-white/20 mx-auto mb-[var(--premium-space-md)]" />
              <p className="body-md text-white/60">
                No accounts found
              </p>
              <p className="body-sm text-white/40 mt-[4px]">
                Try adjusting your search
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

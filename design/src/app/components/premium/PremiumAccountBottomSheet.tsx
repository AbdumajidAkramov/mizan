/**
 * Premium Account BottomSheet Component
 * 
 * Material 3 Modal BottomSheet for account selection
 * Features hierarchical account groups with glassmorphism design
 * 
 * @architecture Reusable Component with callback-based state management
 * @design Material 3 with 8dp grid system and glassmorphism
 */

import { useEffect } from 'react';
import { X } from 'lucide-react';

// Account Interface
interface Account {
  id: string;
  name: string;
  type: string;
  balance: number;
  icon: any;
  color: string;
  group: string;
  groupIcon: any;
}

export interface PremiumAccountBottomSheetProps {
  /** Whether the bottom sheet is visible */
  isOpen: boolean;
  /** Callback when bottom sheet should close */
  onClose: () => void;
  /** Available accounts to select from */
  accounts: Account[];
  /** Currently selected account ID */
  selectedAccountId?: string;
  /** Callback when an account is selected */
  onSelectAccount: (accountId: string) => void;
  /** Optional title override */
  title?: string;
  /** Optional accounts to exclude (for transfers) */
  excludeAccountIds?: string[];
}

/**
 * PremiumAccountBottomSheet Component
 */
export function PremiumAccountBottomSheet({
  isOpen,
  onClose,
  accounts,
  selectedAccountId,
  onSelectAccount,
  title = 'Select Account',
  excludeAccountIds = [],
}: PremiumAccountBottomSheetProps) {
  // Prevent body scroll when bottom sheet is open
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => {
      document.body.style.overflow = '';
    };
  }, [isOpen]);

  if (!isOpen) return null;

  // Filter out excluded accounts
  const filteredAccounts = accounts.filter(
    (acc) => !excludeAccountIds.includes(acc.id)
  );

  // Group accounts by their group property
  const groupAccountsByType = (accs: Account[]): Record<string, Account[]> => {
    return accs.reduce((grouped, account) => {
      const group = account.group;
      if (!grouped[group]) {
        grouped[group] = [];
      }
      grouped[group].push(account);
      return grouped;
    }, {} as Record<string, Account[]>);
  };

  const grouped = groupAccountsByType(filteredAccounts);

  const handleSelectAccount = (accountId: string) => {
    onSelectAccount(accountId);
    // Auto-close with a slight delay for selection animation
    setTimeout(() => {
      onClose();
    }, 200);
  };

  return (
    <>
      {/* Backdrop with blur */}
      <div
        className="
          fixed inset-0 z-[150]
          bg-black/40
          backdrop-blur-md
          animate-[fadeIn_0.2s_ease-out]
        "
        onClick={onClose}
      />

      {/* Bottom Sheet */}
      <div
        className="
          fixed bottom-0 left-0 right-0 z-[151]
          max-h-[85vh]
          bg-[var(--premium-bg-primary)]
          rounded-t-[var(--premium-radius-2xl)]
          border-t border-[var(--premium-glass-border)]
          shadow-[0_-8px_32px_rgba(0,0,0,0.3)]
          animate-[slideUpBottomSheet_0.3s_ease-out]
          overflow-hidden
          flex flex-col
        "
        onClick={(e) => e.stopPropagation()}
      >
        {/* Drag Handle */}
        <div className="flex justify-center pt-[12px] pb-[8px]">
          <div
            className="
              w-[40px] h-[4px]
              rounded-full
              bg-[var(--premium-text-muted)]
              opacity-40
            "
          />
        </div>

        {/* Header */}
        <div
          className="
            px-[var(--premium-space-lg)]
            pt-[var(--premium-space-sm)]
            pb-[var(--premium-space-md)]
            border-b border-[var(--premium-glass-border)]
            flex items-center justify-between
          "
        >
          <h2 className="heading-5 text-[var(--premium-text-primary)]">
            {title}
          </h2>
          <button
            onClick={onClose}
            className="
              w-[32px] h-[32px]
              rounded-full
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              flex items-center justify-center
              text-[var(--premium-text-secondary)]
              transition-all duration-200
              active:scale-95
            "
            aria-label="Close"
          >
            <X size={18} />
          </button>
        </div>

        {/* Account List - Scrollable */}
        <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] py-[var(--premium-space-lg)]">
          <div className="space-y-[var(--premium-space-xl)]">
            {Object.keys(grouped).map((groupName) => {
              const groupAccounts = grouped[groupName];
              const GroupIcon = groupAccounts[0].groupIcon;

              return (
                <div key={groupName}>
                  {/* Group Header */}
                  <div className="flex items-center gap-[8px] mb-[var(--premium-space-md)] px-[4px]">
                    <GroupIcon
                      size={14}
                      className="text-[var(--premium-text-tertiary)]"
                    />
                    <p className="body-xs text-[var(--premium-text-tertiary)] uppercase tracking-wide font-medium">
                      {groupName}
                    </p>
                  </div>

                  {/* Account Rows */}
                  <div className="space-y-[var(--premium-space-sm)]">
                    {groupAccounts.map((account) => {
                      const Icon = account.icon;
                      const isSelected = selectedAccountId === account.id;

                      return (
                        <button
                          key={account.id}
                          onClick={() => handleSelectAccount(account.id)}
                          className={`
                            w-full
                            p-[var(--premium-space-md)]
                            rounded-[var(--premium-radius-xl)]
                            flex items-center gap-[var(--premium-space-md)]
                            transition-all duration-200
                            ${
                              isSelected
                                ? 'bg-[var(--premium-emerald)]/15 border-2 border-[var(--premium-emerald)] shadow-[0_0_0_4px_rgba(16,185,129,0.1)] scale-[0.98]'
                                : 'bg-[var(--premium-surface-2)] border-2 border-transparent hover:bg-[var(--premium-surface-3)] active:scale-[0.98]'
                            }
                          `}
                        >
                          {/* Account Icon */}
                          <div
                            className={`
                              w-[48px] h-[48px]
                              rounded-[var(--premium-radius-lg)]
                              flex items-center justify-center
                              flex-shrink-0
                              transition-all duration-200
                            `}
                            style={{
                              backgroundColor: isSelected
                                ? account.color
                                : `${account.color}20`,
                            }}
                          >
                            <Icon
                              size={24}
                              style={{
                                color: isSelected ? 'white' : account.color,
                              }}
                            />
                          </div>

                          {/* Account Info */}
                          <div className="flex-1 text-left min-w-0">
                            <p
                              className={`
                                body-md font-medium mb-[2px] truncate
                                ${
                                  isSelected
                                    ? 'text-[var(--premium-emerald)]'
                                    : 'text-[var(--premium-text-primary)]'
                                }
                              `}
                            >
                              {account.name}
                            </p>
                            <p
                              className={`
                                body-sm truncate
                                ${
                                  account.balance < 0
                                    ? 'text-[#f5576c]'
                                    : 'text-[var(--premium-text-secondary)]'
                                }
                              `}
                            >
                              ${Math.abs(account.balance).toLocaleString('en-US', {
                                minimumFractionDigits: 2,
                                maximumFractionDigits: 2,
                              })}
                            </p>
                          </div>

                          {/* Selection Indicator */}
                          {isSelected && (
                            <div
                              className="
                                w-[24px] h-[24px]
                                rounded-full
                                bg-[var(--premium-emerald)]
                                flex items-center justify-center
                                animate-[scaleIn_0.2s_ease-out]
                              "
                            >
                              <svg
                                width="14"
                                height="14"
                                viewBox="0 0 14 14"
                                fill="none"
                                xmlns="http://www.w3.org/2000/svg"
                              >
                                <path
                                  d="M11.6667 3.5L5.25 9.91667L2.33333 7"
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
            })}
          </div>
        </div>
      </div>
    </>
  );
}

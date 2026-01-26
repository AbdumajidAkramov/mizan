/**
 * BottomNavigationBar Organism Component
 * Material 3 bottom navigation with FAB
 */

import { Home, BarChart3, Receipt, User, Plus } from 'lucide-react';

export type NavigationTab = 'home' | 'statistics' | 'transactions' | 'profile';

export interface BottomNavigationBarProps {
  /** Currently active tab */
  activeTab: NavigationTab;
  /** Callback when tab changes */
  onTabChange: (tab: NavigationTab) => void;
  /** Callback when add button is clicked */
  onAddExpense: () => void;
}

interface NavigationItem {
  id: NavigationTab | 'add';
  icon: typeof Home;
  label: string;
  isSpecial?: boolean;
}

const navigationItems: NavigationItem[] = [
  { id: 'home', icon: Home, label: 'Home' },
  { id: 'statistics', icon: BarChart3, label: 'Stats' },
  { id: 'add', icon: Plus, label: 'Add', isSpecial: true },
  { id: 'transactions', icon: Receipt, label: 'History' },
  { id: 'profile', icon: User, label: 'Profile' },
];

/**
 * Material 3 Bottom Navigation Bar
 * Maps to Android NavigationBar with FAB
 */
export function BottomNavigationBar({
  activeTab,
  onTabChange,
  onAddExpense,
}: BottomNavigationBarProps) {
  return (
    <div className="fixed bottom-0 left-0 right-0 bg-[var(--color-surface)] border-t border-[var(--color-outline-variant)] z-40 shadow-[var(--elevation-3)]">
      <div className="max-w-lg mx-auto px-[var(--spacing-md)] py-[var(--spacing-sm)]">
        <div className="flex items-center justify-around">
          {navigationItems.map((item) => {
            const Icon = item.icon;
            const isActive = activeTab === item.id;

            // Special FAB button
            if (item.isSpecial) {
              return (
                <button
                  key={item.id}
                  onClick={onAddExpense}
                  aria-label={item.label}
                  className="
                    flex items-center justify-center
                    w-[56px] h-[56px]
                    rounded-full
                    bg-[var(--color-primary-container)]
                    text-[var(--color-on-primary-container)]
                    shadow-[var(--elevation-3)]
                    hover:shadow-[var(--elevation-4)]
                    active:scale-95
                    transition-all duration-200
                    -mt-[var(--spacing-lg)]
                  "
                >
                  <Icon size={24} />
                </button>
              );
            }

            // Regular navigation items
            return (
              <button
                key={item.id}
                onClick={() => onTabChange(item.id as NavigationTab)}
                aria-label={item.label}
                className={`
                  flex flex-col items-center gap-[var(--spacing-xs)]
                  py-[var(--spacing-sm)]
                  px-[var(--spacing-sm)]
                  rounded-xl
                  transition-all duration-200
                  ${isActive
                    ? 'text-[var(--color-on-secondary-container)] bg-[var(--color-secondary-container)]'
                    : 'text-[var(--color-on-surface-variant)] hover:text-[var(--color-on-surface)]'
                  }
                `}
              >
                <Icon
                  size={24}
                  className={`${isActive ? 'scale-110' : ''} transition-transform`}
                />
                <span className="text-xs">{item.label}</span>
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}

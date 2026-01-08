/**
 * M3 BottomNavigationBar Organism Component
 * Material Design 3 NavigationBar with FloatingActionButton
 * Maps 1:1 to Jetpack Compose NavigationBar + ExtendedFloatingActionButton
 */

import { Home, BarChart3, Receipt, User, Plus } from 'lucide-react';
import { M3Surface } from '../atoms/M3Surface';

export type M3NavigationTab = 'home' | 'statistics' | 'transactions' | 'profile';

export interface M3BottomNavigationBarProps {
  /** Currently active tab */
  activeTab: M3NavigationTab;
  /** Callback when tab changes */
  onTabChange: (tab: M3NavigationTab) => void;
  /** Callback when add button is clicked */
  onAddExpense: () => void;
}

interface NavigationItem {
  id: M3NavigationTab | 'add';
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
 * Uses M3 surface container and state layers
 * 
 * @example
 * <M3BottomNavigationBar
 *   activeTab="home"
 *   onTabChange={setActiveTab}
 *   onAddExpense={handleAddExpense}
 * />
 */
export function M3BottomNavigationBar({
  activeTab,
  onTabChange,
  onAddExpense,
}: M3BottomNavigationBarProps) {
  return (
    <M3Surface
      elevation={2}
      shape="none"
      className="fixed bottom-0 left-0 right-0 z-40"
    >
      <div className="max-w-lg mx-auto px-[var(--md-sys-spacing-md)] py-[var(--md-sys-spacing-sm)]">
        <div className="flex items-center justify-around">
          {navigationItems.map((item) => {
            const Icon = item.icon;
            const isActive = activeTab === item.id;

            // M3 FAB (Floating Action Button)
            if (item.isSpecial) {
              return (
                <button
                  key={item.id}
                  onClick={onAddExpense}
                  aria-label={item.label}
                  className="
                    flex items-center justify-center
                    w-[56px] h-[56px]
                    rounded-[var(--md-sys-shape-corner-large)]
                    bg-[var(--md-sys-color-primary-container)]
                    text-[var(--md-sys-color-on-primary-container)]
                    hover:bg-[var(--md-sys-color-primary-container)]/90
                    active:scale-95
                    transition-all duration-200
                    -mt-[var(--md-sys-spacing-lg)]
                  "
                >
                  <Icon size={24} />
                </button>
              );
            }

            // M3 NavigationBar Items
            return (
              <button
                key={item.id}
                onClick={() => onTabChange(item.id as M3NavigationTab)}
                aria-label={item.label}
                className={`
                  flex flex-col items-center gap-[var(--md-sys-spacing-xs)]
                  py-[var(--md-sys-spacing-sm)]
                  px-[var(--md-sys-spacing-md)]
                  rounded-[var(--md-sys-shape-corner-medium)]
                  transition-all duration-200
                  ${isActive
                    ? 'text-[var(--md-sys-color-on-secondary-container)] bg-[var(--md-sys-color-secondary-container)]'
                    : 'text-[var(--md-sys-color-on-surface-variant)] hover:bg-[var(--md-sys-color-on-surface)]/[0.08]'
                  }
                `}
              >
                <Icon
                  size={24}
                  className={`${isActive ? 'scale-110' : ''} transition-transform`}
                />
                <span className="label-medium">{item.label}</span>
              </button>
            );
          })}
        </div>
      </div>
    </M3Surface>
  );
}

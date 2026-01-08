/**
 * Premium Bottom Navigation
 * Glassmorphism nav with gradient FAB
 */

import { Home, TrendingUp, Plus, Sparkles, User } from 'lucide-react';

export type PremiumNavTab = 'home' | 'statistics' | 'mirror' | 'profile' | 'add';

export interface PremiumBottomNavProps {
  activeTab: PremiumNavTab;
  onTabChange: (tab: PremiumNavTab) => void;
  onAddExpense: () => void;
}

interface NavItem {
  id: PremiumNavTab | 'add';
  icon: typeof Home;
  label: string;
  isSpecial?: boolean;
}

const navItems: NavItem[] = [
  { id: 'home', icon: Home, label: 'Home' },
  { id: 'mirror', icon: Sparkles, label: 'Mirror' },
  { id: 'add', icon: Plus, label: 'Add', isSpecial: true },
  { id: 'statistics', icon: TrendingUp, label: 'Stats' },
  { id: 'profile', icon: User, label: 'Profile' },
];

export function PremiumBottomNav({
  activeTab,
  onTabChange,
  onAddExpense,
}: PremiumBottomNavProps) {
  return (
    <div className="
      fixed bottom-0 left-0 right-0 z-50
      pb-[var(--premium-space-md)]
    ">
      <div className="max-w-lg mx-auto px-[var(--premium-space-md)]">
        <div className="
          bg-[var(--premium-glass-bg)]
          backdrop-blur-[30px]
          border border-[var(--premium-glass-border)]
          rounded-[var(--premium-radius-2xl)]
          px-[var(--premium-space-lg)]
          py-[var(--premium-space-md)]
          shadow-[var(--premium-shadow-xl)]
        ">
          <div className="flex items-center justify-around">
            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = activeTab === item.id;

              // Premium FAB
              if (item.isSpecial) {
                return (
                  <button
                    key={item.id}
                    onClick={onAddExpense}
                    aria-label={item.label}
                    className="
                      relative
                      flex items-center justify-center
                      w-[60px] h-[60px]
                      rounded-full
                      bg-gradient-to-r from-[#667eea] to-[#764ba2]
                      text-white
                      shadow-[var(--premium-glow-primary)]
                      hover:shadow-[0_0_30px_rgba(102,126,234,0.7)]
                      active:scale-95
                      transition-all duration-200
                      -mt-[24px]
                    "
                  >
                    <Icon size={28} strokeWidth={2.5} />
                    
                    {/* Pulse animation */}
                    <div className="
                      absolute inset-0
                      rounded-full
                      bg-gradient-to-r from-[#667eea] to-[#764ba2]
                      opacity-50
                      animate-ping
                    " style={{ animationDuration: '2s' }} />
                  </button>
                );
              }

              // Navigation Items
              return (
                <button
                  key={item.id}
                  onClick={() => onTabChange(item.id as PremiumNavTab)}
                  aria-label={item.label}
                  className={`
                    flex flex-col items-center gap-[4px]
                    py-[var(--premium-space-sm)]
                    px-[var(--premium-space-md)]
                    rounded-[var(--premium-radius-md)]
                    transition-all duration-200
                    ${isActive
                      ? 'text-[var(--premium-primary)]'
                      : 'text-[var(--premium-text-tertiary)] hover:text-[var(--premium-text-secondary)]'
                    }
                  `}
                >
                  <div className={`
                    relative
                    transition-all duration-200
                    ${isActive ? 'scale-110' : ''}
                  `}>
                    <Icon
                      size={24}
                      strokeWidth={isActive ? 2.5 : 2}
                    />
                    
                    {/* Active indicator */}
                    {isActive && (
                      <div className="
                        absolute -bottom-[6px] left-1/2 -translate-x-1/2
                        w-[4px] h-[4px]
                        rounded-full
                        bg-[var(--premium-primary)]
                      " />
                    )}
                  </div>
                  
                  <span className={`
                    body-xs font-medium
                    ${isActive ? 'opacity-100' : 'opacity-70'}
                  `}>
                    {item.label}
                  </span>
                </button>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}
/**
 * ProfileScreen
 * User profile and settings screen
 */

import { User, Bell, Lock, Palette, HelpCircle, LogOut, ChevronRight, CreditCard, Target, Settings } from 'lucide-react';

interface MenuItem {
  icon: typeof User;
  label: string;
  badge?: string;
}

interface MenuSection {
  title: string;
  items: MenuItem[];
}

const menuSections: MenuSection[] = [
  {
    title: 'Account',
    items: [
      { icon: User, label: 'Profile Settings' },
      { icon: CreditCard, label: 'Payment Methods', badge: '2' },
      { icon: Target, label: 'Budget Goals' },
    ],
  },
  {
    title: 'Preferences',
    items: [
      { icon: Bell, label: 'Notifications' },
      { icon: Palette, label: 'Appearance' },
      { icon: Lock, label: 'Privacy & Security' },
    ],
  },
  {
    title: 'Support',
    items: [
      { icon: HelpCircle, label: 'Help Center' },
      { icon: Settings, label: 'Settings' },
    ],
  },
];

export interface ProfileScreenProps {
  /** User display name */
  displayName: string;
  /** User email */
  email: string;
  /** Total transactions count */
  transactionCount?: number;
  /** Categories count */
  categoriesCount?: number;
  /** Budgets count */
  budgetsCount?: number;
}

/**
 * Profile Screen Component
 * User settings and account management
 */
export function ProfileScreen({
  displayName,
  email,
  transactionCount = 42,
  categoriesCount = 8,
  budgetsCount = 3,
}: ProfileScreenProps) {
  return (
    <div className="flex flex-col gap-[var(--spacing-lg)] pb-[var(--spacing-3xl)]">
      {/* Profile Header */}
      <div className="bg-gradient-to-br from-[var(--color-primary)] to-[var(--color-secondary)] rounded-[28px] p-[var(--spacing-lg)] text-[var(--color-on-primary)]">
        <div className="flex items-center gap-[var(--spacing-md)] mb-[var(--spacing-lg)]">
          <div className="w-[80px] h-[80px] rounded-full bg-white/20 backdrop-blur-sm flex items-center justify-center text-3xl">
            {displayName.split(' ').map(n => n[0]).join('')}
          </div>
          <div className="flex-1">
            <h2 className="mb-[var(--spacing-xs)]">{displayName}</h2>
            <p className="text-sm opacity-90">{email}</p>
          </div>
        </div>

        <div className="grid grid-cols-3 gap-[var(--spacing-sm)]">
          <div className="bg-white/20 backdrop-blur-sm rounded-xl p-[var(--spacing-sm)] text-center">
            <p className="text-2xl mb-[var(--spacing-xs)]">{transactionCount}</p>
            <p className="text-xs opacity-90">Expenses</p>
          </div>
          <div className="bg-white/20 backdrop-blur-sm rounded-xl p-[var(--spacing-sm)] text-center">
            <p className="text-2xl mb-[var(--spacing-xs)]">{categoriesCount}</p>
            <p className="text-xs opacity-90">Categories</p>
          </div>
          <div className="bg-white/20 backdrop-blur-sm rounded-xl p-[var(--spacing-sm)] text-center">
            <p className="text-2xl mb-[var(--spacing-xs)]">{budgetsCount}</p>
            <p className="text-xs opacity-90">Budgets</p>
          </div>
        </div>
      </div>

      {/* Menu Sections */}
      {menuSections.map((section, sectionIndex) => (
        <div key={sectionIndex}>
          <h3 className="text-sm text-[var(--color-on-surface-variant)] px-[var(--spacing-sm)] mb-[var(--spacing-sm)]">
            {section.title}
          </h3>
          <div className="bg-[var(--color-surface)] rounded-2xl shadow-[var(--elevation-1)] border border-[var(--color-outline-variant)] overflow-hidden">
            {section.items.map((item, itemIndex) => {
              const Icon = item.icon;
              const isLast = itemIndex === section.items.length - 1;

              return (
                <button
                  key={itemIndex}
                  className={`
                    w-full 
                    flex items-center gap-[var(--spacing-md)]
                    p-[var(--spacing-md)]
                    hover:bg-[var(--color-surface-variant)]
                    transition-colors
                    ${!isLast ? 'border-b border-[var(--color-outline-variant)]' : ''}
                  `}
                >
                  <div className="w-[40px] h-[40px] rounded-full bg-[var(--color-surface-variant)] text-[var(--color-on-surface-variant)] flex items-center justify-center">
                    <Icon size={20} />
                  </div>
                  <span className="flex-1 text-left text-[var(--color-on-surface)]">
                    {item.label}
                  </span>
                  {item.badge && (
                    <span className="px-[var(--spacing-sm)] py-[var(--spacing-xs)] bg-[var(--color-primary-container)] text-[var(--color-on-primary-container)] text-xs rounded-full">
                      {item.badge}
                    </span>
                  )}
                  <ChevronRight size={20} className="text-[var(--color-on-surface-variant)]" />
                </button>
              );
            })}
          </div>
        </div>
      ))}

      {/* Logout Button */}
      <button className="flex items-center justify-center gap-[var(--spacing-sm)] p-[var(--spacing-md)] bg-[var(--color-error-container)] text-[var(--color-on-error-container)] rounded-2xl hover:bg-[var(--color-error)] hover:text-[var(--color-on-error)] transition-colors">
        <LogOut size={20} />
        <span>Logout</span>
      </button>

      {/* Version */}
      <p className="text-center text-sm text-[var(--color-on-surface-variant)]">
        Version 1.0.0
      </p>
    </div>
  );
}

/**
 * M3 ProfileScreen
 * Material Design 3 user profile and settings screen
 * Maps to Jetpack Compose Screen with M3 ListItems
 */

import { User, Bell, Lock, Palette, HelpCircle, LogOut, ChevronRight, CreditCard, Target, Settings } from 'lucide-react';
import { M3Surface } from '../components/atoms/M3Surface';
import { M3Button } from '../components/atoms/M3Button';

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

export interface M3ProfileScreenProps {
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
 * Material 3 Profile Screen
 * User settings and account management with M3 list items
 * 
 * @example
 * <M3ProfileScreen
 *   displayName="John Doe"
 *   email="john@example.com"
 *   transactionCount={42}
 * />
 */
export function M3ProfileScreen({
  displayName,
  email,
  transactionCount = 42,
  categoriesCount = 8,
  budgetsCount = 3,
}: M3ProfileScreenProps) {
  return (
    <div className="flex flex-col gap-[var(--md-sys-spacing-lg)]">
      {/* Profile Header - M3 Extra Large Shape */}
      <div className="
        bg-gradient-to-br 
        from-[var(--md-sys-color-primary)] 
        to-[var(--md-sys-color-secondary)] 
        rounded-[var(--md-sys-shape-corner-extra-large)] 
        p-[var(--md-sys-spacing-lg)] 
        text-[var(--md-sys-color-on-primary)]
      ">
        <div className="flex items-center gap-[var(--md-sys-spacing-md)] mb-[var(--md-sys-spacing-lg)]">
          {/* Avatar */}
          <div className="
            w-[80px] h-[80px] 
            rounded-full 
            bg-white/20 
            backdrop-blur-sm 
            flex items-center justify-center 
            headline-large
          ">
            {displayName.split(' ').map(n => n[0]).join('')}
          </div>
          <div className="flex-1">
            <h2 className="headline-small mb-[var(--md-sys-spacing-xs)]">
              {displayName}
            </h2>
            <p className="body-medium opacity-90">{email}</p>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-3 gap-[var(--md-sys-spacing-sm)]">
          <M3Surface
            elevation={0}
            shape="small"
            className="bg-white/20 backdrop-blur-sm p-[var(--md-sys-spacing-sm)] text-center"
          >
            <p className="headline-medium mb-[var(--md-sys-spacing-xs)]">
              {transactionCount}
            </p>
            <p className="label-small opacity-90">Expenses</p>
          </M3Surface>
          <M3Surface
            elevation={0}
            shape="small"
            className="bg-white/20 backdrop-blur-sm p-[var(--md-sys-spacing-sm)] text-center"
          >
            <p className="headline-medium mb-[var(--md-sys-spacing-xs)]">
              {categoriesCount}
            </p>
            <p className="label-small opacity-90">Categories</p>
          </M3Surface>
          <M3Surface
            elevation={0}
            shape="small"
            className="bg-white/20 backdrop-blur-sm p-[var(--md-sys-spacing-sm)] text-center"
          >
            <p className="headline-medium mb-[var(--md-sys-spacing-xs)]">
              {budgetsCount}
            </p>
            <p className="label-small opacity-90">Budgets</p>
          </M3Surface>
        </div>
      </div>

      {/* Menu Sections */}
      {menuSections.map((section, sectionIndex) => (
        <div key={sectionIndex}>
          <h3 className="
            body-small 
            text-[var(--md-sys-color-on-surface-variant)] 
            px-[var(--md-sys-spacing-sm)] 
            mb-[var(--md-sys-spacing-sm)]
          ">
            {section.title}
          </h3>
          
          {/* M3 List Container */}
          <M3Surface
            elevation={1}
            shape="medium"
            className="overflow-hidden"
          >
            {section.items.map((item, itemIndex) => {
              const Icon = item.icon;
              const isLast = itemIndex === section.items.length - 1;

              return (
                <button
                  key={itemIndex}
                  className={`
                    w-full 
                    flex items-center gap-[var(--md-sys-spacing-md)]
                    p-[var(--md-sys-spacing-md)]
                    hover:bg-[var(--md-sys-color-on-surface)]/[0.08]
                    transition-colors
                    ${!isLast ? 'border-b border-[var(--md-sys-color-outline-variant)]' : ''}
                  `}
                >
                  {/* Leading Icon */}
                  <div className="
                    w-[40px] h-[40px] 
                    rounded-full 
                    bg-[var(--md-sys-color-surface-variant)] 
                    text-[var(--md-sys-color-on-surface-variant)] 
                    flex items-center justify-center
                  ">
                    <Icon size={20} />
                  </div>
                  
                  {/* Label */}
                  <span className="flex-1 text-left body-large text-[var(--md-sys-color-on-surface)]">
                    {item.label}
                  </span>
                  
                  {/* Badge (optional) */}
                  {item.badge && (
                    <span className="
                      px-[var(--md-sys-spacing-sm)] 
                      py-[var(--md-sys-spacing-xs)] 
                      bg-[var(--md-sys-color-primary-container)] 
                      text-[var(--md-sys-color-on-primary-container)] 
                      label-small 
                      rounded-full
                    ">
                      {item.badge}
                    </span>
                  )}
                  
                  {/* Trailing Icon */}
                  <ChevronRight size={20} className="text-[var(--md-sys-color-on-surface-variant)]" />
                </button>
              );
            })}
          </M3Surface>
        </div>
      ))}

      {/* Logout Button - M3 Filled Tonal */}
      <M3Button
        variant="filled-tonal"
        size="default"
        fullWidth
        icon={<LogOut size={20} />}
        className="
          bg-[var(--md-sys-color-error-container)] 
          text-[var(--md-sys-color-on-error-container)]
          hover:bg-[var(--md-sys-color-error)] 
          hover:text-[var(--md-sys-color-on-error)]
        "
      >
        Logout
      </M3Button>

      {/* Version - M3 Label Typography */}
      <p className="text-center label-medium text-[var(--md-sys-color-on-surface-variant)]">
        Version 1.0.0 • Material Design 3
      </p>
    </div>
  );
}

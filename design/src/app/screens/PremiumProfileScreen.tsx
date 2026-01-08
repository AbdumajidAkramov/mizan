/**
 * Premium Profile/Settings Screen
 * User profile and app settings
 */

import { PremiumCard } from '../components/premium/PremiumCard';
import { PremiumButton } from '../components/premium/PremiumButton';
import { PremiumThemeToggle } from '../components/premium/PremiumThemeToggle';
import { useTheme } from '../contexts/ThemeContext';
import {
  User,
  Bell,
  Lock,
  Palette,
  Globe,
  HelpCircle,
  FileText,
  LogOut,
  ChevronRight,
  Mail,
  Smartphone,
  Shield,
  Download,
  Share2,
  Star,
  DollarSign,
  TrendingUp,
} from 'lucide-react';

interface SettingItem {
  icon: typeof User;
  label: string;
  description?: string;
  value?: string;
  showChevron?: boolean;
  onClick?: () => void;
}

export interface PremiumProfileScreenProps {
  onNavigateToBudget?: () => void;
}

const profileSections: { title: string; items: SettingItem[] }[] = [
  {
    title: 'Finance',
    items: [
      {
        icon: DollarSign,
        label: 'Budget Management',
        description: 'Set and track category budgets',
        showChevron: true,
      },
      {
        icon: TrendingUp,
        label: 'Financial Goals',
        description: 'Track your savings goals',
        showChevron: true,
      },
    ],
  },
  {
    title: 'Account',
    items: [
      {
        icon: User,
        label: 'Personal Information',
        description: 'Update your profile details',
        showChevron: true,
      },
      {
        icon: Mail,
        label: 'Email',
        value: 'john.doe@email.com',
        showChevron: true,
      },
      {
        icon: Smartphone,
        label: 'Phone Number',
        value: '+1 (555) 123-4567',
        showChevron: true,
      },
    ],
  },
  {
    title: 'Preferences',
    items: [
      {
        icon: Bell,
        label: 'Notifications',
        description: 'Manage notification settings',
        showChevron: true,
      },
      {
        icon: Palette,
        label: 'Appearance',
        value: 'Dark Mode',
        showChevron: true,
      },
      {
        icon: Globe,
        label: 'Language',
        value: 'English (US)',
        showChevron: true,
      },
    ],
  },
  {
    title: 'Security',
    items: [
      {
        icon: Lock,
        label: 'Change Password',
        description: 'Update your password',
        showChevron: true,
      },
      {
        icon: Shield,
        label: 'Two-Factor Authentication',
        value: 'Enabled',
        showChevron: true,
      },
    ],
  },
  {
    title: 'Data & Privacy',
    items: [
      {
        icon: Download,
        label: 'Export Data',
        description: 'Download your transaction history',
        showChevron: true,
      },
      {
        icon: FileText,
        label: 'Privacy Policy',
        showChevron: true,
      },
      {
        icon: FileText,
        label: 'Terms of Service',
        showChevron: true,
      },
    ],
  },
  {
    title: 'Support',
    items: [
      {
        icon: HelpCircle,
        label: 'Help Center',
        description: 'FAQs and guides',
        showChevron: true,
      },
      {
        icon: Share2,
        label: 'Share App',
        description: 'Invite friends to join',
        showChevron: true,
      },
      {
        icon: Star,
        label: 'Rate Us',
        description: 'Share your feedback',
        showChevron: true,
      },
    ],
  },
];

export function PremiumProfileScreen({ onNavigateToBudget }: PremiumProfileScreenProps) {
  const { theme } = useTheme();
  
  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="heading-xl text-[var(--premium-text-primary)]">
          Profile
        </h1>
      </div>

      {/* Profile Card */}
      <div className="
        relative
        rounded-[var(--premium-radius-2xl)]
        p-[var(--premium-space-xl)]
        bg-gradient-to-br from-[#667eea] via-[#764ba2] to-[#f5576c]
        shadow-[var(--premium-shadow-xl)]
        overflow-hidden
      ">
        {/* Background orbs */}
        <div className="absolute top-0 right-0 w-[150px] h-[150px] bg-white/10 rounded-full blur-[50px]" />
        <div className="absolute bottom-0 left-0 w-[120px] h-[120px] bg-black/10 rounded-full blur-[40px]" />
        
        <div className="relative z-10 flex items-center gap-[var(--premium-space-lg)]">
          {/* Avatar */}
          <div className="
            w-[80px] h-[80px]
            rounded-full
            bg-white/20
            backdrop-blur-sm
            border-2 border-white/30
            flex items-center justify-center
            display-sm text-white
            flex-shrink-0
          ">
            JD
          </div>

          {/* Info */}
          <div className="flex-1">
            <h2 className="heading-xl text-white mb-[4px]">
              John Doe
            </h2>
            <p className="body-md text-white/80 mb-[var(--premium-space-md)]">
              Premium Member
            </p>
            
            <div className="flex gap-[var(--premium-space-md)]">
              <div className="
                px-[var(--premium-space-md)] py-[var(--premium-space-sm)]
                bg-white/20
                backdrop-blur-sm
                rounded-[var(--premium-radius-md)]
                border border-white/30
              ">
                <p className="body-xs text-white/70">Member since</p>
                <p className="body-md text-white font-medium">Jan 2024</p>
              </div>
              <div className="
                px-[var(--premium-space-md)] py-[var(--premium-space-sm)]
                bg-white/20
                backdrop-blur-sm
                rounded-[var(--premium-radius-md)]
                border border-white/30
              ">
                <p className="body-xs text-white/70">Transactions</p>
                <p className="body-md text-white font-medium">156</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-3 gap-[var(--premium-space-md)]">
        <PremiumCard variant="glass" className="p-[var(--premium-space-md)] text-center">
          <div className="
            w-[40px] h-[40px]
            mx-auto
            bg-gradient-to-br from-[var(--premium-success)] to-[#4facfe]
            rounded-full
            flex items-center justify-center
            mb-[var(--premium-space-sm)]
          ">
            <Download size={20} className="text-white" />
          </div>
          <p className="body-xs text-[var(--premium-text-muted)] mb-[2px]">Income</p>
          <p className="heading-sm text-[var(--premium-text-primary)]">$12.5K</p>
        </PremiumCard>

        <PremiumCard variant="glass" className="p-[var(--premium-space-md)] text-center">
          <div className="
            w-[40px] h-[40px]
            mx-auto
            bg-gradient-to-br from-[var(--premium-error)] to-[var(--premium-secondary)]
            rounded-full
            flex items-center justify-center
            mb-[var(--premium-space-sm)]
          ">
            <Share2 size={20} className="text-white" />
          </div>
          <p className="body-xs text-[var(--premium-text-muted)] mb-[2px]">Expenses</p>
          <p className="heading-sm text-[var(--premium-text-primary)]">$8.2K</p>
        </PremiumCard>

        <PremiumCard variant="glass" className="p-[var(--premium-space-md)] text-center">
          <div className="
            w-[40px] h-[40px]
            mx-auto
            bg-gradient-to-br from-[var(--premium-primary)] to-[var(--premium-secondary)]
            rounded-full
            flex items-center justify-center
            mb-[var(--premium-space-sm)]
          ">
            <Star size={20} className="text-white" />
          </div>
          <p className="body-xs text-[var(--premium-text-muted)] mb-[2px]">Saved</p>
          <p className="heading-sm text-[var(--premium-text-primary)]">$4.3K</p>
        </PremiumCard>
      </div>

      {/* Settings Sections */}
      {profileSections.map((section) => (
        <div key={section.title}>
          <h3 className="heading-sm text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)] uppercase tracking-wide">
            {section.title}
          </h3>

          <div className="space-y-[var(--premium-space-sm)]">
            {section.items.map((item, index) => {
              const Icon = item.icon;
              const isAppearance = item.label === 'Appearance';
              
              return (
                <PremiumCard
                  key={index}
                  variant="glass"
                  hover={!isAppearance}
                  onClick={!isAppearance ? item.onClick : undefined}
                  className="p-[var(--premium-space-lg)] cursor-pointer"
                >
                  <div className="flex items-center gap-[var(--premium-space-md)]">
                    {/* Icon */}
                    <div className="
                      w-[40px] h-[40px]
                      bg-[var(--premium-surface-2)]
                      rounded-[var(--premium-radius-md)]
                      flex items-center justify-center
                      flex-shrink-0
                    ">
                      <Icon size={20} className="text-[var(--premium-text-secondary)]" />
                    </div>

                    {/* Content */}
                    <div className="flex-1 min-w-0">
                      <p className="body-md text-[var(--premium-text-primary)] font-medium mb-[2px]">
                        {item.label}
                      </p>
                      {item.description && (
                        <p className="body-sm text-[var(--premium-text-tertiary)]">
                          {item.description}
                        </p>
                      )}
                      {item.value && !isAppearance && (
                        <p className="body-sm text-[var(--premium-text-tertiary)]">
                          {item.value}
                        </p>
                      )}
                      {isAppearance && (
                        <p className="body-sm text-[var(--premium-text-tertiary)]">
                          {theme === 'dark' ? 'Dark Mode' : 'Light Mode'}
                        </p>
                      )}
                    </div>

                    {/* Theme Toggle or Chevron */}
                    {isAppearance ? (
                      <PremiumThemeToggle />
                    ) : item.showChevron && (
                      <ChevronRight size={20} className="text-[var(--premium-text-muted)] flex-shrink-0" />
                    )}
                  </div>
                </PremiumCard>
              );
            })}
          </div>
        </div>
      ))}

      {/* Logout Button */}
      <PremiumButton
        variant="outline"
        size="lg"
        fullWidth
        icon={<LogOut size={20} />}
        iconPosition="left"
      >
        Log Out
      </PremiumButton>

      {/* App Version */}
      <div className="text-center pb-[var(--premium-space-lg)]">
        <p className="body-sm text-[var(--premium-text-muted)]">
          Expense Manager v1.0.0
        </p>
        <p className="body-xs text-[var(--premium-text-muted)]">
          © 2026 All rights reserved
        </p>
      </div>
    </div>
  );
}
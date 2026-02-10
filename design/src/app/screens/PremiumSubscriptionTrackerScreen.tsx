/**
 * Premium Subscription Tracker Screen
 * Manage and track all recurring payments and subscriptions
 * 
 * @features
 * - Monthly total summary with next payment indicator
 * - Active subscriptions list with progress bars
 * - Service logos/icons with billing cycles
 * - Status badges (Active, Trial ends soon)
 * - Time until next payment visualization
 * - Add new subscription action
 * 
 * @architecture Material 3 with glassmorphism
 * @design Deep dark blue/purple (#1A1A2E) background, 8dp grid system
 */

import { useState } from 'react';
import {
  ChevronLeft,
  Plus,
  Calendar,
  CreditCard,
  Music,
  Play,
  Cloud,
  Tv,
  Film,
  Zap,
  Gamepad2,
  Smartphone,
  Coffee,
  Bell,
} from 'lucide-react';

/**
 * Billing Cycle Type
 */
export type BillingCycle = 'monthly' | 'yearly' | 'weekly';

/**
 * Subscription Status Type
 */
export type SubscriptionStatus = 'active' | 'trial' | 'cancelled';

/**
 * Subscription Interface
 */
export interface Subscription {
  id: string;
  serviceName: string;
  serviceIcon: typeof Music;
  iconColor: string;
  amount: number; // in UZS
  billingCycle: BillingCycle;
  nextBillingDate: Date;
  status: SubscriptionStatus;
  trialEndsInDays?: number;
  category: string;
}

/**
 * Subscription State Interface
 */
export interface SubscriptionState {
  subscriptions: Subscription[];
  monthlyTotal: number;
  nextPaymentDate: Date;
  daysUntilNextPayment: number;
}

export interface PremiumSubscriptionTrackerScreenProps {
  /** Callback when back button is pressed */
  onBack: () => void;
  /** Callback when subscription card is tapped */
  onSubscriptionDetail?: (subscriptionId: string) => void;
  /** Callback to navigate to create subscription screen */
  onNavigateToCreateSubscription?: () => void;
}

// Mock Subscription Data
const MOCK_SUBSCRIPTIONS: Subscription[] = [
  {
    id: 'sub-1',
    serviceName: 'Netflix',
    serviceIcon: Tv,
    iconColor: '#E50914',
    amount: 120000,
    billingCycle: 'monthly',
    nextBillingDate: new Date('2026-02-11'),
    status: 'active',
    category: 'Entertainment',
  },
  {
    id: 'sub-2',
    serviceName: 'Spotify',
    serviceIcon: Music,
    iconColor: '#1DB954',
    amount: 80000,
    billingCycle: 'monthly',
    nextBillingDate: new Date('2026-02-15'),
    status: 'active',
    category: 'Music',
  },
  {
    id: 'sub-3',
    serviceName: 'YouTube Premium',
    serviceIcon: Play,
    iconColor: '#FF0000',
    amount: 95000,
    billingCycle: 'monthly',
    nextBillingDate: new Date('2026-02-20'),
    status: 'trial',
    trialEndsInDays: 5,
    category: 'Entertainment',
  },
  {
    id: 'sub-4',
    serviceName: 'iCloud Storage',
    serviceIcon: Cloud,
    iconColor: '#007AFF',
    amount: 50000,
    billingCycle: 'monthly',
    nextBillingDate: new Date('2026-02-25'),
    status: 'active',
    category: 'Cloud Storage',
  },
  {
    id: 'sub-5',
    serviceName: 'Adobe Creative',
    serviceIcon: Film,
    iconColor: '#FF0000',
    amount: 1200000,
    billingCycle: 'yearly',
    nextBillingDate: new Date('2026-08-15'),
    status: 'active',
    category: 'Productivity',
  },
  {
    id: 'sub-6',
    serviceName: 'ChatGPT Plus',
    serviceIcon: Zap,
    iconColor: '#10A37F',
    amount: 150000,
    billingCycle: 'monthly',
    nextBillingDate: new Date('2026-02-12'),
    status: 'active',
    category: 'AI Tools',
  },
];

/**
 * Calculate subscription state
 */
function calculateSubscriptionState(subscriptions: Subscription[]): SubscriptionState {
  // Calculate monthly total (convert yearly to monthly)
  const monthlyTotal = subscriptions.reduce((sum, sub) => {
    if (sub.billingCycle === 'yearly') {
      return sum + sub.amount / 12;
    } else if (sub.billingCycle === 'weekly') {
      return sum + sub.amount * 4.33; // Approximate weeks per month
    }
    return sum + sub.amount;
  }, 0);

  // Find next payment date
  const today = new Date();
  const upcomingPayments = subscriptions
    .map((sub) => sub.nextBillingDate)
    .filter((date) => date > today)
    .sort((a, b) => a.getTime() - b.getTime());

  const nextPaymentDate = upcomingPayments[0] || today;
  const daysUntilNextPayment = Math.ceil(
    (nextPaymentDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24)
  );

  return {
    subscriptions,
    monthlyTotal,
    nextPaymentDate,
    daysUntilNextPayment,
  };
}

/**
 * Format number with spaces: "250 000"
 */
function formatUZS(amount: number): string {
  return Math.floor(Math.abs(amount))
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
}

/**
 * Format date to readable string
 */
function formatDate(date: Date): string {
  const options: Intl.DateTimeFormatOptions = { month: 'short', day: 'numeric' };
  return date.toLocaleDateString('en-US', options);
}

/**
 * Calculate days until next payment
 */
function getDaysUntilPayment(date: Date): number {
  const today = new Date();
  const diffTime = date.getTime() - today.getTime();
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

/**
 * Get billing cycle display text
 */
function getBillingCycleText(cycle: BillingCycle): string {
  switch (cycle) {
    case 'monthly':
      return 'Monthly';
    case 'yearly':
      return 'Yearly';
    case 'weekly':
      return 'Weekly';
    default:
      return 'Monthly';
  }
}

/**
 * Get progress percentage for subscription
 */
function getProgressPercentage(subscription: Subscription): number {
  const daysUntil = getDaysUntilPayment(subscription.nextBillingDate);
  let totalDays = 30; // Default to monthly

  if (subscription.billingCycle === 'yearly') {
    totalDays = 365;
  } else if (subscription.billingCycle === 'weekly') {
    totalDays = 7;
  }

  const daysPassed = totalDays - daysUntil;
  return Math.max(0, Math.min(100, (daysPassed / totalDays) * 100));
}

/**
 * Status Badge Component
 */
function StatusBadge({ subscription }: { subscription: Subscription }) {
  if (subscription.status === 'trial' && subscription.trialEndsInDays) {
    return (
      <div
        className="
          flex items-center gap-[4px]
          px-[8px] py-[4px]
          rounded-full
          bg-[#F59E0B]/20
          border border-[#F59E0B]/30
        "
      >
        <Bell size={10} className="text-[#F59E0B]" />
        <span className="body-xs font-semibold text-[#F59E0B]">
          Trial ends in {subscription.trialEndsInDays}d
        </span>
      </div>
    );
  }

  return (
    <div
      className="
        px-[8px] py-[4px]
        rounded-full
        bg-[#10B981]/20
        border border-[#10B981]/30
      "
    >
      <span className="body-xs font-semibold text-[#10B981]">Active</span>
    </div>
  );
}

/**
 * Premium Subscription Tracker Screen Component
 */
export function PremiumSubscriptionTrackerScreen({
  onBack,
  onSubscriptionDetail,
  onNavigateToCreateSubscription,
}: PremiumSubscriptionTrackerScreenProps) {
  const [subscriptions] = useState<Subscription[]>(MOCK_SUBSCRIPTIONS);
  const subscriptionState = calculateSubscriptionState(subscriptions);

  return (
    <div className="fixed inset-0 z-50 bg-[#1A1A2E] flex flex-col">
      {/* Header */}
      <div
        className="
          flex-shrink-0
          px-[var(--premium-space-lg)]
          pt-[var(--premium-space-xl)]
          pb-[var(--premium-space-md)]
        "
      >
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
              Subscriptions
            </h1>
            <p className="body-sm text-white/60">
              Manage your recurring payments
            </p>
          </div>
        </div>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pb-[var(--premium-space-2xl)]">
        <div className="space-y-[var(--premium-space-xl)]">
          {/* Monthly Summary Card - Premium Glass */}
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
                bg-[#10B981]
                opacity-20
                blur-[80px]
              "
            />

            <div className="relative z-10">
              <div className="flex items-center gap-[8px] mb-[var(--premium-space-md)]">
                <div
                  className="
                    w-[44px] h-[44px]
                    rounded-[var(--premium-radius-lg)]
                    bg-[#10B981]/20
                    border border-[#10B981]/30
                    flex items-center justify-center
                  "
                >
                  <CreditCard size={20} className="text-[#10B981]" />
                </div>
                <div>
                  <p className="body-xs text-white/40">Monthly Total</p>
                  <p className="body-sm text-white/80">
                    {subscriptionState.subscriptions.length} active subscriptions
                  </p>
                </div>
              </div>

              {/* Total Amount */}
              <div className="flex items-baseline gap-[4px] mb-[var(--premium-space-md)]">
                <span className="text-[32px] font-bold text-white tracking-tight">
                  {formatUZS(subscriptionState.monthlyTotal)}
                </span>
                <span className="text-[16px] font-medium text-white/50 ml-[4px]">
                  UZS
                </span>
              </div>

              {/* Next Payment Info */}
              <div
                className="
                  inline-flex items-center gap-[6px]
                  px-[12px] py-[6px]
                  rounded-full
                  bg-[#F59E0B]/20
                  border border-[#F59E0B]/30
                "
              >
                <Calendar size={14} className="text-[#F59E0B]" />
                <span className="body-xs font-semibold text-[#F59E0B]">
                  Next payment in {subscriptionState.daysUntilNextPayment} days
                </span>
              </div>
            </div>
          </div>

          {/* Active Subscriptions List */}
          <div className="space-y-[var(--premium-space-md)]">
            {/* Section Header */}
            <div className="flex items-center justify-between px-[4px]">
              <h3 className="heading-sm text-white/90 font-semibold">
                Active Subscriptions
              </h3>
              <span className="body-xs text-white/40">
                {subscriptionState.subscriptions.length} {subscriptionState.subscriptions.length === 1 ? 'service' : 'services'}
              </span>
            </div>

            {/* Subscription Items */}
            <div className="space-y-[8px]">
              {subscriptionState.subscriptions.map((subscription) => {
                const Icon = subscription.serviceIcon;
                const daysUntil = getDaysUntilPayment(subscription.nextBillingDate);
                const progressPercent = getProgressPercentage(subscription);

                return (
                  <button
                    key={subscription.id}
                    onClick={() => onSubscriptionDetail?.(subscription.id)}
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
                    "
                  >
                    {/* Top Row: Icon, Name, Status, Amount */}
                    <div className="flex items-start gap-[var(--premium-space-md)] mb-[12px]">
                      {/* Service Icon */}
                      <div
                        className="
                          w-[52px] h-[52px]
                          rounded-[var(--premium-radius-lg)]
                          flex items-center justify-center
                          flex-shrink-0
                        "
                        style={{
                          backgroundColor: `${subscription.iconColor}20`,
                          border: `1px solid ${subscription.iconColor}30`,
                        }}
                      >
                        <Icon size={26} style={{ color: subscription.iconColor }} />
                      </div>

                      {/* Service Info */}
                      <div className="flex-1 min-w-0 text-left">
                        <div className="flex items-center gap-[6px] mb-[2px]">
                          <p className="body-md font-semibold text-white truncate">
                            {subscription.serviceName}
                          </p>
                          <StatusBadge subscription={subscription} />
                        </div>
                        <p className="body-xs text-white/40">
                          {getBillingCycleText(subscription.billingCycle)} • Next: {formatDate(subscription.nextBillingDate)}
                        </p>
                      </div>

                      {/* Amount */}
                      <div className="text-right flex-shrink-0">
                        <p className="body-md font-bold text-white">
                          {formatUZS(subscription.amount)}
                        </p>
                        <p className="body-xs text-white/30">UZS</p>
                      </div>
                    </div>

                    {/* Progress Bar */}
                    <div className="space-y-[4px]">
                      <div className="flex items-center justify-between">
                        <p className="body-xs text-white/40">
                          Time until next payment
                        </p>
                        <p className="body-xs font-semibold text-white/60">
                          {daysUntil} {daysUntil === 1 ? 'day' : 'days'}
                        </p>
                      </div>
                      <div className="w-full h-[6px] bg-white/10 rounded-full overflow-hidden">
                        <div
                          className="h-full rounded-full transition-all duration-500"
                          style={{
                            width: `${progressPercent}%`,
                            backgroundColor: subscription.status === 'trial' ? '#F59E0B' : '#10B981',
                          }}
                        />
                      </div>
                    </div>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Add New Subscription Button - Inline Dashed Style */}
          <button
            onClick={onNavigateToCreateSubscription}
            className="
              w-full
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-xl)]
              bg-transparent
              border-2 border-dashed border-white/20
              hover:border-[#10B981]/50
              hover:bg-[#10B981]/5
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
                bg-[#10B981]/20
                border border-[#10B981]/30
                flex items-center justify-center
                flex-shrink-0
              "
            >
              <Plus size={24} className="text-[#10B981]" strokeWidth={2.5} />
            </div>

            {/* Text */}
            <div className="flex-1 text-left">
              <p className="body-md font-medium text-white">
                Add New Subscription
              </p>
              <p className="body-xs text-white/40">
                Track your recurring payments
              </p>
            </div>
          </button>

          {/* Insights Card */}
          <div
            className="
              p-[var(--premium-space-lg)]
              rounded-[var(--premium-radius-xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <div className="flex items-start gap-[12px]">
              <div
                className="
                  w-[36px] h-[36px]
                  rounded-full
                  bg-[#10B981]/20
                  flex items-center justify-center
                  flex-shrink-0
                "
              >
                <Zap size={18} className="text-[#10B981]" />
              </div>
              <div>
                <p className="body-sm font-medium text-white mb-[4px]">
                  Smart Tip
                </p>
                <p className="body-xs text-white/60 leading-relaxed">
                  You're spending <span className="font-semibold text-[#10B981]">{formatUZS(subscriptionState.monthlyTotal)} UZS</span> per month on subscriptions. 
                  Review unused services to save money.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

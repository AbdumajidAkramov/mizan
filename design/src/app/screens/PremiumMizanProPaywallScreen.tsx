/**
 * Premium Mizan Pro Paywall Screen
 * CRO-optimized subscription paywall with premium gold gradient theming
 * 
 * @features
 * - Gold crown hero section with "Mizan PRO" branding
 * - Pro features checklist with emerald checkmarks
 * - 3 pricing tiers (Monthly, Yearly, Lifetime)
 * - Highlighted "Most Popular" yearly plan with savings badge
 * - 7-day free trial CTA with gold gradient
 * - Footer links (Restore, Terms, Privacy)
 * 
 * @architecture Material 3 with glassmorphism + gold premium accents
 * @design Deep dark blue/purple (#1A1A2E) background, 8dp grid system
 */

import { useState } from 'react';
import {
  X,
  Crown,
  CheckCircle2,
  TrendingUp,
  Bell,
  FileText,
  Sparkles,
  Zap,
  Shield,
  Infinity,
} from 'lucide-react';

/**
 * Subscription Plan Type
 */
export type SubscriptionPlanType = 'monthly' | 'yearly' | 'lifetime';

/**
 * Subscription Plan Interface
 */
export interface SubscriptionPlan {
  id: SubscriptionPlanType;
  name: string;
  price: string;
  priceNumeric: number;
  billingCycle: string;
  savings?: string;
  badge?: string;
  highlighted: boolean;
  description: string;
}

/**
 * Pro Feature Interface
 */
export interface ProFeature {
  id: string;
  title: string;
  icon: typeof CheckCircle2;
}

export interface PremiumMizanProPaywallScreenProps {
  /** Callback when close button is pressed */
  onClose: () => void;
  /** Callback when user starts free trial */
  onStartTrial: (planId: SubscriptionPlanType) => void;
  /** Callback when user taps "Restore Purchase" */
  onRestorePurchase?: () => void;
  /** Callback when user taps "Terms of Service" */
  onTermsOfService?: () => void;
  /** Callback when user taps "Privacy Policy" */
  onPrivacyPolicy?: () => void;
}

// Subscription Plans
const SUBSCRIPTION_PLANS: SubscriptionPlan[] = [
  {
    id: 'monthly',
    name: 'Monthly',
    price: '$4.99',
    priceNumeric: 4.99,
    billingCycle: '/ month',
    description: 'Billed monthly',
    highlighted: false,
  },
  {
    id: 'yearly',
    name: 'Yearly',
    price: '$39.99',
    priceNumeric: 39.99,
    billingCycle: '/ year',
    savings: 'SAVE 33%',
    badge: 'Most Popular',
    description: 'Billed annually',
    highlighted: true,
  },
  {
    id: 'lifetime',
    name: 'Lifetime',
    price: '$99.99',
    priceNumeric: 99.99,
    billingCycle: 'one-time',
    description: 'Pay once, use forever',
    highlighted: false,
  },
];

// Pro Features
const PRO_FEATURES: ProFeature[] = [
  {
    id: 'accounts',
    title: 'Unlimited Accounts & Categories',
    icon: Infinity,
  },
  {
    id: 'analytics',
    title: 'Advanced Statistics & Analytics',
    icon: TrendingUp,
  },
  {
    id: 'subscriptions',
    title: 'Subscription Tracking & Reminders',
    icon: Bell,
  },
  {
    id: 'export',
    title: 'Data Export (PDF, CSV, Excel)',
    icon: FileText,
  },
  {
    id: 'ai-tips',
    title: 'AI-Powered Financial Tips',
    icon: Sparkles,
  },
];

/**
 * Pricing Card Component
 */
function PricingCard({
  plan,
  isSelected,
  onClick,
}: {
  plan: SubscriptionPlan;
  isSelected: boolean;
  onClick: () => void;
}) {
  return (
    <button
      onClick={onClick}
      className={`
        relative
        w-full
        p-[var(--premium-space-lg)]
        rounded-[var(--premium-radius-2xl)]
        transition-all duration-300
        active:scale-[0.97]
        ${
          plan.highlighted
            ? `
              bg-gradient-to-br from-white/15 to-white/5
              backdrop-blur-2xl
              border-2 border-[#F59E0B]/40
              shadow-[0_0_30px_rgba(245,158,11,0.15)]
            `
            : `
              bg-white/5
              backdrop-blur-xl
              border ${isSelected ? 'border-[#10B981]/50' : 'border-white/10'}
              hover:border-white/20
            `
        }
      `}
    >
      {/* Badge - Most Popular / Savings */}
      {(plan.badge || plan.savings) && (
        <div className="absolute -top-[12px] left-1/2 -translate-x-1/2 flex gap-[8px]">
          {plan.badge && (
            <div
              className="
                px-[12px] py-[4px]
                rounded-full
                bg-gradient-to-r from-[#F59E0B] to-[#D97706]
                border border-[#F59E0B]/50
                shadow-lg
              "
            >
              <span className="body-xs font-bold text-black">
                {plan.badge}
              </span>
            </div>
          )}
          {plan.savings && (
            <div
              className="
                px-[10px] py-[4px]
                rounded-full
                bg-[#10B981]/30
                border border-[#10B981]/50
              "
            >
              <span className="body-xs font-bold text-[#10B981]">
                {plan.savings}
              </span>
            </div>
          )}
        </div>
      )}

      {/* Gold Glow Effect for Highlighted Plan */}
      {plan.highlighted && (
        <div
          className="
            absolute -inset-[1px]
            rounded-[var(--premium-radius-2xl)]
            bg-gradient-to-r from-[#F59E0B]/20 via-[#D97706]/10 to-[#F59E0B]/20
            blur-sm
            -z-10
          "
        />
      )}

      {/* Selection Indicator */}
      {isSelected && (
        <div className="absolute top-[12px] right-[12px]">
          <CheckCircle2 size={24} className="text-[#10B981]" fill="#10B981" />
        </div>
      )}

      {/* Plan Name */}
      <div className="mb-[8px]">
        <p className={`body-md font-semibold ${plan.highlighted ? 'text-[#F59E0B]' : 'text-white/80'}`}>
          {plan.name}
        </p>
      </div>

      {/* Price */}
      <div className="flex items-baseline gap-[4px] mb-[4px]">
        <span className="text-[28px] font-bold text-white">
          {plan.price}
        </span>
        <span className="body-sm text-white/50">
          {plan.billingCycle}
        </span>
      </div>

      {/* Description */}
      <p className="body-xs text-white/40">
        {plan.description}
      </p>
    </button>
  );
}

/**
 * Premium Mizan Pro Paywall Screen Component
 */
export function PremiumMizanProPaywallScreen({
  onClose,
  onStartTrial,
  onRestorePurchase,
  onTermsOfService,
  onPrivacyPolicy,
}: PremiumMizanProPaywallScreenProps) {
  const [selectedPlan, setSelectedPlan] = useState<SubscriptionPlanType>('yearly');

  return (
    <div className="fixed inset-0 z-[100] bg-[#1A1A2E] flex flex-col overflow-hidden">
      {/* Animated Gold Gradient Background */}
      <div className="fixed inset-0 pointer-events-none overflow-hidden">
        <div
          className="absolute top-[-20%] right-[-10%] w-[600px] h-[600px] rounded-full opacity-30 blur-[150px]"
          style={{ background: 'radial-gradient(circle, #F59E0B 0%, transparent 70%)' }}
        />
        <div
          className="absolute bottom-[-10%] left-[-10%] w-[500px] h-[500px] rounded-full opacity-20 blur-[120px]"
          style={{ background: 'radial-gradient(circle, #D97706 0%, transparent 70%)' }}
        />
      </div>

      {/* Close Button */}
      <div className="absolute top-[var(--premium-space-lg)] left-[var(--premium-space-lg)] z-10">
        <button
          onClick={onClose}
          className="
            w-[40px] h-[40px]
            rounded-full
            bg-white/10
            backdrop-blur-xl
            border border-white/20
            hover:bg-white/20
            flex items-center justify-center
            transition-all duration-200
            active:scale-95
          "
        >
          <X size={20} className="text-white" />
        </button>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pt-[var(--premium-space-2xl)] pb-[var(--premium-space-2xl)]">
        <div className="max-w-lg mx-auto space-y-[var(--premium-space-2xl)]">
          {/* Header - Crown + Mizan PRO */}
          <div className="text-center pt-[var(--premium-space-xl)]">
            {/* Crown Icon with Gold Glow */}
            <div className="relative inline-flex mb-[var(--premium-space-md)]">
              <div
                className="
                  absolute inset-0
                  bg-gradient-to-br from-[#F59E0B] to-[#D97706]
                  rounded-full
                  blur-[30px]
                  opacity-50
                "
              />
              <div
                className="
                  relative
                  w-[80px] h-[80px]
                  rounded-full
                  bg-gradient-to-br from-[#F59E0B]/30 to-[#D97706]/20
                  backdrop-blur-xl
                  border border-[#F59E0B]/50
                  flex items-center justify-center
                "
              >
                <Crown size={40} className="text-[#F59E0B]" fill="#F59E0B" />
              </div>
            </div>

            {/* Mizan PRO Text */}
            <h2
              className="
                text-[32px] font-bold
                bg-gradient-to-r from-[#F59E0B] via-[#FBBF24] to-[#D97706]
                bg-clip-text text-transparent
                mb-[var(--premium-space-sm)]
              "
            >
              Mizan PRO
            </h2>
            <div className="flex items-center justify-center gap-[6px] mb-[4px]">
              <Shield size={14} className="text-[#10B981]" />
              <span className="body-xs font-medium text-[#10B981]">
                Secure • No Ads • Premium Support
              </span>
            </div>
          </div>

          {/* Hero Section */}
          <div className="text-center">
            <h1 className="heading-lg text-white font-bold mb-[var(--premium-space-md)]">
              Unlock Your Full Financial Potential
            </h1>
            <p className="body-md text-white/60 leading-relaxed max-w-md mx-auto">
              Experience advanced tracking, AI insights & total control over your finances.
            </p>
          </div>

          {/* Pro Features Checklist - Glassmorphism Card */}
          <div
            className="
              p-[var(--premium-space-xl)]
              rounded-[var(--premium-radius-2xl)]
              bg-white/5
              backdrop-blur-xl
              border border-white/10
            "
          >
            <div className="flex items-center gap-[8px] mb-[var(--premium-space-lg)]">
              <Zap size={20} className="text-[#F59E0B]" fill="#F59E0B" />
              <h3 className="heading-sm text-white font-semibold">
                What You'll Get
              </h3>
            </div>

            <div className="space-y-[var(--premium-space-md)]">
              {PRO_FEATURES.map((feature) => {
                const Icon = feature.icon;
                return (
                  <div
                    key={feature.id}
                    className="flex items-center gap-[var(--premium-space-md)]"
                  >
                    {/* Emerald Checkmark */}
                    <div
                      className="
                        w-[32px] h-[32px]
                        rounded-full
                        bg-[#10B981]/20
                        border border-[#10B981]/40
                        flex items-center justify-center
                        flex-shrink-0
                      "
                    >
                      <CheckCircle2 size={18} className="text-[#10B981]" />
                    </div>

                    {/* Feature Icon */}
                    <div
                      className="
                        w-[36px] h-[36px]
                        rounded-[var(--premium-radius-md)]
                        bg-white/5
                        flex items-center justify-center
                        flex-shrink-0
                      "
                    >
                      <Icon size={18} className="text-white/60" />
                    </div>

                    {/* Feature Text */}
                    <p className="body-md text-white font-medium flex-1">
                      {feature.title}
                    </p>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Pricing Tiers */}
          <div>
            <h3 className="heading-sm text-white font-semibold text-center mb-[var(--premium-space-lg)]">
              Choose Your Plan
            </h3>
            <div className="space-y-[var(--premium-space-lg)]">
              {SUBSCRIPTION_PLANS.map((plan) => (
                <PricingCard
                  key={plan.id}
                  plan={plan}
                  isSelected={selectedPlan === plan.id}
                  onClick={() => setSelectedPlan(plan.id)}
                />
              ))}
            </div>
          </div>

          {/* Primary CTA Button */}
          <div className="space-y-[var(--premium-space-md)]">
            <button
              onClick={() => onStartTrial(selectedPlan)}
              className="
                relative
                w-full
                py-[var(--premium-space-lg)]
                px-[var(--premium-space-xl)]
                rounded-[var(--premium-radius-2xl)]
                bg-gradient-to-r from-[#F59E0B] to-[#D97706]
                hover:from-[#FBBF24] hover:to-[#F59E0B]
                active:scale-[0.98]
                transition-all duration-200
                shadow-[0_8px_30px_rgba(245,158,11,0.3)]
                overflow-hidden
                group
              "
            >
              {/* Shimmer Effect */}
              <div
                className="
                  absolute inset-0
                  bg-gradient-to-r from-transparent via-white/20 to-transparent
                  -translate-x-full
                  group-hover:translate-x-full
                  transition-transform duration-1000
                "
              />

              <div className="relative flex items-center justify-center gap-[8px]">
                <Crown size={24} className="text-black" fill="black" />
                <span className="text-[18px] font-bold text-black">
                  START 7-DAY FREE TRIAL
                </span>
              </div>
            </button>

            {/* Trial Details */}
            <p className="body-xs text-white/50 text-center px-[var(--premium-space-lg)]">
              Free for 7 days, then {SUBSCRIPTION_PLANS.find(p => p.id === selectedPlan)?.price} {SUBSCRIPTION_PLANS.find(p => p.id === selectedPlan)?.billingCycle}. Cancel anytime.
            </p>
          </div>

          {/* Footer Links */}
          <div className="flex items-center justify-center gap-[var(--premium-space-lg)] flex-wrap">
            <button
              onClick={onRestorePurchase}
              className="
                body-xs text-white/40
                hover:text-white/60
                transition-colors duration-200
                underline underline-offset-2
              "
            >
              Restore Purchase
            </button>
            <span className="text-white/20">•</span>
            <button
              onClick={onTermsOfService}
              className="
                body-xs text-white/40
                hover:text-white/60
                transition-colors duration-200
                underline underline-offset-2
              "
            >
              Terms of Service
            </button>
            <span className="text-white/20">•</span>
            <button
              onClick={onPrivacyPolicy}
              className="
                body-xs text-white/40
                hover:text-white/60
                transition-colors duration-200
                underline underline-offset-2
              "
            >
              Privacy Policy
            </button>
          </div>

          {/* Trust Badge */}
          <div
            className="
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-lg)]
              bg-white/5
              border border-white/10
              text-center
            "
          >
            <div className="flex items-center justify-center gap-[6px] mb-[4px]">
              <Shield size={16} className="text-[#10B981]" />
              <span className="body-sm font-semibold text-white">
                100% Secure Payment
              </span>
            </div>
            <p className="body-xs text-white/40">
              Your data is encrypted and protected
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

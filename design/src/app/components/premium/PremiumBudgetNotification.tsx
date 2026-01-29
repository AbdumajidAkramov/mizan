/**
 * Premium Budget Notification Component
 * Toast-style notifications for budget alerts
 * Shown when spending approaches budget thresholds
 * 
 * @architecture Material 3 with progressive feedback
 * @design Glassmorphism with status-based colors
 */

import { useState, useEffect } from 'react';
import { X, AlertTriangle, AlertCircle, TrendingUp, CheckCircle } from 'lucide-react';
import { CategoryIcon } from '../atoms/CategoryIcon';
import type { TransactionCategory } from '../../types/domain';
import { getCategoryMetadata } from '../../mocks/data';

// Notification Type
export type BudgetNotificationType = 'warning' | 'critical' | 'success' | 'info';

// Budget Notification Interface
export interface BudgetNotification {
  id: string;
  type: BudgetNotificationType;
  category: TransactionCategory;
  categoryLabel: string;
  percentage: number;
  spent: number;
  budget: number;
  remaining: number;
  message: string;
  timestamp: Date;
}

// Props Interface
export interface PremiumBudgetNotificationProps {
  /** The notification to display */
  notification: BudgetNotification;
  /** Callback when notification is dismissed */
  onDismiss: (id: string) => void;
  /** Auto-dismiss duration in ms (0 = no auto-dismiss) */
  autoDismissMs?: number;
  /** Position of the notification */
  position?: 'top' | 'bottom';
}

export function PremiumBudgetNotification({
  notification,
  onDismiss,
  autoDismissMs = 5000,
  position = 'top',
}: PremiumBudgetNotificationProps) {
  const [isVisible, setIsVisible] = useState(false);
  const [isExiting, setIsExiting] = useState(false);

  const categoryMeta = getCategoryMetadata(notification.category);

  // Animate in on mount
  useEffect(() => {
    const timer = setTimeout(() => setIsVisible(true), 10);
    return () => clearTimeout(timer);
  }, []);

  // Auto-dismiss timer
  useEffect(() => {
    if (autoDismissMs > 0) {
      const timer = setTimeout(() => handleDismiss(), autoDismissMs);
      return () => clearTimeout(timer);
    }
  }, [autoDismissMs]);

  const handleDismiss = () => {
    setIsExiting(true);
    setTimeout(() => {
      onDismiss(notification.id);
    }, 300);
  };

  // Get notification styling based on type
  const getNotificationStyle = () => {
    switch (notification.type) {
      case 'critical':
        return {
          bg: 'from-red-500/20 to-red-600/10',
          border: 'border-red-500/30',
          icon: <AlertCircle size={24} className="text-red-500" />,
          accentColor: '#ef4444',
        };
      case 'warning':
        return {
          bg: 'from-amber-500/20 to-orange-500/10',
          border: 'border-amber-500/30',
          icon: <AlertTriangle size={24} className="text-amber-500" />,
          accentColor: '#f59e0b',
        };
      case 'success':
        return {
          bg: 'from-emerald-500/20 to-green-600/10',
          border: 'border-emerald-500/30',
          icon: <CheckCircle size={24} className="text-emerald-500" />,
          accentColor: '#10b981',
        };
      case 'info':
        return {
          bg: 'from-blue-500/20 to-cyan-500/10',
          border: 'border-blue-500/30',
          icon: <TrendingUp size={24} className="text-blue-500" />,
          accentColor: '#3b82f6',
        };
    }
  };

  const style = getNotificationStyle();

  return (
    <div
      className={`
        fixed
        ${position === 'top' ? 'top-[var(--premium-space-lg)]' : 'bottom-[var(--premium-space-lg)]'}
        right-[var(--premium-space-lg)]
        z-[100]
        w-full
        max-w-[400px]
        transition-all
        duration-300
        ${isVisible && !isExiting
          ? 'translate-x-0 opacity-100'
          : 'translate-x-[120%] opacity-0'
        }
      `}
    >
      <div
        className={`
          relative
          rounded-[var(--premium-radius-xl)]
          p-[var(--premium-space-lg)]
          bg-gradient-to-r ${style.bg}
          backdrop-blur-xl
          border ${style.border}
          shadow-[var(--premium-shadow-2xl)]
          overflow-hidden
        `}
      >
        {/* Animated left accent bar */}
        <div
          className="absolute left-0 top-0 bottom-0 w-[4px]"
          style={{ backgroundColor: style.accentColor }}
        />

        {/* Animated progress bar (auto-dismiss indicator) */}
        {autoDismissMs > 0 && (
          <div className="absolute bottom-0 left-0 right-0 h-[2px] bg-white/10">
            <div
              className="h-full bg-white/30"
              style={{
                animation: `shrink ${autoDismissMs}ms linear`,
              }}
            />
          </div>
        )}

        <div className="flex gap-[var(--premium-space-md)]">
          {/* Category Icon */}
          <div
            className="w-[48px] h-[48px] rounded-[var(--premium-radius-md)] flex items-center justify-center flex-shrink-0"
            style={{
              background: `linear-gradient(135deg, ${categoryMeta?.colorToken}40, ${categoryMeta?.colorToken}20)`,
            }}
          >
            <CategoryIcon category={notification.category} size={24} />
          </div>

          {/* Content */}
          <div className="flex-1 min-w-0">
            {/* Header */}
            <div className="flex items-start justify-between gap-[var(--premium-space-sm)] mb-[var(--premium-space-xs)]">
              <div className="flex items-center gap-[var(--premium-space-sm)]">
                {style.icon}
                <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                  {notification.categoryLabel}
                </p>
              </div>
              
              <button
                onClick={handleDismiss}
                className="
                  w-[24px] h-[24px]
                  rounded-full
                  flex items-center justify-center
                  hover:bg-white/10
                  transition-all
                  flex-shrink-0
                "
              >
                <X size={16} className="text-[var(--premium-text-secondary)]" />
              </button>
            </div>

            {/* Message */}
            <p className="body-sm text-[var(--premium-text-secondary)] mb-[var(--premium-space-sm)]">
              {notification.message}
            </p>

            {/* Stats */}
            <div className="flex items-center gap-[var(--premium-space-lg)]">
              <div>
                <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">
                  Spent
                </p>
                <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                  ${notification.spent.toLocaleString()}
                </p>
              </div>
              
              <div>
                <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">
                  Budget
                </p>
                <p className="body-md font-semibold text-[var(--premium-text-primary)]">
                  ${notification.budget.toLocaleString()}
                </p>
              </div>
              
              <div>
                <p className="body-xs text-[var(--premium-text-tertiary)] mb-[2px]">
                  Remaining
                </p>
                <p
                  className="body-md font-semibold"
                  style={{
                    color: notification.remaining > 0 ? style.accentColor : '#ef4444',
                  }}
                >
                  ${Math.abs(notification.remaining).toLocaleString()}
                </p>
              </div>
            </div>

            {/* Progress Bar */}
            <div className="mt-[var(--premium-space-sm)]">
              <div className="flex justify-between items-center mb-[4px]">
                <p className="body-xs text-[var(--premium-text-tertiary)]">
                  {notification.percentage}% used
                </p>
              </div>
              
              <div className="w-full h-[6px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
                <div
                  className="h-full rounded-full transition-all duration-500"
                  style={{
                    width: `${Math.min(notification.percentage, 100)}%`,
                    background: `linear-gradient(to right, ${style.accentColor}, ${style.accentColor}cc)`,
                  }}
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <style>{`
        @keyframes shrink {
          from {
            width: 100%;
          }
          to {
            width: 0%;
          }
        }
      `}</style>
    </div>
  );
}

/**
 * Budget Notification Container
 * Manages multiple notifications with stacking
 */
export interface PremiumBudgetNotificationContainerProps {
  /** Array of notifications to display */
  notifications: BudgetNotification[];
  /** Callback when a notification is dismissed */
  onDismiss: (id: string) => void;
  /** Maximum number of notifications to show at once */
  maxVisible?: number;
  /** Auto-dismiss duration in ms (0 = no auto-dismiss) */
  autoDismissMs?: number;
  /** Position of notifications */
  position?: 'top' | 'bottom';
}

export function PremiumBudgetNotificationContainer({
  notifications,
  onDismiss,
  maxVisible = 3,
  autoDismissMs = 5000,
  position = 'top',
}: PremiumBudgetNotificationContainerProps) {
  // Show only the most recent notifications up to maxVisible
  const visibleNotifications = notifications.slice(0, maxVisible);

  return (
    <>
      {visibleNotifications.map((notification, index) => (
        <div
          key={notification.id}
          style={{
            transform: position === 'top'
              ? `translateY(${index * 110}px)`
              : `translateY(-${index * 110}px)`,
          }}
        >
          <PremiumBudgetNotification
            notification={notification}
            onDismiss={onDismiss}
            autoDismissMs={autoDismissMs}
            position={position}
          />
        </div>
      ))}
    </>
  );
}

/**
 * Helper function to generate budget notifications based on thresholds
 */
export function generateBudgetNotification(
  category: TransactionCategory,
  categoryLabel: string,
  spent: number,
  budget: number,
  threshold: number,
): BudgetNotification | null {
  const percentage = Math.round((spent / budget) * 100);
  const remaining = budget - spent;

  // Only generate notification if at or above threshold
  if (percentage < threshold) return null;

  let type: BudgetNotificationType;
  let message: string;

  if (percentage >= 100) {
    type = 'critical';
    message = `You've exceeded your budget by $${Math.abs(remaining).toLocaleString()}!`;
  } else if (percentage >= 90) {
    type = 'critical';
    message = `You're ${percentage}% through your budget. Only $${remaining.toLocaleString()} left!`;
  } else if (percentage >= threshold) {
    type = 'warning';
    message = `You've used ${percentage}% of your budget. $${remaining.toLocaleString()} remaining.`;
  } else {
    return null;
  }

  return {
    id: `budget-${category}-${Date.now()}`,
    type,
    category,
    categoryLabel,
    percentage,
    spent,
    budget,
    remaining,
    message,
    timestamp: new Date(),
  };
}

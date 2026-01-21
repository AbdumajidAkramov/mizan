/**
 * Premium Emergency Fund Tracker
 * Goal-based progress visualization
 */

import { Shield, Target, TrendingUp } from 'lucide-react';
import { PremiumCard } from './PremiumCard';

export interface PremiumEmergencyFundProps {
  /** Current emergency fund amount */
  current: number;
  /** Target goal amount */
  goal: number;
  /** Recommended months of expenses */
  targetMonths?: number;
}

export function PremiumEmergencyFund({
  current,
  goal,
  targetMonths = 6,
}: PremiumEmergencyFundProps) {
  const percentage = Math.min((current / goal) * 100, 100);
  const remaining = Math.max(goal - current, 0);
  const isComplete = current >= goal;

  return (
    <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
      <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <Shield size={20} className="text-[#4facfe]" />
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Emergency Fund
          </h3>
        </div>
        <div className="
          px-[var(--premium-space-sm)] py-[4px]
          bg-[#4facfe]/20
          rounded-full
        ">
          <span className="body-xs text-[#4facfe] font-medium">
            {targetMonths} months
          </span>
        </div>
      </div>

      {/* Progress visualization */}
      <div className="mb-[var(--premium-space-md)]">
        <div className="flex items-baseline justify-between mb-[var(--premium-space-sm)]">
          <div>
            <h4 className="heading-lg text-[var(--premium-text-primary)]">
              ${current.toLocaleString()}
            </h4>
            <p className="body-sm text-[var(--premium-text-tertiary)]">
              of ${goal.toLocaleString()} goal
            </p>
          </div>
          <div className="text-right">
            <p className="heading-md text-[#4facfe]">
              {Math.round(percentage)}%
            </p>
            <p className="body-xs text-[var(--premium-text-muted)]">complete</p>
          </div>
        </div>

        {/* Multi-segment progress bar */}
        <div className="relative h-[12px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
          {/* Main progress */}
          <div
            className="absolute h-full rounded-full transition-all duration-1000 ease-out"
            style={{
              width: `${percentage}%`,
              background: isComplete
                ? 'linear-gradient(to right, var(--premium-success), #4facfe)'
                : 'linear-gradient(to right, #4facfe, #00f2fe)',
            }}
          />
          
          {/* Milestone markers */}
          {[25, 50, 75].map((milestone) => (
            <div
              key={milestone}
              className="absolute top-0 bottom-0 w-[2px] bg-[var(--premium-surface-1)]"
              style={{ left: `${milestone}%` }}
            />
          ))}

          {/* Glow effect on progress */}
          {!isComplete && (
            <div
              className="absolute h-full rounded-full opacity-50 blur-sm"
              style={{
                width: `${percentage}%`,
                background: 'linear-gradient(to right, #4facfe, #00f2fe)',
              }}
            />
          )}
        </div>
      </div>

      {/* Status message */}
      <div className={`
        flex items-start gap-[var(--premium-space-sm)]
        p-[var(--premium-space-md)]
        rounded-[var(--premium-radius-md)]
        ${isComplete ? 'bg-[var(--premium-success)]/10' : 'bg-[#4facfe]/10'}
      `}>
        {isComplete ? (
          <>
            <Target size={18} className="text-[var(--premium-success)] flex-shrink-0 mt-[2px]" />
            <div>
              <p className="body-md text-[var(--premium-success)] font-medium mb-[4px]">
                Goal Achieved! 🎉
              </p>
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                You're financially protected for {targetMonths} months
              </p>
            </div>
          </>
        ) : (
          <>
            <TrendingUp size={18} className="text-[#4facfe] flex-shrink-0 mt-[2px]" />
            <div>
              <p className="body-md text-[var(--premium-text-primary)] font-medium mb-[4px]">
                ${remaining.toLocaleString()} remaining
              </p>
              <p className="body-sm text-[var(--premium-text-tertiary)]">
                Save ~${Math.round(remaining / 12)}/month to reach goal in 1 year
              </p>
            </div>
          </>
        )}
      </div>
    </PremiumCard>
  );
}

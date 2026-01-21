/**
 * Premium Financial Health Score Gauge
 * Visual indicator of overall financial health (0-100)
 */

import { useState, useEffect } from 'react';
import { TrendingUp, Shield, AlertCircle } from 'lucide-react';
import { PremiumCard } from './PremiumCard';

export interface PremiumHealthScoreProps {
  /** Current health score (0-100) */
  score: number;
  /** Trend from previous month */
  trend?: number;
  /** Loading state */
  isLoading?: boolean;
}

export function PremiumHealthScore({ score, trend = 0, isLoading }: PremiumHealthScoreProps) {
  const [animatedScore, setAnimatedScore] = useState(0);

  // Animate score on mount
  useEffect(() => {
    const timer = setTimeout(() => {
      setAnimatedScore(score);
    }, 200);
    return () => clearTimeout(timer);
  }, [score]);

  // Calculate gauge rotation (0-180 degrees)
  const rotation = (animatedScore / 100) * 180;

  // Determine score category
  const getScoreCategory = (score: number) => {
    if (score >= 80) return { label: 'Excellent', color: 'var(--premium-success)', emoji: '🌟' };
    if (score >= 60) return { label: 'Good', color: '#4facfe', emoji: '👍' };
    if (score >= 40) return { label: 'Fair', color: 'var(--premium-warning)', emoji: '⚠️' };
    return { label: 'Needs Attention', color: 'var(--premium-error)', emoji: '🚨' };
  };

  const category = getScoreCategory(score);

  return (
    <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
      <div className="flex items-center justify-between mb-[var(--premium-space-md)]">
        <div className="flex items-center gap-[var(--premium-space-sm)]">
          <Shield size={20} className="text-[var(--premium-primary)]" />
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Financial Health
          </h3>
        </div>
        {trend !== 0 && (
          <div className={`
            flex items-center gap-[4px]
            px-[var(--premium-space-sm)] py-[4px]
            rounded-full
            ${trend > 0 ? 'bg-[var(--premium-success)]/20' : 'bg-[var(--premium-error)]/20'}
          `}>
            <TrendingUp
              size={14}
              className={trend > 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)] rotate-180'}
            />
            <span className={`body-xs font-medium ${trend > 0 ? 'text-[var(--premium-success)]' : 'text-[var(--premium-error)]'}`}>
              {Math.abs(trend)}%
            </span>
          </div>
        )}
      </div>

      {/* Gauge Container */}
      <div className="relative flex flex-col items-center py-[var(--premium-space-lg)]">
        {/* Semi-circle gauge background */}
        <div className="relative w-[200px] h-[100px]">
          {/* Background arc */}
          <svg className="absolute inset-0" viewBox="0 0 200 100">
            <defs>
              <linearGradient id="gaugeGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                <stop offset="0%" stopColor="var(--premium-error)" />
                <stop offset="50%" stopColor="var(--premium-warning)" />
                <stop offset="100%" stopColor="var(--premium-success)" />
              </linearGradient>
            </defs>
            {/* Background track */}
            <path
              d="M 10 90 A 90 90 0 0 1 190 90"
              fill="none"
              stroke="var(--premium-surface-2)"
              strokeWidth="12"
              strokeLinecap="round"
            />
            {/* Gradient progress arc */}
            <path
              d="M 10 90 A 90 90 0 0 1 190 90"
              fill="none"
              stroke="url(#gaugeGradient)"
              strokeWidth="12"
              strokeLinecap="round"
              strokeDasharray={`${(animatedScore / 100) * 283} 283`}
              style={{ transition: 'stroke-dasharray 1s ease-out' }}
            />
          </svg>

          {/* Needle */}
          <div
            className="absolute left-1/2 bottom-0 w-[3px] h-[80px] origin-bottom transition-transform duration-1000 ease-out"
            style={{
              transform: `translateX(-50%) rotate(${rotation - 90}deg)`,
            }}
          >
            <div className="w-full h-full bg-gradient-to-t from-white to-transparent rounded-full" />
            <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-[10px] h-[10px] bg-white rounded-full shadow-lg" />
          </div>

          {/* Center dot */}
          <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-[16px] h-[16px] bg-[var(--premium-surface-1)] rounded-full border-2 border-[var(--premium-surface-3)]" />
        </div>

        {/* Score Display */}
        <div className="text-center mt-[var(--premium-space-md)]">
          <div className="flex items-baseline justify-center gap-[4px] mb-[var(--premium-space-xs)]">
            <span className="display-md text-[var(--premium-text-primary)]">
              {Math.round(animatedScore)}
            </span>
            <span className="heading-sm text-[var(--premium-text-muted)]">/100</span>
          </div>
          <div
            className="inline-flex items-center gap-[var(--premium-space-xs)] px-[var(--premium-space-md)] py-[var(--premium-space-xs)] rounded-full"
            style={{ backgroundColor: `${category.color}20` }}
          >
            <span className="text-[16px]">{category.emoji}</span>
            <span className="body-md font-medium" style={{ color: category.color }}>
              {category.label}
            </span>
          </div>
        </div>
      </div>

      {/* Quick insights */}
      <div className="mt-[var(--premium-space-md)] pt-[var(--premium-space-md)] border-t border-[var(--premium-surface-2)]">
        <p className="body-sm text-[var(--premium-text-tertiary)] text-center">
          Based on spending habits, savings rate, and budget adherence
        </p>
      </div>
    </PremiumCard>
  );
}

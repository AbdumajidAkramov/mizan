/**
 * Premium AI Insights Section
 * Scrollable smart financial tips powered by AI
 */

import { Sparkles, TrendingUp, AlertTriangle, Lightbulb, Target } from 'lucide-react';
import { PremiumCard } from './PremiumCard';

export interface AIInsight {
  id: string;
  type: 'success' | 'warning' | 'info' | 'goal';
  title: string;
  description: string;
  impact?: string;
}

export interface PremiumAIInsightsProps {
  insights?: AIInsight[];
}

const DEFAULT_INSIGHTS: AIInsight[] = [
  {
    id: '1',
    type: 'success',
    title: 'Great savings momentum!',
    description: 'You saved 15% more than last month. Keep it up!',
    impact: '+$245',
  },
  {
    id: '2',
    type: 'warning',
    title: 'Subscription Alert',
    description: 'You have 3 recurring subscriptions totaling $47/mo. Review for potential savings.',
    impact: '$47/mo',
  },
  {
    id: '3',
    type: 'info',
    title: 'Smart Spending Detected',
    description: 'Your Food spending is 12% below average. Well done!',
    impact: '-12%',
  },
  {
    id: '4',
    type: 'goal',
    title: 'Emergency Fund Progress',
    description: 'You\'re 67% of the way to your emergency fund goal. Stay consistent!',
    impact: '67%',
  },
  {
    id: '5',
    type: 'info',
    title: 'Tax Season Tip',
    description: 'You have $2,340 in deductible expenses this year.',
    impact: '$2,340',
  },
];

export function PremiumAIInsights({ insights = DEFAULT_INSIGHTS }: PremiumAIInsightsProps) {
  const getInsightIcon = (type: AIInsight['type']) => {
    switch (type) {
      case 'success':
        return <TrendingUp size={18} className="text-[var(--premium-success)]" />;
      case 'warning':
        return <AlertTriangle size={18} className="text-[var(--premium-warning)]" />;
      case 'goal':
        return <Target size={18} className="text-[#4facfe]" />;
      default:
        return <Lightbulb size={18} className="text-[var(--premium-primary)]" />;
    }
  };

  const getInsightColor = (type: AIInsight['type']) => {
    switch (type) {
      case 'success':
        return 'var(--premium-success)';
      case 'warning':
        return 'var(--premium-warning)';
      case 'goal':
        return '#4facfe';
      default:
        return 'var(--premium-primary)';
    }
  };

  return (
    <div>
      <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
        <Sparkles size={20} className="text-[var(--premium-primary)]" />
        <h3 className="heading-md text-[var(--premium-text-primary)]">
          AI Insights
        </h3>
        <div className="
          px-[var(--premium-space-sm)] py-[2px]
          bg-gradient-to-r from-[#667eea] to-[#764ba2]
          rounded-full
        ">
          <span className="body-xs text-white font-medium">Smart</span>
        </div>
      </div>

      {/* Horizontal scrollable insights */}
      <div 
        className="flex gap-[var(--premium-space-md)] overflow-x-auto pb-[var(--premium-space-sm)] -mx-[var(--premium-space-md)] px-[var(--premium-space-md)]"
        style={{
          scrollbarWidth: 'none',
          msOverflowStyle: 'none',
          WebkitOverflowScrolling: 'touch',
        }}
      >
        {insights.map((insight) => {
          const color = getInsightColor(insight.type);
          
          return (
            <PremiumCard
              key={insight.id}
              variant="glass"
              className="flex-shrink-0 w-[280px] p-[var(--premium-space-md)]"
            >
              <div className="flex items-start gap-[var(--premium-space-sm)] mb-[var(--premium-space-sm)]">
                <div
                  className="w-[32px] h-[32px] rounded-full flex items-center justify-center flex-shrink-0"
                  style={{ backgroundColor: `${color}20` }}
                >
                  {getInsightIcon(insight.type)}
                </div>
                {insight.impact && (
                  <div
                    className="ml-auto px-[var(--premium-space-sm)] py-[4px] rounded-full"
                    style={{ backgroundColor: `${color}20` }}
                  >
                    <span className="body-xs font-medium" style={{ color }}>
                      {insight.impact}
                    </span>
                  </div>
                )}
              </div>

              <h4 className="body-md text-[var(--premium-text-primary)] font-medium mb-[var(--premium-space-xs)]">
                {insight.title}
              </h4>
              <p 
                className="body-sm text-[var(--premium-text-tertiary)] overflow-hidden"
                style={{
                  display: '-webkit-box',
                  WebkitLineClamp: 2,
                  WebkitBoxOrient: 'vertical',
                }}
              >
                {insight.description}
              </p>
            </PremiumCard>
          );
        })}
      </div>

      <style>{`
        .flex.gap-\\[var\\(--premium-space-md\\)\\].overflow-x-auto::-webkit-scrollbar {
          display: none;
        }
      `}</style>
    </div>
  );
}
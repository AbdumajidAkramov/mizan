/**
 * Premium Financial Mirror Screen
 * Advanced analytics: Net Worth Projection, Risk Analysis, Time Machine, Investment Opportunities
 */

import { useState } from 'react';
import { PremiumCard } from '../components/premium/PremiumCard';
import { PremiumButton } from '../components/premium/PremiumButton';
import {
  TrendingUp,
  AlertTriangle,
  Target,
  Clock,
  Sparkles,
  ArrowRight,
  DollarSign,
  Shield,
  Zap,
} from 'lucide-react';
import { LineChart, Line, Area, AreaChart, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';

// Net Worth Projection Data (5 years)
const NET_WORTH_PROJECTION = [
  { year: '2026', conservative: 22450, realistic: 22450, optimistic: 22450 },
  { year: '2027', conservative: 28500, realistic: 32400, optimistic: 38200 },
  { year: '2028', conservative: 34200, realistic: 45800, optimistic: 62500 },
  { year: '2029', conservative: 39800, realistic: 62100, optimistic: 95800 },
  { year: '2030', conservative: 45200, realistic: 82500, optimistic: 142000 },
  { year: '2031', conservative: 50400, realistic: 108200, optimistic: 208500 },
];

// Financial Risk Data
const RISK_FACTORS = [
  { category: 'Emergency Fund', score: 85, status: 'good', description: 'Strong 6-month coverage' },
  { category: 'Debt-to-Income', score: 72, status: 'fair', description: 'Manageable debt levels' },
  { category: 'Diversification', score: 45, status: 'warning', description: 'Needs improvement' },
  { category: 'Insurance Coverage', score: 90, status: 'good', description: 'Well protected' },
];

// Time Machine Scenarios
const TIME_MACHINE_SCENARIOS = [
  {
    id: '1',
    title: 'If you saved $500/month',
    timeline: '5 years',
    impact: '+$38,250',
    description: 'With 5% annual return',
    icon: Target,
  },
  {
    id: '2',
    title: 'Cut subscriptions by 50%',
    timeline: '1 year',
    impact: '+$282',
    description: 'Save $23.50 monthly',
    icon: TrendingUp,
  },
  {
    id: '3',
    title: 'Invest $200/month in index',
    timeline: '10 years',
    impact: '+$32,840',
    description: 'Assuming 7% return',
    icon: Sparkles,
  },
];

// Investment Opportunities (AI Curated)
const INVESTMENT_OPPORTUNITIES = [
  {
    id: '1',
    title: 'High-Yield Savings',
    type: 'Low Risk',
    apy: '4.5%',
    minAmount: '$100',
    description: 'FDIC insured, instant access',
    color: 'var(--premium-success)',
  },
  {
    id: '2',
    title: 'Index Fund ETF',
    type: 'Medium Risk',
    apy: '7-10%',
    minAmount: '$500',
    description: 'Diversified market exposure',
    color: '#4facfe',
  },
  {
    id: '3',
    title: 'Retirement 401(k)',
    type: 'Long-term',
    apy: '8-12%',
    minAmount: '$50',
    description: 'Employer match available',
    color: 'var(--premium-primary)',
  },
];

export function PremiumFinancialMirrorScreen() {
  const [projectionView, setProjectionView] = useState<'conservative' | 'realistic' | 'optimistic'>('realistic');

  return (
    <div className="flex flex-col gap-[var(--premium-space-lg)] animate-fade-in-up">
      {/* Header */}
      <div>
        <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-xs)]">
          <Sparkles size={24} className="text-[var(--premium-primary)]" />
          <h1 className="heading-xl text-[var(--premium-text-primary)]">
            Financial Mirror
          </h1>
        </div>
        <p className="body-md text-[var(--premium-text-tertiary)]">
          AI-powered insights into your financial future
        </p>
      </div>

      {/* Net Worth Projection */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="mb-[var(--premium-space-lg)]">
          <h3 className="heading-md text-[var(--premium-text-primary)] mb-[4px]">
            Net Worth Projection
          </h3>
          <p className="body-sm text-[var(--premium-text-tertiary)]">
            Your potential wealth growth over 5 years
          </p>
        </div>

        {/* Projection Toggle */}
        <div className="flex gap-[var(--premium-space-sm)] mb-[var(--premium-space-lg)]">
          {(['conservative', 'realistic', 'optimistic'] as const).map((view) => (
            <button
              key={view}
              onClick={() => setProjectionView(view)}
              className={`
                flex-1 py-[var(--premium-space-sm)]
                rounded-[var(--premium-radius-md)]
                body-sm font-medium
                transition-all
                ${projectionView === view
                  ? 'bg-gradient-to-r from-[#667eea] to-[#764ba2] text-white'
                  : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-tertiary)] hover:bg-[var(--premium-surface-3)]'
                }
              `}
            >
              {view.charAt(0).toUpperCase() + view.slice(1)}
            </button>
          ))}
        </div>

        {/* Projection Chart */}
        <div className="w-full min-h-[250px]">
          <ResponsiveContainer width="100%" height={250}>
            <AreaChart data={NET_WORTH_PROJECTION}>
              <defs>
                <linearGradient id="netWorthProjection" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="var(--premium-emerald)" stopOpacity={0.3} />
                  <stop offset="100%" stopColor="var(--premium-emerald)" stopOpacity={0} />
                </linearGradient>
              </defs>
              <CartesianGrid
                strokeDasharray="3 3"
                stroke="var(--premium-glass-border)"
                vertical={false}
              />
              <XAxis
                dataKey="year"
                axisLine={false}
                tickLine={false}
                tick={{ fontSize: 12, fill: 'var(--premium-text-muted)' }}
              />
              <YAxis
                axisLine={false}
                tickLine={false}
                tick={{ fontSize: 12, fill: 'var(--premium-text-muted)' }}
                tickFormatter={(value) => `$${value / 1000}k`}
              />
              <Tooltip
                contentStyle={{
                  background: 'var(--premium-glass-bg)',
                  backdropFilter: 'blur(20px)',
                  border: '1px solid var(--premium-glass-border)',
                  borderRadius: 'var(--premium-radius-md)',
                }}
              />
              <Area
                type="monotone"
                dataKey="value"
                stroke="var(--premium-emerald)"
                strokeWidth={3}
                fill="url(#netWorthProjection)"
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>

        {/* Projection Summary */}
        <div className="mt-[var(--premium-space-lg)] grid grid-cols-3 gap-[var(--premium-space-md)]">
          <div className="text-center">
            <p className="body-xs text-[var(--premium-text-muted)] mb-[4px]">Starting</p>
            <p className="heading-sm text-[var(--premium-text-primary)]">$22.5k</p>
          </div>
          <div className="text-center">
            <p className="body-xs text-[var(--premium-text-muted)] mb-[4px]">5-Year Target</p>
            <p className="heading-sm text-[var(--premium-primary)]">
              ${(NET_WORTH_PROJECTION[5][projectionView] / 1000).toFixed(1)}k
            </p>
          </div>
          <div className="text-center">
            <p className="body-xs text-[var(--premium-text-muted)] mb-[4px]">Total Growth</p>
            <p className="heading-sm text-[var(--premium-success)]">
              +{Math.round(((NET_WORTH_PROJECTION[5][projectionView] - 22450) / 22450) * 100)}%
            </p>
          </div>
        </div>
      </PremiumCard>

      {/* Financial Risk Analysis */}
      <PremiumCard variant="glass" className="p-[var(--premium-space-lg)]">
        <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-lg)]">
          <Shield size={20} className="text-[var(--premium-warning)]" />
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Financial Risk Assessment
          </h3>
        </div>

        <div className="space-y-[var(--premium-space-md)]">
          {RISK_FACTORS.map((risk) => {
            const isGood = risk.status === 'good';
            const isFair = risk.status === 'fair';
            const color = isGood
              ? 'var(--premium-success)'
              : isFair
              ? '#4facfe'
              : 'var(--premium-warning)';

            return (
              <div key={risk.category}>
                <div className="flex items-center justify-between mb-[var(--premium-space-sm)]">
                  <div>
                    <p className="body-md text-[var(--premium-text-primary)] font-medium">
                      {risk.category}
                    </p>
                    <p className="body-sm text-[var(--premium-text-tertiary)]">
                      {risk.description}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="heading-sm" style={{ color }}>
                      {risk.score}
                    </p>
                    <p className="body-xs text-[var(--premium-text-muted)]">/ 100</p>
                  </div>
                </div>
                <div className="h-[6px] bg-[var(--premium-surface-2)] rounded-full overflow-hidden">
                  <div
                    className="h-full rounded-full transition-all duration-500"
                    style={{
                      width: `${risk.score}%`,
                      backgroundColor: color,
                    }}
                  />
                </div>
              </div>
            );
          })}
        </div>

        {/* Overall Risk Score */}
        <div className="mt-[var(--premium-space-lg)] p-[var(--premium-space-md)] bg-[var(--premium-surface-2)] rounded-[var(--premium-radius-md)]">
          <div className="flex items-center justify-between">
            <div>
              <p className="body-sm text-[var(--premium-text-tertiary)] mb-[4px]">
                Overall Risk Score
              </p>
              <p className="heading-lg text-[var(--premium-text-primary)]">
                73/100
              </p>
            </div>
            <div className="
              px-[var(--premium-space-md)] py-[var(--premium-space-sm)]
              bg-[#4facfe]/20
              rounded-full
            ">
              <span className="body-md text-[#4facfe] font-medium">Moderate</span>
            </div>
          </div>
        </div>
      </PremiumCard>

      {/* Time Machine - What If Scenarios */}
      <div>
        <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
          <Clock size={20} className="text-[var(--premium-primary)]" />
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Time Machine
          </h3>
          <div className="
            px-[var(--premium-space-sm)] py-[2px]
            bg-gradient-to-r from-[#667eea] to-[#764ba2]
            rounded-full
          ">
            <span className="body-xs text-white font-medium">What If?</span>
          </div>
        </div>

        <div className="space-y-[var(--premium-space-md)]">
          {TIME_MACHINE_SCENARIOS.map((scenario) => {
            const Icon = scenario.icon;
            return (
              <PremiumCard
                key={scenario.id}
                variant="glass"
                hover
                className="p-[var(--premium-space-lg)]"
              >
                <div className="flex items-start gap-[var(--premium-space-md)]">
                  <div className="
                    w-[48px] h-[48px]
                    bg-gradient-to-br from-[#667eea] to-[#764ba2]
                    rounded-[var(--premium-radius-md)]
                    flex items-center justify-center
                    flex-shrink-0
                  ">
                    <Icon size={24} className="text-white" />
                  </div>

                  <div className="flex-1">
                    <h4 className="heading-sm text-[var(--premium-text-primary)] mb-[4px]">
                      {scenario.title}
                    </h4>
                    <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-sm)]">
                      {scenario.description}
                    </p>
                    <div className="flex items-center gap-[var(--premium-space-md)]">
                      <div className="
                        px-[var(--premium-space-sm)] py-[4px]
                        bg-[var(--premium-success)]/20
                        rounded-full
                      ">
                        <span className="body-xs text-[var(--premium-success)] font-medium">
                          {scenario.impact}
                        </span>
                      </div>
                      <span className="body-xs text-[var(--premium-text-muted)]">
                        in {scenario.timeline}
                      </span>
                    </div>
                  </div>

                  <ArrowRight size={20} className="text-[var(--premium-text-muted)] flex-shrink-0" />
                </div>
              </PremiumCard>
            );
          })}
        </div>
      </div>

      {/* Investment Opportunities */}
      <div>
        <div className="flex items-center gap-[var(--premium-space-sm)] mb-[var(--premium-space-md)]">
          <Zap size={20} className="text-[var(--premium-warning)]" />
          <h3 className="heading-md text-[var(--premium-text-primary)]">
            Investment Opportunities
          </h3>
          <div className="
            px-[var(--premium-space-sm)] py-[2px]
            bg-[var(--premium-warning)]/20
            rounded-full
          ">
            <span className="body-xs text-[var(--premium-warning)] font-medium">AI Curated</span>
          </div>
        </div>

        <div className="space-y-[var(--premium-space-md)]">
          {INVESTMENT_OPPORTUNITIES.map((opportunity) => (
            <PremiumCard
              key={opportunity.id}
              variant="glass"
              className="p-[var(--premium-space-lg)]"
            >
              <div className="flex items-start justify-between mb-[var(--premium-space-md)]">
                <div>
                  <h4 className="heading-md text-[var(--premium-text-primary)] mb-[4px]">
                    {opportunity.title}
                  </h4>
                  <div className="flex items-center gap-[var(--premium-space-sm)]">
                    <div
                      className="px-[var(--premium-space-sm)] py-[2px] rounded-full"
                      style={{ backgroundColor: `${opportunity.color}20` }}
                    >
                      <span className="body-xs font-medium" style={{ color: opportunity.color }}>
                        {opportunity.type}
                      </span>
                    </div>
                    <span className="body-sm text-[var(--premium-text-tertiary)]">
                      Min: {opportunity.minAmount}
                    </span>
                  </div>
                </div>

                <div className="text-right">
                  <p className="heading-lg" style={{ color: opportunity.color }}>
                    {opportunity.apy}
                  </p>
                  <p className="body-xs text-[var(--premium-text-muted)]">APY</p>
                </div>
              </div>

              <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-md)]">
                {opportunity.description}
              </p>

              <PremiumButton
                variant="gradient-primary"
                size="md"
                fullWidth
                icon={<ArrowRight size={18} />}
                iconPosition="right"
              >
                Learn More
              </PremiumButton>
            </PremiumCard>
          ))}
        </div>
      </div>

      {/* AI Recommendation */}
      <PremiumCard
        variant="glass"
        className="p-[var(--premium-space-lg)] bg-gradient-to-br from-[#667eea]/10 to-[#764ba2]/10"
      >
        <div className="flex gap-[var(--premium-space-md)]">
          <div className="text-[32px]">🤖</div>
          <div>
            <h4 className="heading-sm text-[var(--premium-text-primary)] mb-[var(--premium-space-sm)]">
              AI Recommendation
            </h4>
            <p className="body-md text-[var(--premium-text-secondary)] mb-[var(--premium-space-md)]">
              Based on your spending patterns and goals, we recommend:
            </p>
            <ul className="space-y-[var(--premium-space-xs)]">
              <li className="body-sm text-[var(--premium-text-tertiary)]">
                • Increase emergency fund by $200/month
              </li>
              <li className="body-sm text-[var(--premium-text-tertiary)]">
                • Start investing in index funds with $150/month
              </li>
              <li className="body-sm text-[var(--premium-text-tertiary)]">
                • Review and cancel unused subscriptions
              </li>
            </ul>
          </div>
        </div>
      </PremiumCard>
    </div>
  );
}
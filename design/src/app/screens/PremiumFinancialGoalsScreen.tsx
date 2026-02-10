/**
 * Premium Financial Goals Screen
 * Track and achieve financial goals with behavioral economics principles
 * 
 * @features
 * - Overall savings progress with sky blue glow
 * - Active goals list with progress bars
 * - Goal icons (Home, Car, Plane, GraduationCap, Trophy)
 * - Target dates and percentage tracking
 * - Color transitions (Sky Blue → Emerald for completed)
 * - Motivational progress badges
 * - Add new goal action
 * 
 * @architecture Material 3 with glassmorphism, behavioral design
 * @design Deep dark blue/purple (#1A1A2E) background, 8dp grid system
 */

import { useState } from 'react';
import {
  ChevronLeft,
  Plus,
  Home,
  Car,
  Plane,
  GraduationCap,
  Trophy,
  Sparkles,
  Target,
  Calendar,
  TrendingUp,
} from 'lucide-react';

/**
 * Financial Goal Interface
 */
export interface FinancialGoal {
  id: string;
  title: string;
  description?: string;
  savedAmount: number; // Current amount saved (in UZS)
  targetAmount: number; // Goal target (in UZS)
  targetDate: Date;
  icon: typeof Home;
  iconColor: string;
  category: string;
  createdAt: Date;
}

/**
 * Financial Goals State Interface
 */
export interface FinancialGoalsState {
  goals: FinancialGoal[];
  totalSaved: number;
  totalTarget: number;
  overallProgress: number; // 0-100
}

export interface PremiumFinancialGoalsScreenProps {
  /** Callback when back button is pressed */
  onBack: () => void;
  /** Callback when goal card is tapped */
  onGoalDetail?: (goalId: string) => void;
  /** Callback to navigate to create goal screen */
  onNavigateToCreateGoal?: () => void;
}

// Mock Financial Goals Data
const MOCK_GOALS: FinancialGoal[] = [
  {
    id: 'goal-1',
    title: 'Dream Home Down Payment',
    description: 'Save for 20% down payment',
    savedAmount: 22500000,
    targetAmount: 30000000,
    targetDate: new Date('2027-12-31'),
    icon: Home,
    iconColor: '#0EA5E9',
    category: 'Housing',
    createdAt: new Date('2024-01-15'),
  },
  {
    id: 'goal-2',
    title: 'New Car',
    description: 'Tesla Model 3',
    savedAmount: 8000000,
    targetAmount: 20000000,
    targetDate: new Date('2026-06-30'),
    icon: Car,
    iconColor: '#8B5CF6',
    category: 'Transportation',
    createdAt: new Date('2024-03-01'),
  },
  {
    id: 'goal-3',
    title: 'Europe Vacation',
    description: 'Summer trip 2026',
    savedAmount: 4500000,
    targetAmount: 5000000,
    targetDate: new Date('2026-08-15'),
    icon: Plane,
    iconColor: '#F59E0B',
    category: 'Travel',
    createdAt: new Date('2025-01-10'),
  },
  {
    id: 'goal-4',
    title: 'Emergency Fund',
    description: '6 months of expenses',
    savedAmount: 15000000,
    targetAmount: 15000000,
    targetDate: new Date('2025-12-31'),
    icon: Trophy,
    iconColor: '#10B981',
    category: 'Safety Net',
    createdAt: new Date('2024-01-01'),
  },
];

/**
 * Calculate financial goals state
 */
function calculateGoalsState(goals: FinancialGoal[]): FinancialGoalsState {
  const totalSaved = goals.reduce((sum, goal) => sum + goal.savedAmount, 0);
  const totalTarget = goals.reduce((sum, goal) => sum + goal.targetAmount, 0);
  const overallProgress = totalTarget > 0 ? (totalSaved / totalTarget) * 100 : 0;

  return {
    goals,
    totalSaved,
    totalTarget,
    overallProgress,
  };
}

/**
 * Format number with spaces: "15 000 000"
 */
function formatUZS(amount: number): string {
  return Math.floor(Math.abs(amount))
    .toString()
    .replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
}

/**
 * Format date to readable string
 */
function formatTargetDate(date: Date): string {
  const options: Intl.DateTimeFormatOptions = { month: 'short', year: 'numeric' };
  return date.toLocaleDateString('en-US', options);
}

/**
 * Calculate goal progress percentage
 */
function getGoalProgress(goal: FinancialGoal): number {
  return Math.min(100, (goal.savedAmount / goal.targetAmount) * 100);
}

/**
 * Determine if goal is completed
 */
function isGoalCompleted(goal: FinancialGoal): boolean {
  return goal.savedAmount >= goal.targetAmount;
}

/**
 * Get progress color based on completion
 */
function getProgressColor(progress: number, isCompleted: boolean): string {
  if (isCompleted || progress >= 100) {
    return '#10B981'; // Emerald for completed
  }
  return '#0EA5E9'; // Sky Blue for in-progress
}

/**
 * Get days until target date
 */
function getDaysUntilTarget(targetDate: Date): number {
  const today = new Date();
  const diffTime = targetDate.getTime() - today.getTime();
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

/**
 * Goal Card Component
 */
function GoalCard({
  goal,
  onClick,
}: {
  goal: FinancialGoal;
  onClick: () => void;
}) {
  const Icon = goal.icon;
  const progress = getGoalProgress(goal);
  const completed = isGoalCompleted(goal);
  const progressColor = getProgressColor(progress, completed);
  const daysUntil = getDaysUntilTarget(goal.targetDate);

  return (
    <button
      onClick={onClick}
      className="
        w-full
        p-[var(--premium-space-lg)]
        rounded-[var(--premium-radius-xl)]
        bg-white/5
        backdrop-blur-xl
        border border-white/10
        hover:bg-white/10
        hover:border-white/20
        transition-all duration-200
        active:scale-[0.98]
        text-left
      "
    >
      {/* Top Row: Icon, Title, Percentage */}
      <div className="flex items-start gap-[var(--premium-space-md)] mb-[12px]">
        {/* Icon */}
        <div
          className="
            w-[48px] h-[48px]
            rounded-[var(--premium-radius-lg)]
            flex items-center justify-center
            flex-shrink-0
          "
          style={{
            backgroundColor: `${goal.iconColor}20`,
            border: `1px solid ${goal.iconColor}30`,
          }}
        >
          <Icon size={24} style={{ color: goal.iconColor }} />
        </div>

        {/* Title & Amount */}
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-[6px] mb-[4px]">
            <h3 className="body-md font-semibold text-white truncate">
              {goal.title}
            </h3>
            {completed && (
              <div
                className="
                  px-[6px] py-[2px]
                  rounded-full
                  bg-[#10B981]/20
                  border border-[#10B981]/30
                "
              >
                <span className="body-xs font-bold text-[#10B981]">✓</span>
              </div>
            )}
          </div>
          <div className="flex items-baseline gap-[4px]">
            <span className="body-sm font-bold text-white">
              {formatUZS(goal.savedAmount)}
            </span>
            <span className="body-xs text-white/40">
              / {formatUZS(goal.targetAmount)} UZS
            </span>
          </div>
        </div>

        {/* Percentage Badge */}
        <div
          className="
            px-[10px] py-[4px]
            rounded-full
            flex-shrink-0
          "
          style={{
            backgroundColor: `${progressColor}20`,
            border: `1px solid ${progressColor}30`,
          }}
        >
          <span className="body-xs font-bold" style={{ color: progressColor }}>
            {Math.round(progress)}%
          </span>
        </div>
      </div>

      {/* Progress Bar */}
      <div className="mb-[8px]">
        <div className="w-full h-[6px] bg-white/10 rounded-full overflow-hidden">
          <div
            className="h-full rounded-full transition-all duration-500 ease-out"
            style={{
              width: `${progress}%`,
              backgroundColor: progressColor,
            }}
          />
        </div>
      </div>

      {/* Footer: Target Date */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-[4px]">
          <Calendar size={12} className="text-white/40" />
          <span className="body-xs text-white/40">
            Target: {formatTargetDate(goal.targetDate)}
          </span>
        </div>
        {daysUntil > 0 && !completed && (
          <span className="body-xs text-white/30">
            {daysUntil} days left
          </span>
        )}
        {completed && (
          <span className="body-xs text-[#10B981] font-medium">
            Goal Achieved! 🎉
          </span>
        )}
      </div>
    </button>
  );
}

/**
 * Premium Financial Goals Screen Component
 */
export function PremiumFinancialGoalsScreen({
  onBack,
  onGoalDetail,
  onNavigateToCreateGoal,
}: PremiumFinancialGoalsScreenProps) {
  const [goals] = useState<FinancialGoal[]>(MOCK_GOALS);
  const goalsState = calculateGoalsState(goals);

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
              Financial Goals
            </h1>
            <p className="body-sm text-white/60">
              Achieve your dreams, one step at a time.
            </p>
          </div>
        </div>
      </div>

      {/* Content - Scrollable */}
      <div className="flex-1 overflow-y-auto px-[var(--premium-space-lg)] pb-[var(--premium-space-2xl)]">
        <div className="space-y-[var(--premium-space-xl)]">
          {/* Overall Progress Card - Premium Glass with Sky Blue Glow */}
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
            {/* Sky Blue Glow Effect */}
            <div
              className="
                absolute -top-[50%] -right-[20%]
                w-[250px] h-[250px]
                rounded-full
                bg-[#0EA5E9]
                opacity-20
                blur-[80px]
              "
            />

            <div className="relative z-10">
              {/* Label */}
              <div className="flex items-center gap-[8px] mb-[var(--premium-space-sm)]">
                <Target size={20} className="text-[#0EA5E9]" />
                <p className="body-sm text-white/60">
                  Total Savings Progress
                </p>
              </div>

              {/* Amount */}
              <div className="flex items-baseline gap-[8px] mb-[var(--premium-space-md)]">
                <span className="text-[28px] font-bold text-white tracking-tight">
                  {formatUZS(goalsState.totalSaved)}
                </span>
                <span className="text-[20px] font-medium text-white/40">
                  / {formatUZS(goalsState.totalTarget)}
                </span>
              </div>
              <p className="body-xs text-white/40 mb-[var(--premium-space-lg)]">
                UZS
              </p>

              {/* Progress Bar */}
              <div className="mb-[var(--premium-space-md)]">
                <div className="w-full h-[12px] bg-white/10 rounded-full overflow-hidden">
                  <div
                    className="h-full rounded-full transition-all duration-500 ease-out"
                    style={{
                      width: `${goalsState.overallProgress}%`,
                      background: 'linear-gradient(90deg, #0EA5E9 0%, #06B6D4 100%)',
                    }}
                  />
                </div>
              </div>

              {/* Motivation Badge */}
              <div className="flex items-center justify-between">
                <div
                  className="
                    inline-flex items-center gap-[6px]
                    px-[12px] py-[6px]
                    rounded-full
                    bg-[#0EA5E9]/20
                    border border-[#0EA5E9]/30
                  "
                >
                  <TrendingUp size={14} className="text-[#0EA5E9]" />
                  <span className="body-xs font-semibold text-[#0EA5E9]">
                    {Math.round(goalsState.overallProgress)}% Achieved
                  </span>
                </div>

                <div className="flex items-center gap-[4px]">
                  <Sparkles size={14} className="text-[#F59E0B]" />
                  <span className="body-xs text-white/50">
                    {goals.length} {goals.length === 1 ? 'Goal' : 'Goals'}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Active Goals List */}
          <div className="space-y-[var(--premium-space-md)]">
            {/* Section Header */}
            <div className="flex items-center justify-between px-[4px]">
              <h3 className="heading-sm text-white/90 font-semibold">
                Active Goals
              </h3>
              <span className="body-xs text-white/40">
                {goals.filter(g => !isGoalCompleted(g)).length} in progress
              </span>
            </div>

            {/* Goal Cards */}
            <div className="space-y-[8px]">
              {goals.map((goal) => (
                <GoalCard
                  key={goal.id}
                  goal={goal}
                  onClick={() => onGoalDetail?.(goal.id)}
                />
              ))}
            </div>
          </div>

          {/* Add New Goal Button - Inline Dashed Style */}
          <button
            onClick={onNavigateToCreateGoal}
            className="
              w-full
              p-[var(--premium-space-md)]
              rounded-[var(--premium-radius-xl)]
              bg-transparent
              border-2 border-dashed border-white/20
              hover:border-[#0EA5E9]/50
              hover:bg-[#0EA5E9]/5
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
                bg-[#0EA5E9]/20
                border border-[#0EA5E9]/30
                flex items-center justify-center
                flex-shrink-0
              "
            >
              <Plus size={24} className="text-[#0EA5E9]" strokeWidth={2.5} />
            </div>

            {/* Text */}
            <div className="flex-1 text-left">
              <p className="body-md font-medium text-white">
                Add New Goal
              </p>
              <p className="body-xs text-white/40">
                Start planning your next achievement
              </p>
            </div>
          </button>

          {/* Motivational Tip Card */}
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
                  bg-[#0EA5E9]/20
                  flex items-center justify-center
                  flex-shrink-0
                "
              >
                <Sparkles size={18} className="text-[#0EA5E9]" />
              </div>
              <div>
                <p className="body-sm font-medium text-white mb-[4px]">
                  Financial Tip
                </p>
                <p className="body-xs text-white/60 leading-relaxed">
                  You're <span className="font-semibold text-[#0EA5E9]">{Math.round(goalsState.overallProgress)}%</span> closer to your dreams! 
                  Consider automating your savings to reach goals faster.
                </p>
              </div>
            </div>
          </div>

          {/* Quick Stats Grid */}
          <div className="grid grid-cols-2 gap-[var(--premium-space-md)]">
            {/* Completed Goals */}
            <div
              className="
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-white/5
                backdrop-blur-xl
                border border-white/10
              "
            >
              <div
                className="
                  w-[40px] h-[40px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#10B981]/20
                  border border-[#10B981]/30
                  flex items-center justify-center
                  mb-[12px]
                "
              >
                <Trophy size={20} className="text-[#10B981]" />
              </div>
              <p className="body-xs text-white/40 mb-[4px]">Completed</p>
              <p className="text-[20px] font-bold text-white mb-[2px]">
                {goals.filter(g => isGoalCompleted(g)).length}
              </p>
              <p className="body-xs text-white/30">
                {goals.filter(g => isGoalCompleted(g)).length === 1 ? 'goal' : 'goals'}
              </p>
            </div>

            {/* Average Progress */}
            <div
              className="
                p-[var(--premium-space-lg)]
                rounded-[var(--premium-radius-xl)]
                bg-white/5
                backdrop-blur-xl
                border border-white/10
              "
            >
              <div
                className="
                  w-[40px] h-[40px]
                  rounded-[var(--premium-radius-lg)]
                  bg-[#0EA5E9]/20
                  border border-[#0EA5E9]/30
                  flex items-center justify-center
                  mb-[12px]
                "
              >
                <TrendingUp size={20} className="text-[#0EA5E9]" />
              </div>
              <p className="body-xs text-white/40 mb-[4px]">Avg. Progress</p>
              <p className="text-[20px] font-bold text-white mb-[2px]">
                {Math.round(goals.reduce((sum, g) => sum + getGoalProgress(g), 0) / goals.length)}%
              </p>
              <p className="body-xs text-white/30">across all goals</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

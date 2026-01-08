/**
 * DashboardScreen
 * Main screen showing financial overview
 */

import { DashboardSummarySection } from '../components/organisms/DashboardSummarySection';
import type { UiState, DashboardSummary } from '../../types/domain';

export interface DashboardScreenProps {
  /** Dashboard data state */
  dashboardState: UiState<DashboardSummary>;
  /** User display name */
  userDisplayName?: string;
}

/**
 * Dashboard Screen Component
 * Entry point for main financial overview
 */
export function DashboardScreen({
  dashboardState,
  userDisplayName,
}: DashboardScreenProps) {
  return (
    <div className="pb-[var(--spacing-3xl)]">
      <DashboardSummarySection
        dashboardState={dashboardState}
        userDisplayName={userDisplayName}
      />
    </div>
  );
}

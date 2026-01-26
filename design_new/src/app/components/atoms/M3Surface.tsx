/**
 * M3 Surface Component
 * Container using Material 3 elevation levels with surface tints
 * Maps 1:1 to Jetpack Compose Surface()
 */

import type { ReactNode } from 'react';

export type M3ElevationLevel = 0 | 1 | 2 | 3 | 4 | 5;
export type M3ShapeSize = 'none' | 'extra-small' | 'small' | 'medium' | 'large' | 'extra-large' | 'full';

export interface M3SurfaceProps {
  /** Surface content */
  children: ReactNode;
  /** Elevation level (0-5) using M3 surface tints */
  elevation?: M3ElevationLevel;
  /** Shape corner radius */
  shape?: M3ShapeSize;
  /** Click handler (makes surface interactive) */
  onClick?: () => void;
  /** Additional CSS classes */
  className?: string;
}

/**
 * M3 Elevation mapping to surface container colors
 * Material 3 uses surface tints instead of shadows
 */
const elevationStyles: Record<M3ElevationLevel, string> = {
  0: 'bg-[var(--md-sys-color-surface)]',
  1: 'bg-[var(--md-sys-color-surface-container-low)]',
  2: 'bg-[var(--md-sys-color-surface-container)]',
  3: 'bg-[var(--md-sys-color-surface-container-high)]',
  4: 'bg-[var(--md-sys-color-surface-container-high)]',
  5: 'bg-[var(--md-sys-color-surface-container-highest)]',
};

/**
 * M3 Shape mapping
 */
const shapeStyles: Record<M3ShapeSize, string> = {
  'none': 'rounded-[var(--md-sys-shape-corner-none)]',
  'extra-small': 'rounded-[var(--md-sys-shape-corner-extra-small)]',
  'small': 'rounded-[var(--md-sys-shape-corner-small)]',
  'medium': 'rounded-[var(--md-sys-shape-corner-medium)]',
  'large': 'rounded-[var(--md-sys-shape-corner-large)]',
  'extra-large': 'rounded-[var(--md-sys-shape-corner-extra-large)]',
  'full': 'rounded-[var(--md-sys-shape-corner-full)]',
};

/**
 * Material 3 Surface Component
 * 
 * @example
 * <M3Surface elevation={1} shape="extra-large">
 *   <p>Elevated surface with large rounded corners</p>
 * </M3Surface>
 */
export function M3Surface({
  children,
  elevation = 0,
  shape = 'medium',
  onClick,
  className = '',
}: M3SurfaceProps) {
  const isInteractive = !!onClick;

  return (
    <div
      onClick={onClick}
      className={`
        text-[var(--md-sys-color-on-surface)]
        transition-all duration-200
        ${elevationStyles[elevation]}
        ${shapeStyles[shape]}
        ${isInteractive ? 'cursor-pointer hover:opacity-90 active:scale-[0.98]' : ''}
        ${className}
      `}
    >
      {children}
    </div>
  );
}

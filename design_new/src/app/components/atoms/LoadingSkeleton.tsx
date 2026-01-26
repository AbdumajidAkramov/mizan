/**
 * LoadingSkeleton Atom Component
 * Material 3 shimmer loading state
 */

export interface LoadingSkeletonProps {
  /** Width in pixels or percentage */
  width?: string | number;
  /** Height in pixels (must follow 8dp grid) */
  height?: number;
  /** Border radius variant */
  radius?: 'sm' | 'md' | 'lg' | 'full';
  /** Additional CSS classes */
  className?: string;
}

const radiusStyles = {
  sm: 'rounded-sm',
  md: 'rounded-lg',
  lg: 'rounded-2xl',
  full: 'rounded-full',
};

/**
 * Material 3 Loading Skeleton
 * Animated placeholder for loading states
 */
export function LoadingSkeleton({
  width = '100%',
  height = 16,
  radius = 'md',
  className = '',
}: LoadingSkeletonProps) {
  const widthStyle = typeof width === 'number' ? `${width}px` : width;

  return (
    <div
      className={`
        bg-[var(--color-surface-variant)]
        animate-pulse
        ${radiusStyles[radius]}
        ${className}
      `}
      style={{ width: widthStyle, height: `${height}px` }}
    />
  );
}

/**
 * LoadingSkeleton - Mizan Design System
 * Clean loading state placeholder with emerald green accent
 */

export interface LoadingSkeletonProps {
  width?: string | number;
  height?: number;
  radius?: 'sm' | 'md' | 'lg' | 'xl' | 'full';
}

export function LoadingSkeleton({ 
  width = '100%', 
  height = 20, 
  radius = 'md' 
}: LoadingSkeletonProps) {
  const radiusMap = {
    sm: '4px',
    md: '8px',
    lg: '12px',
    xl: '16px',
    full: '9999px',
  };

  return (
    <div
      className="animate-pulse bg-gradient-to-r from-[var(--premium-surface-2)] via-[#10b981]/10 to-[var(--premium-surface-2)]"
      style={{
        width: typeof width === 'number' ? `${width}px` : width,
        height: `${height}px`,
        borderRadius: radiusMap[radius],
      }}
    />
  );
}

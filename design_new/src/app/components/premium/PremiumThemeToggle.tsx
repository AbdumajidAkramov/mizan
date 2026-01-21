/**
 * Premium Theme Toggle
 * Elegant theme switcher component
 */

import { Sun, Moon } from 'lucide-react';
import { useTheme } from '../../contexts/ThemeContext';

export function PremiumThemeToggle() {
  const { theme, toggleTheme } = useTheme();

  return (
    <button
      onClick={toggleTheme}
      className="
        relative w-[56px] h-[28px]
        rounded-full
        transition-all duration-300
        flex items-center
      "
      style={{
        background: theme === 'dark' 
          ? 'var(--premium-gradient-primary)'
          : 'var(--premium-gradient-warm)',
        boxShadow: theme === 'dark'
          ? 'var(--premium-glow-primary)'
          : '0 2px 8px rgba(0, 0, 0, 0.1)',
      }}
      aria-label={`Switch to ${theme === 'light' ? 'dark' : 'light'} mode`}
    >
      {/* Sliding circle */}
      <div
        className="
          absolute
          w-[24px] h-[24px]
          rounded-full
          bg-white
          flex items-center justify-center
          transition-transform duration-300
          shadow-md
        "
        style={{
          transform: theme === 'dark' ? 'translateX(30px)' : 'translateX(2px)',
        }}
      >
        {theme === 'dark' ? (
          <Moon size={14} className="text-[#667eea]" />
        ) : (
          <Sun size={14} className="text-[#fa709a]" />
        )}
      </div>
    </button>
  );
}

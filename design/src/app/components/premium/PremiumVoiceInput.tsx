/**
 * Premium Voice Input Component
 * Beautiful soundwave animation for voice-to-transaction
 */

import { useEffect, useState } from 'react';
import { Mic, MicOff } from 'lucide-react';

export interface PremiumVoiceInputProps {
  isListening: boolean;
  onStartListening: () => void;
  onStopListening: () => void;
  transcript?: string;
}

export function PremiumVoiceInput({
  isListening,
  onStartListening,
  onStopListening,
  transcript = '',
}: PremiumVoiceInputProps) {
  const [bars, setBars] = useState<number[]>(Array(12).fill(20));

  // Animate soundwave bars when listening
  useEffect(() => {
    if (!isListening) {
      setBars(Array(12).fill(20));
      return;
    }

    const interval = setInterval(() => {
      setBars(Array(12).fill(0).map(() => Math.random() * 60 + 20));
    }, 100);

    return () => clearInterval(interval);
  }, [isListening]);

  return (
    <div className="flex flex-col items-center justify-center py-[var(--premium-space-2xl)]">
      {/* Microphone Button */}
      <button
        onClick={isListening ? onStopListening : onStartListening}
        className={`
          w-[120px] h-[120px]
          rounded-full
          flex items-center justify-center
          mb-[var(--premium-space-xl)]
          transition-all duration-300
          ${isListening
            ? 'bg-[var(--premium-emerald)] text-white shadow-[0_0_40px_rgba(16,185,129,0.4)]'
            : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-primary)] hover:bg-[var(--premium-surface-3)]'
          }
          active:scale-95
        `}
      >
        {isListening ? (
          <MicOff size={48} strokeWidth={2} />
        ) : (
          <Mic size={48} strokeWidth={2} />
        )}
      </button>

      {/* Soundwave Animation */}
      {isListening && (
        <div className="flex items-center justify-center gap-[6px] h-[80px] mb-[var(--premium-space-lg)]">
          {bars.map((height, idx) => (
            <div
              key={idx}
              className="
                w-[4px]
                bg-[var(--premium-emerald)]
                rounded-full
                transition-all duration-100
              "
              style={{
                height: `${height}px`,
                opacity: 0.4 + (height / 80) * 0.6,
              }}
            />
          ))}
        </div>
      )}

      {/* Status Text */}
      <div className="text-center px-[var(--premium-space-lg)] max-w-md">
        {isListening ? (
          <>
            <p className="body-lg font-medium text-[var(--premium-text-primary)] mb-[8px]">
              I'm listening...
            </p>
            <p className="body-sm text-[var(--premium-text-tertiary)]">
              Try: "Dinner $45 yesterday" or "Coffee 5 dollars"
            </p>
            {transcript && (
              <div className="
                mt-[var(--premium-space-lg)]
                p-[var(--premium-space-md)]
                bg-[var(--premium-surface-2)]
                rounded-[var(--premium-radius-lg)]
              ">
                <p className="body-md text-[var(--premium-text-primary)]">
                  "{transcript}"
                </p>
              </div>
            )}
          </>
        ) : (
          <>
            <p className="body-lg font-medium text-[var(--premium-text-primary)] mb-[8px]">
              Voice Entry
            </p>
            <p className="body-sm text-[var(--premium-text-tertiary)]">
              Tap the microphone to speak your transaction
            </p>
          </>
        )}
      </div>
    </div>
  );
}

/**
 * Premium Enhanced Voice Input Component
 * AI-powered voice-to-transaction with intelligent parsing
 */

import { useEffect, useState } from 'react';
import { Mic, MicOff } from 'lucide-react';

export interface VoiceParseResult {
  amount?: number;
  type?: 'expense' | 'income' | 'transfer';
  category?: string;
  fromAccount?: string;
  toAccount?: string;
  description?: string;
}

export interface PremiumEnhancedVoiceInputProps {
  isListening: boolean;
  onStartListening: () => void;
  onStopListening: () => void;
  onVoiceParsed: (result: VoiceParseResult) => void;
}

export function PremiumEnhancedVoiceInput({
  isListening,
  onStartListening,
  onStopListening,
  onVoiceParsed,
}: PremiumEnhancedVoiceInputProps) {
  const [bars, setBars] = useState<number[]>(Array(12).fill(20));
  const [transcript, setTranscript] = useState('');

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

  // Simulate AI voice parsing
  useEffect(() => {
    if (isListening) {
      // Simulate speech recognition with realistic delay
      const simulateRecognition = setTimeout(() => {
        const examples = [
          {
            text: "Transfer $2000 from Bank to Cash",
            result: {
              amount: 2000,
              type: 'transfer' as const,
              fromAccount: 'bank-checking',
              toAccount: 'cash',
            }
          },
          {
            text: "Coffee 5 dollars",
            result: {
              amount: 5,
              type: 'expense' as const,
              category: 'food',
              description: 'Coffee',
            }
          },
          {
            text: "Dinner $45 yesterday",
            result: {
              amount: 45,
              type: 'expense' as const,
              category: 'food',
              description: 'Dinner',
            }
          },
          {
            text: "Salary $5000",
            result: {
              amount: 5000,
              type: 'income' as const,
              category: 'income',
            }
          },
        ];

        const randomExample = examples[Math.floor(Math.random() * examples.length)];
        setTranscript(randomExample.text);

        // Parse and send result after showing transcript
        setTimeout(() => {
          onVoiceParsed(randomExample.result);
          onStopListening();
          setTranscript('');
        }, 1500);
      }, 1500);

      return () => clearTimeout(simulateRecognition);
    }
  }, [isListening, onVoiceParsed, onStopListening]);

  return (
    <div className="flex flex-col items-center justify-center py-[var(--premium-space-xl)]">
      {/* Microphone Button */}
      <button
        onClick={isListening ? onStopListening : onStartListening}
        className={`
          w-[100px] h-[100px]
          rounded-full
          flex items-center justify-center
          mb-[var(--premium-space-lg)]
          transition-all duration-300
          ${isListening
            ? 'bg-[var(--premium-emerald)] text-white shadow-[0_0_40px_rgba(16,185,129,0.5)] scale-110'
            : 'bg-[var(--premium-surface-2)] text-[var(--premium-text-primary)] hover:bg-[var(--premium-surface-3)]'
          }
          active:scale-95
        `}
      >
        {isListening ? (
          <MicOff size={40} strokeWidth={2} />
        ) : (
          <Mic size={40} strokeWidth={2} />
        )}
      </button>

      {/* Soundwave Animation */}
      {isListening && (
        <div className="flex items-center justify-center gap-[6px] h-[80px] mb-[var(--premium-space-md)]">
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
            <p className="body-sm text-[var(--premium-text-tertiary)] mb-[var(--premium-space-md)]">
              Speak naturally
            </p>
            {transcript && (
              <div className="
                mt-[var(--premium-space-md)]
                p-[var(--premium-space-md)]
                bg-[var(--premium-emerald)]/10
                border border-[var(--premium-emerald)]/20
                rounded-[var(--premium-radius-lg)]
                animate-[slideUp_0.3s_ease-out]
              ">
                <p className="body-md text-[var(--premium-text-primary)] font-medium">
                  "{transcript}"
                </p>
              </div>
            )}
          </>
        ) : (
          <>
            <p className="body-lg font-medium text-[var(--premium-text-primary)] mb-[8px]">
              AI Voice Entry
            </p>
            <div className="body-sm text-[var(--premium-text-tertiary)] space-y-[4px]">
              <p>"Dinner $45 yesterday"</p>
              <p>"Transfer $2000 from Bank to Cash"</p>
              <p>"Coffee 5 dollars"</p>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

/**
 * Premium Scan Input Component
 * Minimalist viewfinder for receipt scanning (OCR)
 */

import { Camera, Upload, X } from 'lucide-react';

export interface PremiumScanInputProps {
  isScanning: boolean;
  onStartScan: () => void;
  onStopScan: () => void;
  onUploadImage: (file: File) => void;
}

export function PremiumScanInput({
  isScanning,
  onStartScan,
  onStopScan,
  onUploadImage,
}: PremiumScanInputProps) {
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      onUploadImage(file);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center py-[var(--premium-space-2xl)]">
      {!isScanning ? (
        <>
          {/* Camera Button */}
          <button
            onClick={onStartScan}
            className="
              w-[120px] h-[120px]
              rounded-full
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              flex items-center justify-center
              mb-[var(--premium-space-xl)]
              transition-all duration-200
              active:scale-95
            "
          >
            <Camera size={48} strokeWidth={2} className="text-[var(--premium-text-primary)]" />
          </button>

          {/* Info Text */}
          <div className="text-center px-[var(--premium-space-lg)] max-w-md mb-[var(--premium-space-xl)]">
            <p className="body-lg font-medium text-[var(--premium-text-primary)] mb-[8px]">
              Receipt Scanning
            </p>
            <p className="body-sm text-[var(--premium-text-tertiary)]">
              Scan a receipt to automatically extract amount and details
            </p>
          </div>

          {/* Upload Button */}
          <label className="
            px-[var(--premium-space-xl)]
            h-[48px]
            rounded-[var(--premium-radius-full)]
            bg-[var(--premium-surface-2)]
            hover:bg-[var(--premium-surface-3)]
            flex items-center justify-center gap-[8px]
            cursor-pointer
            transition-all duration-200
            active:scale-95
          ">
            <Upload size={20} className="text-[var(--premium-text-secondary)]" />
            <span className="body-md font-medium text-[var(--premium-text-secondary)]">
              Upload Image
            </span>
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="hidden"
            />
          </label>
        </>
      ) : (
        <>
          {/* Viewfinder */}
          <div className="relative w-full max-w-md aspect-[3/4] mb-[var(--premium-space-xl)]">
            {/* Viewfinder Frame */}
            <div className="
              absolute inset-0
              rounded-[var(--premium-radius-xl)]
              bg-[var(--premium-surface-1)]
              border-2 border-dashed border-[var(--premium-emerald)]
              overflow-hidden
            ">
              {/* Corner Brackets */}
              <div className="absolute top-[16px] left-[16px] w-[40px] h-[40px] border-t-4 border-l-4 border-[var(--premium-emerald)] rounded-tl-[8px]" />
              <div className="absolute top-[16px] right-[16px] w-[40px] h-[40px] border-t-4 border-r-4 border-[var(--premium-emerald)] rounded-tr-[8px]" />
              <div className="absolute bottom-[16px] left-[16px] w-[40px] h-[40px] border-b-4 border-l-4 border-[var(--premium-emerald)] rounded-bl-[8px]" />
              <div className="absolute bottom-[16px] right-[16px] w-[40px] h-[40px] border-b-4 border-r-4 border-[var(--premium-emerald)] rounded-br-[8px]" />

              {/* Scanning Line Animation */}
              <div className="
                absolute left-0 right-0 h-[2px]
                bg-[var(--premium-emerald)]
                shadow-[0_0_20px_rgba(16,185,129,0.6)]
                animate-[scan_2s_ease-in-out_infinite]
              " />

              {/* Center Guide */}
              <div className="absolute inset-0 flex items-center justify-center">
                <div className="
                  w-[80%] h-[60%]
                  border border-[var(--premium-emerald)]/30
                  rounded-[var(--premium-radius-md)]
                " />
              </div>
            </div>

            {/* Status Overlay */}
            <div className="
              absolute bottom-0 left-0 right-0
              p-[var(--premium-space-md)]
              bg-gradient-to-t from-black/60 to-transparent
              rounded-b-[var(--premium-radius-xl)]
            ">
              <p className="body-sm text-center text-white font-medium">
                Position receipt within frame
              </p>
            </div>
          </div>

          {/* Cancel Button */}
          <button
            onClick={onStopScan}
            className="
              w-[56px] h-[56px]
              rounded-full
              bg-[var(--premium-surface-2)]
              hover:bg-[var(--premium-surface-3)]
              flex items-center justify-center
              transition-all duration-200
              active:scale-95
            "
          >
            <X size={24} className="text-[var(--premium-text-secondary)]" />
          </button>
        </>
      )}
    </div>
  );
}

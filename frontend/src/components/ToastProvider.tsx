import React from 'react'

/**
 * Implementación mínima por ahora (no-op).
 * Más adelante podremos integrar radix-ui/react-toast o shadcn/ui.
 */
export const ToastProvider: React.FC<React.PropsWithChildren> = ({ children }) => {
  return <>{children}</>
}

// API básica para futuros toasts (placeholder)
export function useToast() {
  return {
    success: (msg: string) => console.log('[toast:success]', msg),
    error: (msg: string) => console.error('[toast:error]', msg),
    info: (msg: string) => console.info('[toast:info]', msg),
  }
}


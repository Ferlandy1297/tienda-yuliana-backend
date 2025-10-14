import React, { createContext, useContext, useEffect, useMemo, useState } from 'react'

export type Role = 'ADMIN' | 'EMPLEADO'

export interface Session {
  idUsuario?: number
  nombreUsuario: string
  rol: Role
}

interface SessionContextValue {
  session: Session | null
  login: (s: Session) => void
  logout: () => void
  hasRole: (...roles: Role[]) => boolean
}

const SESSION_KEY = 'app:session'
const SessionContext = createContext<SessionContextValue | undefined>(undefined)

export const SessionProvider: React.FC<React.PropsWithChildren> = ({ children }) => {
  const [session, setSession] = useState<Session | null>(() => {
    try {
      const raw = localStorage.getItem(SESSION_KEY)
      return raw ? (JSON.parse(raw) as Session) : null
    } catch {
      return null
    }
  })

  useEffect(() => {
    if (session) localStorage.setItem(SESSION_KEY, JSON.stringify(session))
    else localStorage.removeItem(SESSION_KEY)
  }, [session])

  const value = useMemo<SessionContextValue>(
    () => ({
      session,
      login: (s) => setSession(s),
      logout: () => setSession(null),
      hasRole: (...roles) => !!session && (roles.length === 0 || roles.includes(session.rol)),
    }),
    [session]
  )

  return <SessionContext.Provider value={value}>{children}</SessionContext.Provider>
}

export const useSession = (): SessionContextValue => {
  const ctx = useContext(SessionContext)
  if (!ctx) throw new Error('useSession must be used within a SessionProvider')
  return ctx
}


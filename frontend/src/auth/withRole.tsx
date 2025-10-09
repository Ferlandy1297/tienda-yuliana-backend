import React from 'react'
import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { Role, useSession } from './session'

type WithRoleProps = {
  roles?: Role[]
  fallback?: React.ReactNode
  children: React.ReactNode
}

/**
 * Muestra los children solo si el usuario tiene alguno de los roles indicados.
 * Si no se pasan roles, muestra siempre (útil para envolver acciones opcionales).
 */
export const WithRole: React.FC<WithRoleProps> = ({ roles = [], fallback = null, children }) => {
  const { hasRole } = useSession()
  const allowed = hasRole(...roles)
  return <>{allowed ? children : fallback}</>
}

type RequireRoleProps = {
  roles?: Role[]
  redirectTo?: string
}

/**
 * Guard para rutas protegidas por rol.
 * - Si no hay sesión, redirige a /login (o redirectTo).
 * - Si hay sesión pero no tiene el rol requerido, redirige a la raíz (/).
 * - Si pasa, renderiza <Outlet />.
 */
export const RequireRole: React.FC<RequireRoleProps> = ({ roles = [], redirectTo = '/login' }) => {
  const { session, hasRole } = useSession()
  const location = useLocation()

  if (!session) {
    return <Navigate to={redirectTo} replace state={{ from: location }} />
  }
  if (!hasRole(...roles)) {
    return <Navigate to="/" replace />
  }
  return <Outlet />
}

/** Helper para verificar rápido si el usuario es ADMIN en componentes. */
export const useIsAdmin = (): boolean => {
  const { hasRole } = useSession()
  return hasRole('ADMIN')
}


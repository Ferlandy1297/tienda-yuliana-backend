import React from 'react'
import { NavLink, Outlet } from 'react-router-dom'

export const AppLayout: React.FC = () => {
  const linkBase =
    'px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-100 transition-colors'
  const linkActive = 'bg-gray-200'

  return (
    <div className="min-h-screen flex flex-col">
      <header className="border-b">
        <div className="mx-auto max-w-6xl px-4 py-3 flex items-center justify-between">
          <div className="font-semibold">Tienda Yuliana</div>
          <nav className="flex gap-2">
            <NavLink
              to="/productos"
              className={({ isActive }) => `${linkBase} ${isActive ? linkActive : ''}`}
            >
              Productos
            </NavLink>
            <NavLink
              to="/ventas"
              className={({ isActive }) => `${linkBase} ${isActive ? linkActive : ''}`}
            >
              Ventas
            </NavLink>
            <NavLink
              to="/reportes"
              className={({ isActive }) => `${linkBase} ${isActive ? linkActive : ''}`}
            >
              Reportes
            </NavLink>
          </nav>
        </div>
      </header>
      <main className="flex-1">
        <div className="mx-auto max-w-6xl px-4 py-6">
          <Outlet />
        </div>
      </main>
    </div>
  )
}


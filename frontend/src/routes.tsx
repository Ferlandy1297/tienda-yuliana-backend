import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { AppLayout } from './layouts/AppLayout'

// Placeholders iniciales (se reemplazarán por páginas reales)
const LoginPage: React.FC = () => (
  <div className="p-6">
    <h1 className="text-2xl font-semibold">Login</h1>
    <p className="text-sm text-gray-500">Pantalla temporal. Implementaremos el formulario después.</p>
  </div>
)
const ProductosPage: React.FC = () => (
  <div className="p-6">
    <h1 className="text-2xl font-semibold">Productos</h1>
    <p className="text-sm text-gray-500">Listado/CRUD se implementará más adelante.</p>
  </div>
)
const VentasPage: React.FC = () => (
  <div className="p-6">
    <h1 className="text-2xl font-semibold">Ventas</h1>
    <p className="text-sm text-gray-500">Flujos de venta y cálculo de cambio vendrán después.</p>
  </div>
)
const ReportesPage: React.FC = () => (
  <div className="p-6">
    <h1 className="text-2xl font-semibold">Reportes de Ventas</h1>
    <p className="text-sm text-gray-500">Filtro por periodo y resultados se implementarán luego.</p>
  </div>
)

export const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<AppLayout />}>
        <Route path="/" element={<Navigate to="/productos" replace />} />
        <Route path="/productos" element={<ProductosPage />} />
        <Route path="/ventas" element={<VentasPage />} />
        <Route path="/reportes" element={<ReportesPage />} />
      </Route>
      <Route path="*" element={<div className="p-6">404 - No encontrado</div>} />
    </Routes>
  )
}


# Matriz de Cobertura de Casos de Uso — Tienda Yuliana

CU | Endpoint(s) | Service/method | Regla de negocio clave | Estado | Notas
--- | --- | --- | --- | --- | ---
CU-TY-001 Registrar Producto | POST `/api/productos` | `ProductoService.crear` | DTO con validaciones; código de barras único | OK | Validaciones: precio > 0, stock >= 0; unicidad de código en service.
CU-TY-002 Actualizar Inventario en Venta | POST `/api/ventas` | `VentaServiceImpl.crear` | Descuento de stock transaccional; FIFO por lote | OK | FIFO por lotes y actualización de stock por producto.
CU-TY-003 Registrar Venta + Pago | POST `/api/ventas`; POST `/api/ventas/{id}/pagos` | `VentaService.crear`; `VentaService.registrarPago` | Pago en EFECTIVO y cálculo de cambio | OK | Endpoint de pago implementado y validado (ver CU-TY-012).
CU-TY-004 Reportes de Ventas | GET `/api/reportes/ventas?periodo=` | `ReporteService.ventasPorPeriodo` | Periodos: diario/quincenal/mensual | OK | Valida periodo y consulta agregada.
CU-TY-005 Autenticación Usuarios | POST `/api/auth/login` | `AuthService.login` | Roles `ADMIN`/`EMPLEADO` | OK | Retorna rol; sin JWT (no requerido en este alcance).
CU-TY-006 Alertas de Stock Bajo | GET `/api/productos/stock-bajo` | `ProductoService.stockBajo` | stock <= stock_minimo | OK | Consulta JPA con `@Query`.
CU-TY-007 Venta al por Mayor | POST `/api/ventas?tipo=MAYOREO` | `VentaService.crear` | Precio por volumen | OK | 10% descuento si cantidad >= 10 y cliente mayorista.
CU-TY-008 Gestionar Fiados | POST `/api/ventas?tipo=FIADO`; POST `/api/cuentas/{id}/abonos` | `VentaService.crear`; `CuentaCorrienteService.registrarAbono` | Límite de crédito | OK | Valida saldoActual + total <= límite; registra CARGO.
CU-TY-009 Caducidades | GET `/api/lotes/por-vencer?dias=`; POST `/api/acciones/caducidad` | `LoteService.porVencer`; `CaducidadService.aplicar` | Acciones impactan stock y merma/devolución | OK | Por vencer implementado; DONACION/DEVOLUCION ajustan stock y registran; DESCUENTO no-op (sin cambio de esquema).
CU-TY-010 Registrar Merma | POST `/api/mermas` | `MermaService.registrar` | Disminuye stock y registra merma | OK | Valida stock; decrementa producto y guarda Merma con costo estimado.
CU-TY-011 Devolución a Proveedor | POST `/api/devoluciones` | `DevolucionProveedorService.registrar` | Disminuye stock, crea devolución | OK | Valida proveedor/lotes; descuenta lote y stock; crea cabecera y detalles.
CU-TY-012 Registrar Pago de Venta | POST `/api/ventas/{id}/pagos` | `VentaService.registrarPago` | EFECTIVO, monto, denominación, cambio | OK | Valida denominaciones {0.50,1,5,10,20,50,100,200} y múltiplos; protege doble pago.
CU-TY-013 Registrar Compra | POST `/api/compras` | `CompraService.crearCompra` | Crea/actualiza lotes y stock | OK | Crea lotes, actualiza stock y estado de compra.
CU-TY-014 Pago de Compra | POST `/api/compras/{id}/pagos` | `CompraService.pagar` | Estados ABIERTA/PARCIAL/PAGADA | OK | Valida montos y actualiza estado.
CU-TY-015 Reportes de Compras | GET `/api/reportes/compras` | `ReporteComprasService.compras` | Filtro por rango y proveedor | OK | Devuelve totales por día.

## Hallazgos y Recomendaciones
- En “DESCUENTO” de caducidades no se persiste bandera en `lote` para evitar cambios de esquema; considerar agregar columna en una migración futura.
- Añadir tests de integración para FIADO (control de crédito) y caducidades.
- Si se requiere multipago en ventas, extender repositorio de pagos para manejar saldos.

## Extras no numerados: OK
- CRUD Clientes y Proveedores con DTOs, services y controllers; roles: ADMIN CRUD, EMPLEADO/SUPERVISOR solo lectura.
- Reportes: utilidades y top productos entre rangos de fechas.
- Comprobante PDF de venta: GET `/api/ventas/{id}/comprobante.pdf` con OpenPDF.
- Exportaciones: PDF y Excel para ventas por periodo (OpenPDF y Apache POI).
- Promociones: entidad y endpoints; descuento global aplicado sin romper mayoreo.
- Notificaciones por email: EmailService + StockNotificationService + endpoint de disparo y hooks tras venta/merma/devolución.
- Analytics dashboard: ventasPorDia, utilidad, ticketsPromedio.

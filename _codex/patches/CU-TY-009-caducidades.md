Title: CU-TY-009 Caducidades — Rutas y servicios

Resumen:
- Endpoint GET `/api/lotes/por-vencer?dias=` en `LoteController` y `LoteService` con TODO para query en `LoteRepository`.
- Endpoint POST `/api/acciones/caducidad` en `AccionesCaducidadController` + `CaducidadService` con TODOs para aplicar acciones (DESCUENTO/DONACION/DEVOLUCION).
- DTOs `LotePorVencerDTO` y `CaducidadAccionDTO` agregados.

Sugerencia repositorio:
- `List<Lote> findByFechaVencimientoBetween(LocalDate desde, LocalDate hasta);`

Impactos:
- Las acciones deben registrar `merma` o `devolucion_proveedor` y ajustar stock.


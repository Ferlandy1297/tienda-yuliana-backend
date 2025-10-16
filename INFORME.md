AUDITORÍA BACKEND – TIENDA YULIANA

Resumen: Se auditó el backend (Spring Boot) respecto a los casos de uso críticos e intermedios y el esquema de BD indicado. En general, el proyecto ya cubre la mayoría de endpoints y reglas. Se identifican pocos gaps puntuales (principalmente acción de caducidad = DESCUENTO sin persistencia) y se proponen cambios mínimos.

Matriz de cumplimiento (Casos de Uso vs Implementación)

- CU-TY-001 Registrar Producto
  - Endpoints: `POST /api/productos` (ProductoController.java)
  - Servicios/Repos: ProductoServiceImpl.java, ProductoRepository.java
  - DTOs: ProductCreateDTO, ProductResponseDTO
  - Reglas: código de barras único (valida en servicio + unique en entidad); `precioVenta > 0`; `stock >= 0` (DTO). Proveedor FK opcional.
  - Cumple: Sí.

- CU-TY-002 Actualizar inventario al vender
  - Endpoints: `POST /api/ventas` (VentaController.java)
  - Servicios/Repos: VentaServiceImpl.java (@Transactional), VentaRepository, VentaDetalleRepository, LoteRepository, ProductoRepository
  - DTOs: VentaCreateDTO, VentaDetalleCreateDTO, VentaResponseDTO
  - Reglas: descuenta stock por producto y por lote; si no se especifica lote, consume por FIFO de lotes (`findByProducto_IdProductoOrderByFechaVencimientoAsc`).
  - Cumple: Sí. Transaccionalidad con @Transactional.

- CU-TY-003 Registrar Venta diaria con pago y cambio
  - Endpoints: `POST /api/ventas` (opcional pago embebido) y `POST /api/ventas/{id}/pagos`
  - Servicios/Repos: VentaServiceImpl.java, PagoRepository.java
  - DTOs: PagoCreateDTO, VentaResponseDTO
  - Reglas: método de pago = EFECTIVO; valida monto vs total; calcula y persiste `cambioCalculado`; guarda `denominacionBillete`.
  - Cumple: Sí.

- CU-TY-004 Reportes de Ventas (diario/quincenal/mensual)
  - Endpoints: `GET /api/reportes/ventas?periodo=diario|quincenal|mensual`
  - Servicios/Repos: ReporteServiceImpl.java, VentaRepository.java (consultas agregadas)
  - DTOs: ReporteVentasDTO
  - Cumple: Sí.

- CU-TY-005 Autenticación (roles y activo)
  - Endpoints: `POST /api/auth/login`
  - Servicios/Repos: AuthServiceImpl.java, UsuarioSisRepository.java
  - Reglas: valida usuario activo; contraseñas con BCrypt o {noop}; retorna rol.
  - Cumple: Sí (login sin JWT, pero suficiente según alcance solicitado).

- CU-TY-006 Alertas de stock bajo
  - Endpoints: `GET /api/productos/stock-bajo` y `POST /api/notificaciones/stock-bajo/send`
  - Servicios/Repos: ProductoServiceImpl.stockBajo(), StockNotificationServiceImpl
  - Cumple: Sí.

- CU-TY-007 Venta al por mayor
  - Endpoints: `POST /api/ventas` (usa `tipo=MAYOREO` en el cuerpo)
  - Servicios: VentaServiceImpl.calcularPrecioMayoreo(); valida cliente mayorista
  - Cumple: Sí (la indicación de query `?tipo=` del roadmap se atiende vía body, funcionalmente equivalente).

- CU-TY-008 Fiados y abonos
  - Endpoints: `POST /api/ventas` con `tipo=FIADO`; `POST /api/cuentas/{id}/abonos`
  - Servicios/Repos: VentaServiceImpl (CARGO en cuenta), CuentaCorrienteServiceImpl (ABONO), CuentaCorrienteRepository, MovimientoCcRepository
  - Cumple: Sí (límites de crédito básicos; puede ampliarse política de mora/bloqueo).

- CU-TY-009 Caducidades/Lotes por vencer
  - Endpoints: `GET /api/lotes/por-vencer?dias=30`; `POST /api/acciones/caducidad`
  - Servicios: LoteServiceImpl (listado), CaducidadServiceImpl (DONACION y DEVOLUCION ajustan stock y registran merma/devolución). Acción DESCUENTO no persiste bandera actualmente.
  - Cumple: Parcial (DESCUENTO sin persistencia/flag). Ver plan de cambios.

- CU-TY-010 Merma
  - Endpoints: `POST /api/mermas`
  - Servicios: MermaServiceImpl (descuenta stock, registra merma y notifica si stock bajo)
  - Cumple: Sí.

- CU-TY-011 Devolución a proveedor
  - Endpoints: `POST /api/devoluciones`
  - Servicios: DevolucionProveedorServiceImpl (detalle por lote, ajusta stock y total estimado)
  - Cumple: Sí.

- CU-TY-012 Pago de venta
  - Endpoints: `POST /api/ventas/{id}/pagos`
  - Servicios/Repos: VentaServiceImpl.registrarPago; PagoRepository
  - Reglas: método=EFECTIVO; calcula cambio; valida denominación múltiplo; evita doble pago.
  - Cumple: Sí.

- CU-TY-013 Compras
  - Endpoints: `POST /api/compras`
  - Servicios: CompraServiceImpl (crea lotes, incrementa stock, detalle por lote)
  - Cumple: Sí.

- CU-TY-014 Pago de compra
  - Endpoints: `POST /api/compras/{id}/pagos`
  - Servicios/Repos: CompraServiceImpl.pagar, PagoCompraRepository
  - Cumple: Sí.

- CU-TY-015 Reporte de compras
  - Endpoints: `GET /api/reportes/compras?desde&hasta&proveedorId`
  - Servicios/Repos: ReporteComprasService, CompraRepository (consulta agregada)
  - Cumple: Sí.

Validaciones específicas solicitadas

- Pago EFECTIVO, cambio y denominación: Implementado en VentaServiceImpl (calcula `cambio`, fuerza `metodo=EFECTIVO`, persiste `denominacionBillete`). Entidad Pago incluye campos `montoEntregado`, `cambioCalculado` y `denominacionBillete`.
- Actualización de stock transaccional + FIFO por lote: VentaServiceImpl está anotado con @Transactional y consume lotes en orden de `fecha_vencimiento` (FIFO) creando un `VentaDetalle` por lote consumido. También actualiza `cantidad_disponible` del lote y `stock` del producto dentro de la misma transacción.

Gaps detectados y observaciones

- Acción de caducidad DESCUENTO: actualmente no persiste ninguna marca/flag en los lotes/productos. Propuesta: agregar columna en `lote` para marcar descuento (ej. `en_descuento boolean` o `porcentaje_descuento numeric`).
- OpenAPI generado contenía rutas fuera de `paths` (estructuralmente inválido). Se agregó `openapi.yaml` corregido con todos los endpoints clave.
- Tests: el repo tenía cobertura de pruebas mínima. Se agregan pruebas unitarias de servicio con Mockito y “integración ligera” de flujos usando mocks.

Plan de cambios propuesto (aplicado en este PR)

- Documentación: agregar INFORME.md (este documento) y corregir/centralizar OpenAPI en `openapi.yaml`.
- QA: agregar pruebas unitarias (servicios de ventas, pagos, fiados/abonos, stock-bajo, lotes por vencer) y pruebas de flujo con mocks (DETALLE/MAYOREO, FIADO+abono, compra+pago compra).
- Operativa: agregar scripts de arranque y ejemplos curl en `scripts/`.
- Semillas: agregar datasets de ejemplo en `docs/seeds` para ventas y compras.
- Caducidad – DESCUENTO: se propone migración SQL para añadir un flag en `lote`. Se entrega script en `docs/migrations/20251015__add_lote_descuento.sql`. No se activa Flyway para no introducir dependencia nueva sin confirmación.

Conclusión

El backend cumple ampliamente con los casos de uso críticos e intermedios solicitados, con reglas de negocio alineadas al esquema (pagos en efectivo con cambio/denominación, consumo FIFO por lote y manejo de fiados/abonos). Los únicos ajustes recomendados son mejorar la acción de caducidad “DESCUENTO” con persistencia, robustecer pruebas, y usar el nuevo `openapi.yaml` centralizado.


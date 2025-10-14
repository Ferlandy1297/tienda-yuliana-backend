Title: CU-TY-012 Registrar Pago de Venta — Endpoint y Service

Resumen del parche (no destructivo):
- Agrega endpoint POST `/api/ventas/{id}/pagos` en `VentaController`.
- Agrega método `registrarPago(Integer, PagoCreateDTO)` en `VentaService` y su implementación en `VentaServiceImpl`.
- Reglas actuales: solo EFECTIVO; valida monto >= total; retorna cambio. TODOs pendientes sobre denominaciones/caja.

Archivos tocados:
- src/main/java/com/tiendayuliana/backend/controller/VentaController.java
- src/main/java/com/tiendayuliana/backend/service/VentaService.java
- src/main/java/com/tiendayuliana/backend/service/impl/VentaServiceImpl.java
- src/test/java/com/tiendayuliana/backend/controller/VentaControllerTest.java (esqueleto @Disabled)

Notas:
- Diseño actual permite un pago por venta (según `PagoRepository.findFirst...`). Se valida que no exista pago previo.
- Si se requiere multipagos, extender `PagoRepository` para sumar pagos y comparar contra saldo.


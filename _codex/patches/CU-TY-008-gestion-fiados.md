Title: CU-TY-008 Gestionar Fiados — Endpoints y reglas

Resumen:
- Se agrega endpoint POST `/api/cuentas/{id}/abonos` con `CuentaCorrienteController`.
- Se incorpora `CuentaCorrienteService` + `CuentaCorrienteServiceImpl` con TODOs para límite de crédito y bloqueo por mora.
- DTO `AbonoCreateDTO` creado.

Pendientes (TODO):
- Validar límite de crédito antes de aceptar ventas FIADO.
- Calcular deuda, mora y estados de cuenta; bloquear si aplica.
- Agregar consultas/reportes de estado de cuenta.

Archivos nuevos: controller, service, service/impl, dto.


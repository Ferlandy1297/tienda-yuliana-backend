# v0.9-backend  Backend listo para Frontend (Fase 6)

## Cambios destacados
-  CUs completos: 15/15 (pagos, fiados, caducidad, merma, devoluciones, etc.).
-  Extras implementados:
  - Clientes/Proveedores: CRUD con DTOs, services y controllers.  
    Endpoints: 
    - GET/POST /api/clientes, GET/PUT/DELETE /api/clientes/{id}
    - GET/POST /api/proveedores, GET/PUT/DELETE /api/proveedores/{id}
  - Promociones: entidad + descuento global aplicado en ventas (respeta mayoreo).  
    Endpoints: GET/POST /api/promociones
  - Reportes extra:
    - GET /api/reportes/utilidades?desde&hasta
    - GET /api/reportes/top-productos?desde&hasta&limit=10
    - GET /api/reportes/ventas.pdf?periodo=diario|quincenal|mensual
    - GET /api/reportes/ventas.xlsx?periodo=...
  - Ventas: comprobante PDF  
    - GET /api/ventas/{id}/comprobante.pdf
  - Notificaciones stock bajo: EmailService + hooks (venta/merma/devolución)  
    - POST /api/notificaciones/stock-bajo/send
  - Analytics dashboard  
    - GET /api/analytics/dashboard?desde&hasta (ventasPorDia, utilidad, ticketsPromedio)
- 🔒 Roles por cabecera X-Role con PreAuthorize
:  - ADMIN: CRUD total
  - SUPERVISOR: lectura y (extensible) autorizaciones
  - EMPLEADO: lectura básica + operaciones de venta
- 📄 Artefactos: OpenAPI (generated/expected), Postman actualizada, auditoría OK.
- 📦 Dependencias: security, mail, OpenPDF, Apache POI.

## Notas de validación rápida
1) mvn -q -DskipTests clean package  OK  
2) Probar roles en Postman con X-Role: ADMIN|SUPERVISOR|EMPLEADO  
3) Verificar PDF/XLSX y comprobante en endpoints indicados.

## Consideraciones
- Seguridad real de producción pendiente (header X-Role es sólo para demo); integrar auth JWT/Spring Security más adelante.

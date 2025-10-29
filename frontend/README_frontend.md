Frontend Web (Flutter)

Requisitos
- Flutter 3.22+ con canal stable
- Dart SDK compatible (incluido con Flutter)
- Chrome para ejecutar web

Configuración
1) Instala Flutter y activa web:
   flutter config --enable-web

2) Ajusta la URL del backend en:
   frontend/lib/config.dart
   Por defecto: http://localhost:8081

3) Instala dependencias:
   cd frontend
   flutter pub get

4) Ejecuta en Chrome:
   flutter run -d chrome

5) Compila producción:
   flutter build web --dart-define=API_BASE_URL=https://tu-backend

Características
- Login con persistencia de token y rol (local storage)
- Enrutamiento con go_router y protección por rol
- Pantallas:
  - Login
  - Panel (dashboard)
  - Productos: alertas de stock bajo, crear, actualizar precio, notificar por correo (solo admin)
  - Proveedores: listar, crear, actualizar (admin; listar también empleado)
  - Clientes: listar, crear, actualizar (admin; listar también empleado), filtros y exportar CSV
  - Reportes (admin): ventas (periodo o rango, CSV), utilidades, serie, top vendidos
  - Lotes (admin): por vencer en N días
  - Fiados (admin/empleado): consultar saldo y registrar abonos
  - Mermas (admin): registrar merma
  - Devoluciones (admin): devolver a proveedor con ítems
  - Ventas (admin/empleado): registrar venta simple (detalle/mayoreo/fiado)
- UI responsiva (Material 3) con layout tipo rail/drawer
- Manejo de carga/errores y validaciones en formularios

Autenticación
- HTTP Basic hacia el backend Spring Security.
- Endpoint de verificación: GET /api/auth/check (retorna user y authorities).
- Usuario inicial por defecto: admin / admin123 (ver DataInitializer del backend).

Endpoints usados
- Productos:
  - GET /api/productos/alertas/stock-bajo (listar productos con stock bajo)
  - POST /api/productos (crear)
  - PUT /api/productos/{id}/precio (actualizar precio de venta)
  - POST /api/productos/alertas/stock-bajo/notificar (intenta enviar correo)
- Proveedores:
  - GET /api/proveedores (listar)
  - GET /api/proveedores/{id}
  - POST /api/proveedores (crear)
  - PUT /api/proveedores/{id} (actualizar)
 - Clientes:
   - GET /api/clientes (listar)
   - GET /api/clientes/{id}
   - POST /api/clientes (crear, solo ADMIN)
   - PUT /api/clientes/{id} (actualizar, solo ADMIN)
 - Reportes (solo ADMIN):
   - GET /api/reportes/ventas?periodo=diario|quincenal|mensual o inicio/fin ISO8601
   - GET /api/reportes/ventas.csv?periodo=...
   - GET /api/reportes/mas-vendidos?inicio&fin&top
   - GET /api/reportes/utilidades?inicio&fin
   - GET /api/reportes/ventas/serie?inicio&fin&granularidad=day|month
 - Lotes (solo ADMIN):
   - GET /api/lotes/por-vencer?dias=30
 - Fiados (ADMIN/EMPLEADO):
   - GET /api/fiados/saldo/{idCliente}
   - POST /api/fiados/abonos { idCliente, monto }
 - Mermas (solo ADMIN):
   - POST /api/mermas { idProducto, idLote?, cantidad, motivo, observacion? }
 - Devoluciones a proveedor (solo ADMIN):
   - POST /api/devoluciones-proveedor { idProveedor, motivo?, items:[{idProducto,idLote,cantidad,costoEstimado}] }
 - Ventas (ADMIN/EMPLEADO):
   - POST /api/ventas { tipo, idCliente?, items:[{idProducto,cantidad,precioUnitario?}], pago:{montoEntregado} }
 - Clientes:
   - GET /api/clientes (listar)
   - GET /api/clientes/{id}
   - POST /api/clientes (crear, solo ADMIN)
   - PUT /api/clientes/{id} (actualizar, solo ADMIN)

Notas
- Este frontend asume nombres de campos del backend: idProducto, nombre, precioVenta, stock, stockMinimo, etc.
- La navegación usa context.go('/ruta').


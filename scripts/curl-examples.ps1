$BASE_URL = $env:BASE_URL
if (-not $BASE_URL) { $BASE_URL = 'http://localhost:8080' }

Write-Host 'Auth login'
irm -Method Post "$BASE_URL/api/auth/login" -ContentType 'application/json' -Body '{"nombreUsuario":"admin","password":"admin123"}'

Write-Host 'Crear producto'
irm -Method Post "$BASE_URL/api/productos" -ContentType 'application/json' -Body '{"nombre":"Galletas","codigoBarras":"123456","precioVenta":2.5,"costoActual":1.2,"stock":100,"stockMinimo":10}'

Write-Host 'Stock bajo'
irm "$BASE_URL/api/productos/stock-bajo"

Write-Host 'Crear venta DETALLE'
irm -Method Post "$BASE_URL/api/ventas" -ContentType 'application/json' -Body '{"tipo":"DETALLE","idUsuario":1,"detalles":[{"idProducto":1,"cantidad":2}],"pago":{"montoEntregado":50,"denominacionBillete":50}}'

Write-Host 'Reporte ventas diario'
irm "$BASE_URL/api/reportes/ventas?periodo=diario"


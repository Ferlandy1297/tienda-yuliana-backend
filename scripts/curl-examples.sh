#!/usr/bin/env bash
BASE_URL=${BASE_URL:-http://localhost:8080}

echo "Auth login"
curl -s -X POST "$BASE_URL/api/auth/login" \
  -H 'Content-Type: application/json' \
  -d '{"nombreUsuario":"admin","password":"admin123"}'

echo "\nCrear producto"
curl -s -X POST "$BASE_URL/api/productos" \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Galletas","codigoBarras":"123456","precioVenta":2.5,"costoActual":1.2,"stock":100,"stockMinimo":10}'

echo "\nStock bajo"
curl -s "$BASE_URL/api/productos/stock-bajo"

echo "\nCrear venta DETALLE"
curl -s -X POST "$BASE_URL/api/ventas" \
  -H 'Content-Type: application/json' \
  -d '{"tipo":"DETALLE","idUsuario":1,"detalles":[{"idProducto":1,"cantidad":2}],"pago":{"montoEntregado":50,"denominacionBillete":50}}'

echo "\nReporte ventas diario"
curl -s "$BASE_URL/api/reportes/ventas?periodo=diario"


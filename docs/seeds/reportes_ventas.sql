-- Dataset de ejemplo para reportes de ventas (esquema tienda_yuliana)
-- NOTA: Ajusta IDs y FK según tu base local

insert into tienda_yuliana.usuario_sis (id_usuario, nombre_usuario, password_hash, rol, activo)
values (1, 'admin', '{noop}admin123', 'ADMIN', true)
on conflict do nothing;

insert into tienda_yuliana.producto (id_producto, nombre, codigo_barras, precio_venta, costo_actual, stock, stock_minimo, id_proveedor, activo)
values (1, 'Galletas', '123456', 2.50, 1.20, 100, 10, null, true)
on conflict do nothing;

-- Venta de hoy
insert into tienda_yuliana.venta (id_venta, fecha_hora, tipo, total, id_cliente, id_usuario)
values (1, now(), 'DETALLE', 5.00, null, 1)
on conflict do nothing;

insert into tienda_yuliana.pago (id_pago, id_venta, metodo, monto_entregado, cambio_calculado, fecha_hora)
values (1, 1, 'EFECTIVO', 10.00, 5.00, now())
on conflict do nothing;


-- Dataset de ejemplo para reportes de compras (esquema tienda_yuliana)

insert into tienda_yuliana.proveedor (id_proveedor, nombre)
values (1, 'Proveedor 1')
on conflict do nothing;

insert into tienda_yuliana.compra (id_compra, fecha_hora, condicion, estado, observacion, total, id_proveedor)
values (1, now() - interval '5 day', 'CONTADO', 'PAGADA', 'compra test', 100.00, 1)
on conflict do nothing;

insert into tienda_yuliana.pago_compra (id_pago_compra, id_compra, metodo, monto, observacion)
values (1, 1, 'EFECTIVO', 100.00, 'pago total')
on conflict do nothing;


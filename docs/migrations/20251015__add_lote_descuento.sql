-- Propuesta de migración para soportar acción de caducidad "DESCUENTO"
-- Agrega columnas en lote para marcar descuento o porcentaje

alter table tienda_yuliana.lote
  add column if not exists en_descuento boolean default false,
  add column if not exists porcentaje_descuento numeric(5,2);

-- Opcionalmente, índice para consultar rápidamente lotes en descuento
create index if not exists idx_lote_en_descuento on tienda_yuliana.lote (en_descuento);


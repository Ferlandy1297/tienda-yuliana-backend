package com.tiendayuliana.backend.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VentaResponseDTO(
        Integer idVenta,
        String tipo,
        Integer idCliente,
        Integer idUsuario,
        LocalDateTime fechaHora,
        BigDecimal total,
        List<DetalleOut> detalles,
        PagoOut pago // puede ser null
) {
    public record DetalleOut(Integer idProducto, Integer idLote, Integer cantidad,
                             BigDecimal precioUnitario, BigDecimal descuento) {}
    public record PagoOut(Integer idPago, String metodo, BigDecimal montoEntregado, BigDecimal cambioCalculado) {}
}

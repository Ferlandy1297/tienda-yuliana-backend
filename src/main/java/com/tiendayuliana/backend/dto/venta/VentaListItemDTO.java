package com.tiendayuliana.backend.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VentaListItemDTO(
        Integer idVenta,
        LocalDateTime fechaHora,
        String tipo,
        Integer idCliente,
        Integer idUsuario,
        BigDecimal total
) {
    
}

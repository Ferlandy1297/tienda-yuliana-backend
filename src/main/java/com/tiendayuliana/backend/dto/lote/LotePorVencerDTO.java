package com.tiendayuliana.backend.dto.lote;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LotePorVencerDTO(
        Integer idLote,
        Integer idProducto,
        String nombreProducto,
        LocalDate fechaVencimiento,
        Integer cantidadDisponible,
        Boolean enDescuento,
        BigDecimal porcentajeDescuento
) {}


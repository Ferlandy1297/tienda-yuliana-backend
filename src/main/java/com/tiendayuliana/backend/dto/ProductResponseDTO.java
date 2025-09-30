package com.tiendayuliana.backend.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Integer idProducto,
        String nombre,
        String codigoBarras,
        BigDecimal precioVenta,
        BigDecimal costoActual,
        Integer stock,
        Integer stockMinimo,
        Integer idProveedor,
        Boolean activo
) {}

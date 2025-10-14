package com.tiendayuliana.backend.dto.cliente;

import java.math.BigDecimal;

public record ClienteResponseDTO(
        Integer idCliente,
        String nombre,
        String telefono,
        String nit,
        boolean esMayorista,
        BigDecimal limiteCredito,
        String estadoCredito,
        Boolean activo
) {}


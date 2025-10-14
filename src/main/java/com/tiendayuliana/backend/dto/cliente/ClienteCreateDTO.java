package com.tiendayuliana.backend.dto.cliente;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteCreateDTO(
        @NotBlank @Size(max = 120) String nombre,
        @Size(max = 30) String telefono,
        @Size(max = 30) String nit,
        boolean esMayorista,
        BigDecimal limiteCredito
) {}


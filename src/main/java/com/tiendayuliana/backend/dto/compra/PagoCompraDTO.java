package com.tiendayuliana.backend.dto.compra;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PagoCompraDTO(
        @NotNull @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero") BigDecimal monto,
        String observacion
) {}

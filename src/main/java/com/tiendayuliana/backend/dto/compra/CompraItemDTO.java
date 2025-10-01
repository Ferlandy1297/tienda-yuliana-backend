package com.tiendayuliana.backend.dto.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CompraItemDTO(
    @NotNull Integer idProducto,
    @Positive Integer cantidad,
    @Positive BigDecimal costoUnitario,
    LocalDate fechaVencimiento
) {}
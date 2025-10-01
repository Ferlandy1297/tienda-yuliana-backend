package com.tiendayuliana.backend.dto.compra;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public record CompraCreateDTO(
    @NotNull Integer idProveedor,
    String condicion, // CONTADO | CREDITO
    @NotEmpty List<CompraItemDTO> items,
    @PositiveOrZero BigDecimal pagoInicial, // opcional: si CONTADO puede venir igual al total
    String observacion
) {}
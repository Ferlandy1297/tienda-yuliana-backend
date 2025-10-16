package com.tiendayuliana.backend.dto.caducidad;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CaducidadAccionDTO(
        @NotNull String accion, // DESCUENTO | DONACION | DEVOLUCION
        @NotEmpty @Valid List<Item> items,
        BigDecimal porcentajeDescuento // opcional; aplica para DESCUENTO (0 < p <= 1)
) {
    public record Item(@NotNull Integer idLote, @NotNull Integer cantidad) {}
}


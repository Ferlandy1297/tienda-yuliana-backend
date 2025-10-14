package com.tiendayuliana.backend.dto.devolucion;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DevolucionCreateDTO(
        @NotNull Integer idProveedor,
        @NotEmpty @Valid List<Item> items,
        String motivo
) {
    public record Item(@NotNull Integer idProducto, @NotNull Integer idLote, @NotNull Integer cantidad) {}
}


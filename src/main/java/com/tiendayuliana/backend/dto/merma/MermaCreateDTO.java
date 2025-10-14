package com.tiendayuliana.backend.dto.merma;

import jakarta.validation.constraints.NotNull;

public record MermaCreateDTO(
        @NotNull Integer idProducto,
        @NotNull Integer cantidad,
        String motivo
) {}


package com.tiendayuliana.backend.dto.cc;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record AbonoCreateDTO(
        @NotNull @DecimalMin("0.01") BigDecimal monto,
        String observacion
) {}


package com.tiendayuliana.backend.dto.promocion;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PromocionCreateDTO(
        @NotBlank String nombre,
        @NotNull @DecimalMin("0.00") BigDecimal porcentaje,
        LocalDate desde,
        LocalDate hasta,
        Boolean activo
) {}


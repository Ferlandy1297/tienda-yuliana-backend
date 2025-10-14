package com.tiendayuliana.backend.dto.promocion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PromocionResponseDTO(
        Integer idPromocion,
        String nombre,
        BigDecimal porcentaje,
        LocalDate desde,
        LocalDate hasta,
        Boolean activo
) {}


package com.tiendayuliana.backend.dto.reportes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReporteVentasDTO(
        String periodo,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal totalVentas,
        long totalTransacciones,
        List<DetalleDia> detalle
) {
    public record DetalleDia(LocalDate fecha, BigDecimal totalDia, long cantidadVentas) {}
}

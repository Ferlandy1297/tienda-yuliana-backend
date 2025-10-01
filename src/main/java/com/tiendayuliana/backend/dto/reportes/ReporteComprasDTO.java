package com.tiendayuliana.backend.dto.reportes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReporteComprasDTO(LocalDate fecha, BigDecimal total) {}